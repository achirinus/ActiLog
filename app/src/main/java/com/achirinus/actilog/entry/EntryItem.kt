package com.achirinus.actilog.entry

import java.time.LocalDateTime


open class EntryItem (val type : EntryType, val date: EntryDateTime, var duration: EntryDuration, var tags: MutableList<EntryTag>){

    constructor() : this(EntryType.Running, EntryDateTime(), EntryDuration(), mutableListOf())

    open var info: String = ""
    var status: EntryStatus = EntryStatus.Finished
    var resumedDate:EntryDateTime = date

    fun getName(): String = type.getCustomName()
    fun getUID(): String {
        return "${type.name}-${date.toStringForPath()}"
    }

    fun equals(other: EntryItem): Boolean {
        if(type != other.type) return false
        if(date.compareTo(other.date) != 0) return false

        return true
    }

    fun durationToNow(): EntryDuration {
        val dateNow = EntryDateTime(LocalDateTime.now())
        var dur = dateNow.durationFrom(resumedDate)
        return dur.add(duration)
    }
}

class EntryItemBuilder {
    companion object {
        fun build(type: EntryType, date: EntryDateTime, duration: EntryDuration): EntryItem {

            return when (type) {
                EntryType.Running -> EntryItemRunning(date, duration)
                EntryType.WeightLifting -> EntryItemWeightLifting(date, duration)
                EntryType.Treadmill -> EntryItemTreadMill(date, duration)
                EntryType.HeavyBag -> EntryItemHeavyBag(date, duration)
                EntryType.Reading -> EntryItemReading(date, duration)
                EntryType.Language -> EntryItemLanguage(date, duration)
                EntryType.Skill -> EntryItemSkill(date, duration)
                EntryType.Gaming -> EntryItemGaming(date, duration)
            }
        }
    }
}


class EntryItemRunning (date: EntryDateTime, duration: EntryDuration) : EntryItem(EntryType.Running, date, duration, mutableListOf(EntryTag.Exercise)) {

    constructor() : this(EntryDateTime(), EntryDuration())

    var distance: Int = 0

    fun getDistanceString() : String {
        if(distance > 1000)
        {
            val floatKm: Float = (distance.toFloat()) / 1000
            return ("$floatKm km")
        }
        return ("$distance m")
    }
}

open class EntryItemWithName (type : EntryType, date: EntryDateTime, duration: EntryDuration, var itemTitle: String, tags: MutableList<EntryTag>) : EntryItem(type, date, duration, tags) {
    open var itemName: String = ""
}

class EntryItemWeightLifting (date: EntryDateTime, duration: EntryDuration) : EntryItemWithName(
    EntryType.WeightLifting, date, duration, "Exercise", mutableListOf(
        EntryTag.Exercise
    )) {

    constructor() : this(EntryDateTime(), EntryDuration())

    var sets: Int = 0
    var reps: Int = 0
    var kg: Int = 0
}

class EntryItemTreadMill (date: EntryDateTime, duration: EntryDuration) : EntryItem(EntryType.Treadmill, date, duration, mutableListOf(EntryTag.Exercise)) {
    constructor() : this(EntryDateTime(), EntryDuration())

    var speed: Int = 0
    var incline: Int = 0
}
class EntryItemHeavyBag (date: EntryDateTime, duration: EntryDuration) : EntryItem(EntryType.HeavyBag, date, duration, mutableListOf(EntryTag.Exercise)) {
    constructor() : this(EntryDateTime(), EntryDuration())
}

class EntryItemReading (date: EntryDateTime, duration: EntryDuration) : EntryItemWithName(
    EntryType.Reading, date, duration, "Book", mutableListOf(
        EntryTag.Learning,
        EntryTag.Practice
    )) {

    constructor() : this(EntryDateTime(), EntryDuration())
    var pages: Int = 0
}

class EntryItemLanguage (date: EntryDateTime, duration: EntryDuration) : EntryItemWithName(
    EntryType.Language, date, duration, "Language", mutableListOf(
        EntryTag.Learning,
        EntryTag.Practice
    )) {

    constructor() : this(EntryDateTime(), EntryDuration())
}

class EntryItemSkill (date: EntryDateTime, duration: EntryDuration) : EntryItemWithName(
    EntryType.Skill, date, duration, "Skill",mutableListOf(
        EntryTag.Learning,
        EntryTag.Practice
    )) {

    constructor() : this(EntryDateTime(), EntryDuration())
}

class EntryItemGaming (date: EntryDateTime, duration: EntryDuration) : EntryItemWithName(
    EntryType.Gaming, date, duration, "Game", mutableListOf(
        EntryTag.Fun
    )) {
    constructor() : this(EntryDateTime(), EntryDuration())
}



