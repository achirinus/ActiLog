package com.achirinus.actilog.entry

import androidx.lifecycle.ViewModel


class EntryData : ViewModel{
    val tagMap: MutableMap<EntryTag, MutableList<EntryItem>> = mutableMapOf()
    val typeMap: MutableMap<EntryType, MutableList<EntryItem>> = mutableMapOf()

    var dataLists: EntryDataLists = EntryDataLists()
    var entriesList: MutableList<EntryItem> = mutableListOf()
    var startedEntries: MutableList<EntryItem> = mutableListOf()
    constructor() {
        clearAll()
    }

    fun clearAll() {
        for(tag in EntryTag.values())
        {
            tagMap[tag] = mutableListOf()
        }

        for(actType in EntryType.values())
        {
            typeMap[actType] = mutableListOf()
        }
        dataLists = EntryDataLists()
        entriesList.clear()
        startedEntries.clear()
    }

    public fun getEntriesWithTag(tag: EntryTag) : List<EntryItem> {
        return tagMap[tag]!!.toList()
    }

    public fun getEntriesWithType(type: EntryType): List<EntryItem> {
        return typeMap[type]!!.toList()
    }

    fun getEntryIndex(entryItem: EntryItem): Int {
        for(idx in entriesList.indices)
        {
            if(entriesList[idx] == entryItem)
            {
                return idx
            }
        }
        return -1
    }

    fun getAtIndex(index: Int) : EntryItem? {
        var res : EntryItem? = null

        if(index < entriesList.size) {
            res = entriesList[index]
        }
        return res
    }

    fun getStartedAtIndex(index: Int) : EntryItem? {
        var res : EntryItem? = null

        if(index < startedEntries.size) {
            res = startedEntries[index]
        }
        return res
    }

    public fun addEntry(act: EntryItem) {
        if(act.status != EntryStatus.Finished)
        {
            startedEntries.add(act)
            return
        }
        else
        {
            startedEntries.remove(act)
        }

        entriesList.add(act)
        dataLists.add(act)
        for(actTag in  act.tags)
        {
            tagMap[actTag]?.add(act)
        }
        typeMap[act.type]?.add(act)
    }

    public fun removeEntry(act: EntryItem) {
        entriesList.remove(act)
        dataLists.remove(act)
        for(actTag in  act.tags)
        {
            tagMap[actTag]?.remove(act)
        }
        typeMap[act.type]?.remove(act)
    }

    public fun removeStartedEntry(item: EntryItem) {
        startedEntries.remove(item)
    }

    public fun updateStartedEntry(item: EntryItem) {
        for(itemIdx in startedEntries.indices)
        {
            if(startedEntries[itemIdx].equals(item)){
                startedEntries.set(itemIdx, item)
                break
            }
        }
    }

    public fun fromLists(actLists: EntryDataLists) {
        clearAll()

        for(act in actLists.runningList)
        {
            addEntry(act)
        }
        for(act in actLists.weightList)
        {
            addEntry(act)
        }
        for(act in actLists.treadList)
        {
            addEntry(act)
        }
        for(act in actLists.bagList)
        {
            addEntry(act)
        }
        for(act in actLists.readingList)
        {
            addEntry(act)
        }
        for(act in actLists.languageList)
        {
            addEntry(act)
        }
        for(act in actLists.skillList)
        {
            addEntry(act)
        }
        for(act in actLists.gamingList)
        {
            addEntry(act)
        }
        actLists.removeStartedItems()
    }

    fun getAlreadyEnteredItemArray(type: EntryType) :Array<String> {
        val set = mutableSetOf<String>()
        for(act in typeMap[type]!!)
        {
            val changedAct = act as EntryItemWithName
            set.add(changedAct.itemName)
        }
        return set.toTypedArray()
    }

    fun getNamedItemsOfType(itemType: EntryType, itemName: String) : MutableList<EntryItem> {
        var namedItemList: MutableList<EntryItem> = mutableListOf()

        val itemList = typeMap[itemType]!!
        for(item in itemList)
        {
            val namedItem = item as EntryItemWithName
            if(namedItem.itemName == itemName)
            {
                namedItemList.add(item)
            }
        }
        return namedItemList
    }

}