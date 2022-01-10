package com.example.activeme.entry



class EntryData {
    val tagMap: MutableMap<EntryTag, MutableList<EntryItem>> = mutableMapOf()
    val typeMap: MutableMap<EntryType, MutableList<EntryItem>> = mutableMapOf()

    var datalLists: EntryDataLists = EntryDataLists()
    var entriesList: MutableList<EntryItem> = mutableListOf()
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
        datalLists = EntryDataLists()
        entriesList = mutableListOf()
    }

    public fun getEntriesWithTag(tag: EntryTag) : List<EntryItem> {
        return tagMap[tag]!!.toList()
    }

    public fun getEntriesWithType(type: EntryType): List<EntryItem> {
        return typeMap[type]!!.toList()
    }

    public fun addEntry(act: EntryItem) {
        entriesList.add(act)
        datalLists.add(act)
        for(actTag in  act.tags)
        {
            tagMap[actTag]?.add(act)
        }
        typeMap[act.type]?.add(act)
    }

    public fun removeEntry(act: EntryItem) {
        entriesList.remove(act)
        datalLists.remove(act)
        for(actTag in  act.tags)
        {
            tagMap[actTag]?.remove(act)
        }
        typeMap[act.type]?.remove(act)
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