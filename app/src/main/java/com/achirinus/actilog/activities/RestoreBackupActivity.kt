package com.achirinus.actilog.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.achirinus.actilog.R
import com.achirinus.actilog.adapters.RestoreItemAdapter

class RestoreBackupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restore_backup)

        val backUpList : RecyclerView = findViewById(R.id.backupList);
        backUpList.adapter = RestoreItemAdapter(this, MainActivity.savedData);
    }
}