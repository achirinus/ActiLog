package com.example.activeme.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.activeme.R
import com.example.activeme.adapters.EntryItemAdapter
import com.example.activeme.adapters.RestoreItemAdapter

class RestoreBackupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restore_backup)

        val backUpList : RecyclerView = findViewById(R.id.backupList);
        backUpList.adapter = RestoreItemAdapter(this, MainActivity.savedData);
    }
}