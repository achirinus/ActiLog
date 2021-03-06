package com.achirinus.actilog.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.achirinus.actilog.activities.MainActivity
import com.achirinus.actilog.R
import com.achirinus.actilog.adapters.EntryTypeAdapter


class TypeFragment : TabFragment() {
    lateinit var itemAdapter: EntryTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_type, container, false)

        val activityList : RecyclerView = view.findViewById(R.id.activityList);

        itemAdapter = EntryTypeAdapter(view.context, MainActivity.actData);

        activityList.adapter = itemAdapter

        return view
    }

    override fun refreshList() {
        itemAdapter.notifyDataSetChanged()
    }
}