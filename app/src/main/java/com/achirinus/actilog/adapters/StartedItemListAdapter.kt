package com.achirinus.actilog.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.achirinus.actilog.entry.EntryType
import com.achirinus.actilog.activities.AddItemActivity
import com.achirinus.actilog.R
import com.achirinus.actilog.activities.MainActivity
import com.achirinus.actilog.entry.EntryItem

class StartedItemListAdapter(private val context: Context, private val dataList: MutableList<EntryItem>)
    : RecyclerView.Adapter<StartedItemListAdapter.StartedItemViewHolder>() {

    class StartedItemViewHolder(public val view: View) : RecyclerView.ViewHolder(view) {
        val itemToSelectText: TextView = view.findViewById(R.id.itemToSelectText)
        val startDate: TextView = view.findViewById(R.id.startDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartedItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).
        inflate(R.layout.list_started_item, parent, false);

        return StartedItemListAdapter.StartedItemViewHolder(adapterLayout);
    }

    override fun onBindViewHolder(holder: StartedItemViewHolder, position: Int) {
        val item = dataList[position];
        holder.itemToSelectText.text = item.type.getCustomName()
        holder.startDate.text = "Started on ${item.date}"

        holder.view.setOnClickListener {
            val intent = Intent(holder.itemToSelectText.context, AddItemActivity::class.java)
            intent.putExtra(AddItemActivity.ITEM_INDEX, position)
            holder.itemToSelectText.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}