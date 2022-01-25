package com.achirinus.actilog.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.achirinus.actilog.activities.MainActivity
import com.achirinus.actilog.R
import com.achirinus.actilog.adapters.EntryItemAdapter


class EntriesFragment : TabFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var itemAdapter: EntryItemAdapter

    private val sortPreferenceTag = "sort_pref"

    private fun getSortPref() : Int {
        val sharedPref = requireActivity().getSharedPreferences(sortPreferenceTag, 0)
        return sharedPref.getInt("sort_order", 0)
    }
    private fun setSortPref(prefVal: Int) {
        val sharedPrefEdit = requireActivity().getSharedPreferences(sortPreferenceTag, 0).edit()
        sharedPrefEdit.putInt("sort_order", prefVal)
        sharedPrefEdit.apply()
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_entries, container, false)

        val activityList : RecyclerView = view.findViewById(R.id.activityList)
        activityList.setHasFixedSize(true)
        itemAdapter = EntryItemAdapter(view.context, MainActivity.actData.entriesList)
        activityList.adapter = itemAdapter

        val spinner: Spinner = view.findViewById(R.id.sortSpinner)
        ArrayAdapter.createFromResource(view.context, R.array.sort_array, android.R.layout.simple_spinner_item).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
        spinner.setSelection(getSortPref())
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val activityList: RecyclerView = requireView().findViewById(R.id.activityList)
        when (pos)
        {
            0 -> { //Sort By Date Asc
                MainActivity.actData.entriesList.sortBy { it.date }
            }
            1 -> { //Sort By Date Desc
                MainActivity.actData.entriesList.sortByDescending { it.date }
            }
            2 -> { //Sort By Type
                MainActivity.actData.entriesList.sortBy { it.type }
            }
        }
        activityList.adapter!!.notifyDataSetChanged()
        setSortPref(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun refreshList() {
        itemAdapter.notifyDataSetChanged()
    }
}