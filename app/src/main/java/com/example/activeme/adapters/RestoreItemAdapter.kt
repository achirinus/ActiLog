package com.example.activeme.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.activeme.R
import com.example.activeme.activities.AddItemActivity
import com.example.activeme.activities.MainActivity
import com.example.activeme.entry.EntryDateTime
import com.example.activeme.entry.EntrySaveData
import com.example.activeme.entry.EntryType
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class RestoreItemAdapter(private val context: Context, private val dataList: MutableList<EntrySaveData>)
    : RecyclerView.Adapter<RestoreItemAdapter.RestoreItemViewHolder>() {

    class RestoreItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.nameText)
        val selectBackup: Button = view.findViewById(R.id.selectBackup)
        val removeBackup: Button = view.findViewById(R.id.removeBackup)
        val dateText: TextView = view.findViewById(R.id.dateText)
        val entriesText: TextView = view.findViewById(R.id.entriesText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoreItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).
        inflate(R.layout.list_restore_item, parent, false);

        return RestoreItemViewHolder(adapterLayout);
    }

    override fun onBindViewHolder(holder: RestoreItemViewHolder, position: Int) {
        val item = dataList[position];

        holder.nameText.text = item.name;
        holder.dateText.text = item.dateModified.toStringForPath()
        holder.entriesText.text = item.entriesList.getSize().toString()

        holder.selectBackup.setOnClickListener{
            MainActivity.restoreSave(context, item)
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)

        }
        holder.removeBackup.setOnClickListener{
            MainActivity.removeSave(context, item)
            this@RestoreItemAdapter.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}