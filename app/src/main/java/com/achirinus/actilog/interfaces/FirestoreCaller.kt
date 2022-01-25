package com.achirinus.actilog.interfaces

import com.achirinus.actilog.ActiLogUser
import com.achirinus.actilog.entry.EntryDataLists
import com.achirinus.actilog.entry.EntryItem

interface FirestoreCaller {

    fun getUser() :ActiLogUser {return ActiLogUser()}

    fun onAddEntrySuccess(item: EntryItem) {}
    fun onAddEntryFail() {}
    fun onRemoveEntrySuccess(item: EntryItem) {}
    fun onRemoveEntryFail() {}
    fun onLoadEntriesSuccess(entries: EntryDataLists) {}
    fun onLoadEntriesFail() {}

    fun onAddUserSuccess(item: ActiLogUser) {}
    fun onAddUserFail() {}
}