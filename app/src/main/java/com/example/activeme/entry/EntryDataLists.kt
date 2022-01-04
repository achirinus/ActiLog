package com.example.activeme.entry

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
}