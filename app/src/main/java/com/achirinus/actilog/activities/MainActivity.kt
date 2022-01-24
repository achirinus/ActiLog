package com.achirinus.actilog.activities

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.achirinus.actilog.R
import com.achirinus.actilog.entry.*
import com.achirinus.actilog.fragments.EntriesFragment
import com.achirinus.actilog.fragments.ProfileMenuFragment
import com.achirinus.actilog.fragments.TagsFragment
import com.achirinus.actilog.fragments.TypeFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.File
import java.io.FileNotFoundException
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var isProfileMenuOpen = false
    private var profileFragment = ProfileMenuFragment()

    companion object {
        var actData: EntryData = EntryData()
        var lastTabSelected: Int = 0
        var savedData: MutableList<EntrySaveData> = mutableListOf()

        const val ENTRY_SAVE_FILE_NAME = "save.json"
        const val SETTINGS_SAVE_FILE_NAME = "settings.json"
        fun removeEntry(act: EntryItem) {
            actData.removeEntry(act)

        }
        fun saveEntries(context: Context) {
            val jsonString = Gson().toJson(actData.dataLists)
            context.openFileOutput(ENTRY_SAVE_FILE_NAME, Activity.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }
        }
        fun restoreSave(context: Context, saveData: EntrySaveData) {
            //Save current file to a temp
            val savedJson = context.openFileInput(ENTRY_SAVE_FILE_NAME).bufferedReader().readText()
            context.openFileOutput("temp.json", Activity.MODE_PRIVATE).use {
                it.write(savedJson.toByteArray())
            }

            //Move backup into current save
            val backupJson = context.openFileInput(saveData.name).bufferedReader().readText()
            context.openFileOutput(ENTRY_SAVE_FILE_NAME, Activity.MODE_PRIVATE).use {
                it.write(backupJson.toByteArray())
            }

            //Make backup from temp
            val timeNow = LocalDateTime.now()
            val customTime = EntryDateTime(timeNow)
            val customTimeString = customTime.toStringForPath()
            val cacheFileName = "${customTimeString}-$ENTRY_SAVE_FILE_NAME"
            context.openFileOutput(cacheFileName, Activity.MODE_PRIVATE).use {
                it.write(savedJson.toByteArray())
            }

            val tempFile = File("${context.filesDir}/temp.json")
            if(tempFile.exists()) tempFile.delete()

            reloadBackupFiles(context)
        }

        fun removeSave(context: Context, saveData: EntrySaveData) {
            val backup = File("${context.filesDir}/${saveData.name}")
            if(backup.exists()) backup.delete()
            reloadBackupFiles(context)
        }

        fun reloadBackupFiles(context: Context) {
            val filesArr = context.fileList()

            savedData.clear()

            for(fileName in filesArr) {
                if(fileName == ENTRY_SAVE_FILE_NAME) continue
                if(!fileName.endsWith("save.json")) continue
                try{
                   val backupFile = File("${context.filesDir.absolutePath}/${fileName}")
                   val inputStream = backupFile.inputStream()
                   val backupJson = inputStream.bufferedReader().readText()
                   var backupLists = Gson().fromJson(backupJson, EntryDataLists::class.java)

                   val lastModTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(backupFile.lastModified()), TimeZone.getDefault().toZoneId())
                   val customTime = EntryDateTime(lastModTime)

                   savedData.add(EntrySaveData(fileName, backupLists, customTime))
                }catch (e: JsonSyntaxException)
                {
                   e.printStackTrace()
                }
            }
            savedData.sortByDescending { it.dateModified }
        }
        fun loadEntries(context: Context) {
            var savedJson : String = ""
            try
            {
                val savedFile = File("${context.filesDir.absolutePath}/${ENTRY_SAVE_FILE_NAME}")
                val lastMod = savedFile.lastModified()
                val inputStream = context.openFileInput(ENTRY_SAVE_FILE_NAME)
                savedJson = inputStream.bufferedReader().readText()

                var actDataLists = Gson().fromJson(savedJson, EntryDataLists::class.java)
                actData.fromLists(actDataLists)

                val lastModTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastMod), TimeZone.getDefault().toZoneId())
                val timeNow = LocalDateTime.now()

                val dur = Duration.between(lastModTime, timeNow)
                if(dur.toDays() > 1)
                {
                    val customTime = EntryDateTime(timeNow)
                    val customTimeString = customTime.toStringForPath()
                    val cacheFileName = "${customTimeString}-$ENTRY_SAVE_FILE_NAME"
                    context.openFileOutput(cacheFileName, Activity.MODE_PRIVATE).use {
                        it.write(savedJson.toByteArray())
                    }
                }

                reloadBackupFiles(context)
            } catch (e: FileNotFoundException)
            {
                e.printStackTrace()
            }
        }
    }

    fun setupTabs() {
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                var frag= Fragment()
                when (tab.position) {
                    0 -> frag = TagsFragment()
                    1 -> frag = TypeFragment()
                    2 -> frag = EntriesFragment()
                }

                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.simpleFrameLayout, frag)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        if(auth.currentUser != null)
        {
            val user: FirebaseUser = auth.currentUser!!
            if(user.email != null)
            {
                Log.d(TAG, "Main Activity started with user: ${user.email}")
            }
        }

        loadEntries(this)
        setupTabs()
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
            R.id.menuHistory -> {
                val intent = Intent(this, RestoreBackupActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}



