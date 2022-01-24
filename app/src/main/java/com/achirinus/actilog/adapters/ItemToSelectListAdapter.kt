package com.achirinus.actilog.adapters

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

class ItemToSelectListAdapter(private val context: Context, private val dataList: Array<EntryType>)
    : RecyclerView.Adapter<ItemToSelectListAdapter.ItemToSelectViewHolder>() {

    class ItemToSelectViewHolder(public val view: View) : RecyclerView.ViewHolder(view) {
        val itemToSelectText: TextView = view.findViewById(R.id.itemToSelectText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemToSelectViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).
        inflate(R.layout.list_to_add_item, parent, false);

        return ItemToSelectListAdapter.ItemToSelectViewHolder(adapterLayout);
    }

    override fun onBindViewHolder(holder: ItemToSelectViewHolder, position: Int) {
        val item = dataList[position];
        holder.itemToSelectText.text = item.getCustomName();
        holder.view.setOnClickListener {
            val intent = Intent(holder.itemToSelectText.context, AddItemActivity::class.java)
            intent.putExtra(AddItemActivity.ITEM_TYPE, item.name)
            holder.itemToSelectText.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}