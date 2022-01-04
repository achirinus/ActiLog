package com.example.activeme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.activeme.*
import com.example.activeme.entry.*

class ActivityTypeAdapter (private val context: Context, private val data: EntryData)
    : RecyclerView.Adapter<ActivityTypeAdapter.ActivityTypeViewHolder>(){

    class ActivityTypeViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        val nameText: TextView = view.findViewById(R.id.nameText);
        val durationText: TextView = view.findViewById(R.id.durationText);
        val durationAvgText: TextView = view.findViewById(R.id.durationAvgText);
        val distanceLayout : LinearLayout = view.findViewById(R.id.distanceLayout);
        val speedLayout : LinearLayout = view.findViewById(R.id.speedLayout);
        val inclineLayout : LinearLayout = view.findViewById(R.id.inclineLayout);
        val pagesLayout : LinearLayout = view.findViewById(R.id.pagesLayout);

        val distanceText : TextView = view.findViewById(R.id.distanceText);
        val speedText : TextView = view.findViewById(R.id.speedText);
        val inclineText : TextView = view.findViewById(R.id.inclineText);
        val pagesText : TextView = view.findViewById(R.id.pagesText);

        val defaultGoneViewList: List<View> = listOf( distanceLayout, speedLayout, inclineLayout, pagesLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityTypeViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).
        inflate(R.layout.list_type_item, parent, false);

        return ActivityTypeViewHolder(adapterLayout);
    }

    fun setAllDefaultsGone(holder: ActivityTypeViewHolder) {
        for(viewItem in holder.defaultGoneViewList)
        {
            viewItem.visibility = View.GONE
        }
    }

    override fun onBindViewHolder(holder: ActivityTypeViewHolder, position: Int) {
        val itemEnum: EntryType = EntryType.values()[position]
        val itemList = data.typeMap[itemEnum]!!;
        setAllDefaultsGone(holder);

        var totalDuration: EntryDuration = EntryDuration(0,0,0)
        var totalDistance = 0
        var totalSpeed = 0
        var totalIncline = 0
        var numEntries = 0
        var pagesTotal = 0

        for(entry in itemList)
        {
            totalDuration = totalDuration.add(entry.duration)
            numEntries++
            when (itemEnum)
            {
                EntryType.Running -> {
                    val castedItem = entry as EntryItemRunning
                    totalDistance += castedItem.distance
                }
                EntryType.Treadmill -> {
                    val castedItem = entry as EntryItemTreadMill
                    totalSpeed += castedItem.speed
                    totalIncline += castedItem.incline
                }
                EntryType.Reading -> {
                    val castedItem = entry as EntryItemReading
                    pagesTotal += castedItem.pages
                }
            }
        }
        var avgSeconds = totalDuration.getDurationInSeconds()
        if(numEntries > 0)
        {
            avgSeconds /= numEntries
        }

        var avgDuration: EntryDuration = EntryDuration(0,0,0)
        avgDuration.fromSeconds(avgSeconds)

        holder.nameText.text = itemEnum.getCustomName()
        holder.durationText.text = totalDuration.toString()
        holder.durationAvgText.text = avgDuration.toString()

        when (itemEnum)
        {
            EntryType.Running -> {
                holder.distanceLayout.visibility = View.VISIBLE
                holder.distanceText.text = totalDistance.toString()
            }
            EntryType.Treadmill -> {
                holder.speedLayout.visibility = View.VISIBLE
                holder.inclineLayout.visibility = View.VISIBLE

                var speedAvg = 0
                var avgIncline = 0
                if(numEntries > 0)
                {
                    speedAvg = totalSpeed / numEntries
                    avgIncline = totalIncline / numEntries
                }

                holder.speedText.text = speedAvg.toString()
                holder.inclineText.text = avgIncline.toString()
            }
            EntryType.Reading -> {
                holder.pagesLayout.visibility = View.VISIBLE
                holder.pagesText.text = pagesTotal.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return EntryType.values().size;
    }
}