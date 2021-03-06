package com.achirinus.actilog.entry

class EntryDataLists {
    var runningList: MutableList<EntryItemRunning> = mutableListOf()
    var weightList: MutableList<EntryItemWeightLifting> = mutableListOf()
    var treadList: MutableList<EntryItemTreadMill> = mutableListOf()
    var bagList: MutableList<EntryItemHeavyBag> = mutableListOf()
    var readingList: MutableList<EntryItemReading> = mutableListOf()
    var languageList: MutableList<EntryItemLanguage> = mutableListOf()
    var skillList: MutableList<EntryItemSkill> = mutableListOf()
    var gamingList: MutableList<EntryItemGaming> = mutableListOf()

    fun add(act: EntryItem) {
        when (act.type)
        {
            EntryType.Running ->
            {
                val changedAct = act as EntryItemRunning
                runningList.add(changedAct)
            }
            EntryType.WeightLifting ->
            {
                val changedAct = act as EntryItemWeightLifting
                weightList.add(changedAct)
            }
            EntryType.Treadmill ->
            {
                val changedAct = act as EntryItemTreadMill
                treadList.add(changedAct)
            }
            EntryType.HeavyBag ->
            {
                val changedAct = act as EntryItemHeavyBag
                bagList.add(changedAct)
            }
            EntryType.Reading ->
            {
                val changedAct = act as EntryItemReading
                readingList.add(changedAct)
            }
            EntryType.Language ->
            {
                val changedAct = act as EntryItemLanguage
                languageList.add(changedAct)
            }
            EntryType.Skill ->
            {
                val changedAct = act as EntryItemSkill
                skillList.add(changedAct)
            }
            EntryType.Gaming ->
            {
                val changedAct = act as EntryItemGaming
                gamingList.add(changedAct)
            }
        }
    }

    fun removeStartedItems() {
        runningList.removeIf {(it.status != EntryStatus.Finished) }
        weightList.removeIf {(it.status != EntryStatus.Finished) }
        treadList.removeIf {(it.status != EntryStatus.Finished) }
        readingList.removeIf {(it.status != EntryStatus.Finished) }
        bagList.removeIf {(it.status != EntryStatus.Finished) }
        languageList.removeIf {(it.status != EntryStatus.Finished) }
        skillList.removeIf {(it.status != EntryStatus.Finished) }
        gamingList.removeIf {(it.status != EntryStatus.Finished) }
    }

    fun remove(act: EntryItem) {
        when (act.type)
        {
            EntryType.Running ->
            {
                val changedAct = act as EntryItemRunning
                runningList.remove(changedAct)
            }
            EntryType.WeightLifting ->
            {
                val changedAct = act as EntryItemWeightLifting
                weightList.remove(changedAct)
            }
            EntryType.Treadmill ->
            {
                val changedAct = act as EntryItemTreadMill
                treadList.remove(changedAct)
            }
            EntryType.HeavyBag ->
            {
                val changedAct = act as EntryItemHeavyBag
                bagList.remove(changedAct)
            }
            EntryType.Reading ->
            {
                val changedAct = act as EntryItemReading
                readingList.remove(changedAct)
            }
            EntryType.Language ->
            {
                val changedAct = act as EntryItemLanguage
                languageList.remove(changedAct)
            }
            EntryType.Skill ->
            {
                val changedAct = act as EntryItemSkill
                skillList.remove(changedAct)
            }
            EntryType.Gaming ->
            {
                val changedAct = act as EntryItemGaming
                gamingList.remove(changedAct)
            }
        }
    }

    fun getSize() : Int {
        return runningList.size + weightList.size + treadList.size + bagList.size + readingList.size + languageList.size + skillList.size + gamingList.size
    }
}