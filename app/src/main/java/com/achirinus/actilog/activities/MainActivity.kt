package com.achirinus.actilog.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import com.achirinus.actilog.ActiLogUser
import com.achirinus.actilog.FirestoreDAO
import com.achirinus.actilog.R
import com.achirinus.actilog.entry.*
import com.achirinus.actilog.fragments.*
import com.achirinus.actilog.interfaces.FirestoreCaller
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() , FirestoreCaller {
    private val TAG = "MainActivity"

    private val firestoreDAO: FirestoreDAO = FirestoreDAO
    private lateinit var auth: FirebaseAuth
    private var isProfileMenuOpen = false
    private var profileFragment = ProfileMenuFragment()
    private var tabFragment = TabFragment()

    companion object {
        var actData: EntryData = EntryData()
        var lastTabSelected: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            Log.d(TAG, "Main Activity started with user: ${user.email}")
        }
        firestoreDAO.loadEntries(this)
        setupTabs()
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
        val profileMenuItem = menu!!.findItem(R.id.menuProfile)
        val profileMenuPic: ImageView = profileMenuItem.actionView.findViewById(R.id.profilePicMenuItem)

        profileMenuPic.setOnClickListener {
            isProfileMenuOpen = !isProfileMenuOpen
            if(isProfileMenuOpen)
            {
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.profileFrameLayout, profileFragment)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ft.commit()
            }
            else
            {
                val ft = supportFragmentManager.beginTransaction()
                ft.remove(profileFragment)
                ft.commit()
            }

        }
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


}



