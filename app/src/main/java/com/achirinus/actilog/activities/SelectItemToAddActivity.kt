package com.achirinus.actilog.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.achirinus.actilog.R
import com.achirinus.actilog.adapters.ItemToSelectListAdapter
import com.achirinus.actilog.entry.EntryType

class SelectItemToAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_item_to_add)

        val activityList : RecyclerView = findViewById(R.id.itemToAddList);
        activityList.setHasFixedSize(true)
        activityList.adapter = ItemToSelectListAdapter(this, EntryType.values())

    }
}