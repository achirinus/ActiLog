package com.achirinus.actilog.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.achirinus.actilog.activities.MainActivity
import com.achirinus.actilog.R
import com.achirinus.actilog.adapters.EntryItemAdapter
import com.achirinus.actilog.adapters.EntryTagAdapter


class TagsFragment : TabFragment() {
    lateinit var itemAdapter: EntryTagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tags, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activityList : RecyclerView = view.findViewById(R.id.activityList);
        activityList.setHasFixedSize(true)

        itemAdapter = EntryTagAdapter(view.context, MainActivity.actData);

        activityList.adapter = itemAdapter
    }

    override fun refreshList() {
        itemAdapter.notifyDataSetChanged()
    }
}