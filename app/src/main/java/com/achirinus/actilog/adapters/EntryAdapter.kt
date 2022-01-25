package com.achirinus.actilog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.achirinus.actilog.*
import com.achirinus.actilog.activities.MainActivity
import com.achirinus.actilog.entry.*

class EntryItemAdapter (private val context: Context, private val dataList: List<EntryItem>)
    : RecyclerView.Adapter<EntryItemAdapter.EntryItemViewHolder>(){

    class EntryItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nameText: TextView = view.findViewById(R.id.nameText)
        val dateText: TextView = view.findViewById(R.id.dateText)
        val durationText: TextView = view.findViewById(R.id.durationText)

        val infoText: TextView = view.findViewById(R.id.infoText)
        val infoLayout: LinearLayout = view.findViewById(R.id.infoLayout)
        val tagSeparator: View = view.findViewById(R.id.tagSeparator)
        val tagLayout: LinearLayout = view.findViewById(R.id.tagLayout)
        val exerciseTag: ImageView = view.findViewById(R.id.exerciseTagView)
        val learningTag: ImageView = view.findViewById(R.id.learningTagView)
        val practiceTag: ImageView = view.findViewById(R.id.practiceTagView)
        val funTag: ImageView = view.findViewById(R.id.funTagView)
        val itemNameLayout : LinearLayout = view.findViewById(R.id.itemNameLayout)
        val distanceLayout : LinearLayout = view.findViewById(R.id.distanceLayout)
        val setsLayout : LinearLayout = view.findViewById(R.id.setsLayout)
        val repsLayout : LinearLayout = view.findViewById(R.id.repsLayout)
        val weightLayout : LinearLayout = view.findViewById(R.id.weightLayout)
        val speedLayout : LinearLayout = view.findViewById(R.id.speedLayout)
        val inclineLayout : LinearLayout = view.findViewById(R.id.inclineLayout)
        val pagesLayout : LinearLayout = view.findViewById(R.id.pagesLayout)

        val itemNameText : TextView = view.findViewById(R.id.itemNameText)
        val distanceText : TextView = view.findViewById(R.id.distanceText)
        val setsText : TextView = view.findViewById(R.id.setsText)
        val repsText : TextView = view.findViewById(R.id.repsText)
        val weightText : TextView = view.findViewById(R.id.weightText)
        val speedText : TextView = view.findViewById(R.id.speedText)
        val inclineText : TextView = view.findViewById(R.id.inclineText)
        val pagesText : TextView = view.findViewById(R.id.pagesText)
        val itemNameValText : TextView = view.findViewById(R.id.itemNameValText)

        val removeEntry: Button = view.findViewById(R.id.removeEntry)

        val defaultGoneViewList: List<View> = listOf(infoLayout, tagSeparator, tagLayout,
            exerciseTag, learningTag, practiceTag, funTag, itemNameLayout, distanceLayout,
            setsLayout, repsLayout, weightLayout, speedLayout, inclineLayout, pagesLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).
        inflate(R.layout.list_entry_item, parent, false)

        return EntryItemViewHolder(adapterLayout)
    }

    fun setAllDefaultsGone(holder: EntryItemViewHolder) {
        for(viewItem in holder.defaultGoneViewList)
        {
            viewItem.visibility = View.GONE
        }
    }

    override fun onBindViewHolder(holder: EntryItemViewHolder, position: Int) {
        val item = dataList[position]
        setAllDefaultsGone(holder)

        if(item is EntryItemWithName)
        {
            if(item.itemTitle.isEmpty())
            {
                val tempItem = EntryItemBuilder.build(item.type, EntryDateTime(), EntryDuration()) as EntryItemWithName
                item.itemTitle =tempItem.itemTitle
            }
        }

        holder.nameText.text = item.getName()
        holder.dateText.text = item.date.toString()
        holder.durationText.text = item.duration.toString()

        holder.removeEntry.setOnClickListener{
            val act = context as MainActivity
            act.removeEntry(item)
        }

        if(item.info.isNotEmpty())
        {
            holder.infoLayout.visibility = View.VISIBLE
            holder.infoText.text = item.info
        }
        else
        {
            holder.infoLayout.visibility = View.GONE
        }
        if(item.tags.isNotEmpty())
        {
            holder.tagSeparator.visibility = View.VISIBLE
            holder.tagLayout.visibility =View.VISIBLE

            for(tagItem in item.tags)
            {
                when (tagItem)
                {
                    EntryTag.Exercise -> holder.exerciseTag.visibility = View.VISIBLE
                    EntryTag.Learning -> holder.learningTag.visibility = View.VISIBLE
                    EntryTag.Practice -> holder.practiceTag.visibility = View.VISIBLE
                    EntryTag.Fun -> holder.funTag.visibility = View.VISIBLE
                }
            }
        }
        else
        {
            setAllDefaultsGone(holder)
            holder.tagSeparator.visibility = View.GONE
            holder.tagLayout.visibility = View.GONE
        }

        when (item.type)
        {
            EntryType.Running -> {
                val castedItem = item as EntryItemRunning
                holder.distanceLayout.visibility = View.VISIBLE
                holder.distanceText.text = castedItem.getDistanceString()
            }
            EntryType.WeightLifting -> {
                val castedItem = item as EntryItemWeightLifting
                holder.setsLayout.visibility = View.VISIBLE
                holder.setsText.text = castedItem.sets.toString()

                holder.repsLayout.visibility = View.VISIBLE
                holder.repsText.text = castedItem.reps.toString()

                holder.weightLayout.visibility = View.VISIBLE
                holder.weightText.text = castedItem.kg.toString()

                holder.itemNameLayout.visibility = View.VISIBLE
                holder.itemNameText.text = castedItem.itemTitle
                holder.itemNameValText.text = castedItem.itemName
            }
            EntryType.Treadmill -> {
                val castedItem = item as EntryItemTreadMill
                holder.speedLayout.visibility = View.VISIBLE
                holder.speedText.text = castedItem.speed.toString()

                holder.inclineLayout.visibility = View.VISIBLE
                holder.inclineText.text = castedItem.incline.toString()
            }
            EntryType.HeavyBag -> {

            }
            EntryType.Reading -> {
                val castedItem = item as EntryItemReading

                holder.itemNameLayout.visibility = View.VISIBLE
                holder.itemNameText.text = castedItem.itemTitle
                holder.itemNameValText.text = castedItem.itemName

                holder.pagesLayout.visibility = View.VISIBLE
                holder.pagesText.text = castedItem.pages.toString()
            }
            EntryType.Language -> {
                val castedItem = item as EntryItemLanguage
                holder.itemNameLayout.visibility = View.VISIBLE
                holder.itemNameText.text = castedItem.itemTitle
                holder.itemNameValText.text = castedItem.itemName
            }
            EntryType.Skill -> {
                val castedItem = item as EntryItemSkill
                holder.itemNameLayout.visibility = View.VISIBLE
                holder.itemNameText.text = castedItem.itemTitle
                holder.itemNameValText.text = castedItem.itemName
            }
            EntryType.Gaming -> {
                val castedItem = item as EntryItemGaming
                holder.itemNameLayout.visibility = View.VISIBLE
                holder.itemNameText.text = castedItem.itemTitle
                holder.itemNameValText.text = castedItem.itemName
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}