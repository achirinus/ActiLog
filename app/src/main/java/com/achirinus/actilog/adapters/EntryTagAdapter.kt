package com.achirinus.actilog.adapters

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.*
import com.achirinus.actilog.*
import com.achirinus.actilog.entry.EntryData
import com.achirinus.actilog.entry.EntryDuration
import com.achirinus.actilog.entry.EntryTag

class EntryTagAdapter (private val context: Context, private val data: EntryData)
    : Adapter<EntryTagAdapter.ActivityTagViewHolder>() {

    class ActivityTagViewHolder(private val view: View) : ViewHolder(view) {
        val baseCard: CardView = view.findViewById(R.id.baseCard)
        val nameText: TextView = view.findViewById(R.id.nameText)
        val entriesText: TextView = view.findViewById(R.id.entriesText)
        val durationText: TextView = view.findViewById(R.id.durationText)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityTagViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).
        inflate(R.layout.list_tag_item, parent, false);

        return EntryTagAdapter.ActivityTagViewHolder(adapterLayout);
    }

    override fun onBindViewHolder(holder: ActivityTagViewHolder, position: Int) {
        val tag: EntryTag = EntryTag.values()[position]
        val list = data.getEntriesWithTag(tag)

        var totalTagDuration: EntryDuration = EntryDuration(0, 0, 0)
        for(entry in list)
        {
            totalTagDuration = totalTagDuration.add(entry.duration)
        }

        var totalDuration = EntryDuration(0,0,0)
        for(listTag in EntryTag.values())
        {
            for(listEntry in data.tagMap[listTag]!!)
            {
                totalDuration = totalDuration.add(listEntry.duration)
            }
        }

        when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {holder.baseCard.setCardBackgroundColor(tag.colorDark)}
            Configuration.UI_MODE_NIGHT_NO -> {holder.baseCard.setCardBackgroundColor(tag.colorLight)}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {holder.baseCard.setCardBackgroundColor(tag.colorLight)}
        }


        holder.nameText.text = tag.name
        holder.entriesText.text = list.size.toString()
        holder.durationText.text = totalTagDuration.toString()
        holder.progressBar.max = totalDuration.getDurationInSeconds()
        holder.progressBar.min = 0
        holder.progressBar.progress = totalTagDuration.getDurationInSeconds()

    }

    override fun getItemCount(): Int {
        return EntryTag.values().size
    }
}