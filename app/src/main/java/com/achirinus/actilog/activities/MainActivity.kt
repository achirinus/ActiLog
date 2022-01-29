package com.achirinus.actilog.activities

import android.content.Intent
import android.graphics.Rect
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.achirinus.actilog.ActiLogUser
import com.achirinus.actilog.FirestoreDAO
import com.achirinus.actilog.R
import com.achirinus.actilog.adapters.ItemToSelectListAdapter
import com.achirinus.actilog.adapters.StartedItemListAdapter
import com.achirinus.actilog.entry.*
import com.achirinus.actilog.fragments.*
import com.achirinus.actilog.interfaces.FirestoreCaller
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() , FirestoreCaller, NavigationView.OnNavigationItemSelectedListener {
    private val TAG = "MainActivity"

    private val firestoreDAO: FirestoreDAO = FirestoreDAO
    private lateinit var auth: FirebaseAuth
    private var isProfileMenuOpen = false
    private var profileFragment = ProfileMenuFragment()
    private var tabFragment = TabFragment()
    private lateinit var drawer: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var navViewRight: NavigationView
    private lateinit var startedList: RecyclerView

    companion object {
        var actData: EntryData = EntryData()
        var lastTabSelected: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainToolbar: Toolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(mainToolbar)

        drawer = findViewById(R.id.drawerLayout)
        val drawerToggle = ActionBarDrawerToggle(this, drawer, mainToolbar, R.string.drawer_open, R.string.drawer_close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navView = findViewById(R.id.navigationView)
        navView.setNavigationItemSelectedListener(this)

        navViewRight = findViewById(R.id.navigationViewRight)
        val rightLayout = navViewRight.getHeaderView(0)
        startedList = rightLayout.findViewById(R.id.navRightList);
        startedList.adapter = StartedItemListAdapter(this, actData.startedEntries)

        auth = Firebase.auth

        if(auth.currentUser == null)
        {
            Log.d(TAG, "Current user is null, starting LoginActivity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        val user: FirebaseUser = auth.currentUser!!
        if(user.email != null)
        {
            val navHeader = navView.getHeaderView(0)
            val profileEmail: TextView = navHeader.findViewById(R.id.profileEmail)
            profileEmail.text = user.email

            val profileImage: ImageView = navHeader.findViewById(R.id.profileImage)
            profileImage.setImageURI(user.photoUrl)

            Log.d(TAG, "Main Activity started with user: ${user.email}")
        }
        firestoreDAO.loadEntries(this)
        setupTabs()
    }

    override fun onResume() {
        super.onResume()

        drawer.closeDrawer(GravityCompat.END)
        startedList.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START)
        }
        else
        {
            super.onBackPressed()
        }
    }
    private fun setupTabs() {
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> tabFragment = TagsFragment()
                    1 -> tabFragment = TypeFragment()
                    2 -> tabFragment = EntriesFragment()
                }

                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.simpleFrameLayout, tabFragment)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        var nextTabIndex = lastTabSelected + 1
        nextTabIndex = nextTabIndex.mod(tabLayout.tabCount)
        tabLayout.getTabAt(nextTabIndex)?.select()
        tabLayout.getTabAt(lastTabSelected)?.select()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.menuAdd -> {
                val tabLayout: TabLayout = findViewById(R.id.tab_layout)
                lastTabSelected = tabLayout.selectedTabPosition
                val intent = Intent(this, SelectItemToAddActivity::class.java)
                startActivity(intent)
            }
            R.id.menuProgress -> {
                if(drawer.isDrawerOpen(GravityCompat.END))
                {
                    drawer.closeDrawer(GravityCompat.END)
                }
                else
                {
                    startedList.adapter?.notifyDataSetChanged()
                    drawer.openDrawer(GravityCompat.END)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun removeEntry(item: EntryItem) {
        firestoreDAO.removeEntry(item, this)
    }

    override fun onRemoveEntrySuccess(item: EntryItem) {
        actData.removeEntry(item)
        tabFragment.refreshList()
    }

    override fun onRemoveEntryFail() {
        Log.d(TAG, "Failed to remove entry")
    }

    override fun onLoadEntriesFail() {
        Log.d(TAG, "Failed to load entry")
    }

    override  fun onLoadEntriesSuccess(entries: EntryDataLists) {
        actData.fromLists(entries)
        tabFragment.refreshList()
    }

    override fun getUser(): ActiLogUser {
        val fireUser = Firebase.auth.currentUser
        if(fireUser != null) return ActiLogUser(fireUser)
        return ActiLogUser()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.drawerMenuLogout ->
            {
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        return true
    }


}



