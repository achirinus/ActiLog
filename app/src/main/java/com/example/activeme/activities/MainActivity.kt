package com.example.activeme.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.activeme.R
import com.example.activeme.entry.EntryData
import com.example.activeme.entry.EntryDataLists
import com.example.activeme.entry.EntryDateTime
import com.example.activeme.entry.EntryItem
import com.example.activeme.fragments.EntriesFragment
import com.example.activeme.fragments.TagsFragment
import com.example.activeme.fragments.TypeFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import java.io.FileNotFoundException
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    companion object {
        var actData: EntryData = EntryData()
        var lastTabSelected: Int = 0

        const val ENTRY_SAVE_FILE_NAME = "save.json"
        const val SETTINGS_SAVE_FILE_NAME = "settings.json"
        fun removeEntry(act: EntryItem) {
            actData.removeEntry(act)

        }
        fun saveEntries(context: Context) {
            val jsonString = Gson().toJson(actData.datalLists)
            context.openFileOutput(ENTRY_SAVE_FILE_NAME, Activity.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }
        }
    }

    fun loadEntries() {
        var savedJson : String = ""
        try
        {
            val inputStream = openFileInput(ENTRY_SAVE_FILE_NAME)
            savedJson = inputStream.bufferedReader().readText()

            var actDataLists = Gson().fromJson(savedJson, EntryDataLists::class.java)
            actData.fromLists(actDataLists)

            val timeNow = LocalDateTime.now()
            val customTime = EntryDateTime(timeNow)
            val customTimeString = customTime.toDateString()
            val cacheFileName = "${customTimeString}-$ENTRY_SAVE_FILE_NAME"
            openFileOutput(cacheFileName, Activity.MODE_PRIVATE).use {
                it.write(savedJson.toByteArray())
            }
        } catch (e: FileNotFoundException)
        {
            e.printStackTrace()
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

        loadEntries()
        setupTabs()
        val floatingButton: FloatingActionButton = findViewById(R.id.addButton)
        floatingButton.setOnClickListener {
            val tabLayout: TabLayout = findViewById(R.id.tab_layout)
            lastTabSelected = tabLayout.selectedTabPosition
            val intent = Intent(it.context, SelectItemToAddActivity::class.java)
            it.context.startActivity(intent)
        }
    }
}



