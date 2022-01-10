package com.example.activeme.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.activeme.activities.MainActivity
import com.example.activeme.R
import com.example.activeme.adapters.EntryTagAdapter


class TagsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tags, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activityList : RecyclerView = view.findViewById(R.id.activityList);
        activityList.setHasFixedSize(true)
        activityList.adapter = EntryTagAdapter(view.context, MainActivity.actData);
    }

}