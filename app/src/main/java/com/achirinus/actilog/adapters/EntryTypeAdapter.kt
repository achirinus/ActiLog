package com.achirinus.actilog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.achirinus.actilog.*
import com.achirinus.actilog.entry.*

class EntryTypeAdapter (private val context: Context, private val data: EntryData)
    : RecyclerView.Adapter<EntryTypeAdapter.ActivityTypeViewHolder>() {

    class ActivityTypeViewHolder(private val view: View) : RecyclerView.ViewHolder(view), AdapterView.OnItemSelectedListener {
        var itemType: EntryType = EntryType.Running
        var entryData: EntryData? = null

        val nameText: TextView = view.findViewById(R.id.nameText);
        val durationText: TextView = view.findViewById(R.id.durationText);
        val durationAvgText: TextView = view.findViewById(R.id.durationAvgText);
        val distanceLayout : LinearLayout = view.findViewById(R.id.distanceLayout);
        val speedLayout : LinearLayout = view.findViewById(R.id.speedLayout);
        val inclineLayout : LinearLayout = view.findViewById(R.id.inclineLayout);
        val pagesLayout : LinearLayout = view.findViewById(R.id.pagesLayout);
        val spinnerLayout : LinearLayout = view.findViewById(R.id.spinnerLayout);

        val distanceText : TextView = view.findViewById(R.id.distanceText);
        val speedText : TextView = view.findViewById(R.id.speedText);
        val inclineText : TextView = view.findViewById(R.id.inclineText);
        val pagesText : TextView = view.findViewById(R.id.pagesText);
        val itemSpinner: Spinner = view.findViewById(R.id.typeItemSpinner);
        val defaultGoneViewList: List<View> = listOf( distanceLayout, speedLayout, inclineLayout, pagesLayout, spinnerLayout)

        var spinnerItems: MutableList<String> = mutableListOf()

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if(position >= spinnerItems.size) return

            if(position == 0)
            {
                bindWithItemList(entryData!!.typeMap[itemType]!!)
            }
            else
            {
                val itemName = spinnerItems[position]
                val itemList = entryData!!.getNamedItemsOfType(itemType, itemName)
                bindWithItemList(itemList)
            }

        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

        fun setAllDefaultsGone() {
            for(viewItem in defaultGoneViewList)
            {
                viewItem.visibility = View.GONE
            }
        }
        fun bindWithItemList(itemList: MutableList<EntryItem>) {
            var totalDuration = EntryDuration(0,0,0)
            var totalDistance = 0
            var totalSpeed = 0
            var totalIncline = 0
            var numEntries = 0
            var pagesTotal = 0

            for(entry in itemList)
            {
                totalDuration = totalDuration.add(entry.duration)
                numEntries++
                when (itemType)
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

            var avgDuration = EntryDuration(0,0,0)
            avgDuration.fromSeconds(avgSeconds)

            nameText.text = itemType.getCustomName()
            durationText.text = totalDuration.toString()
            durationAvgText.text = avgDuration.toString()

            when (itemType)
            {
                EntryType.Running -> {
                    distanceLayout.visibility = View.VISIBLE
                    distanceText.text = totalDistance.toString()
                }
                EntryType.Treadmill -> {
                    speedLayout.visibility = View.VISIBLE
                    inclineLayout.visibility = View.VISIBLE

                    var speedAvg = 0
                    var avgIncline = 0
                    if(numEntries > 0)
                    {
                        speedAvg = totalSpeed / numEntries
                        avgIncline = totalIncline / numEntries
                    }

                    speedText.text = speedAvg.toString()
                    inclineText.text = avgIncline.toString()
                }
                EntryType.Reading -> {
                    pagesLayout.visibility = View.VISIBLE
                    pagesText.text = pagesTotal.toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityTypeViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).
        inflate(R.layout.list_type_item, parent, false);

        return ActivityTypeViewHolder(adapterLayout);
    }



    override fun onBindViewHolder(holder: ActivityTypeViewHolder, position: Int) {
        holder.itemType = EntryType.values()[position]
        holder.entryData = data
        val itemList = data.typeMap[holder.itemType]!!;
        holder.setAllDefaultsGone();

        val tempItem = EntryItemBuilder.build(holder.itemType, EntryDateTime(), EntryDuration())

        if(tempItem is EntryItemWithName)
        {
            holder.spinnerLayout.visibility = View.VISIBLE

            var spinnerItems: MutableList<String> = data.getAlreadyEnteredItemArray(holder.itemType).toMutableList()
            spinnerItems.add(0, "All")

            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerItems)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.itemSpinner.adapter = arrayAdapter
            holder.itemSpinner.onItemSelectedListener = holder

            holder.spinnerItems = spinnerItems
        }
        holder.bindWithItemList(itemList)
    }

    override fun getItemCount(): Int {
        return EntryType.values().size;
    }


}