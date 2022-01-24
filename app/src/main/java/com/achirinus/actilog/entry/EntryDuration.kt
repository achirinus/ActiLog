package com.achirinus.actilog.entry

import java.lang.Exception

class EntryDuration (var hours: Int, var minutes: Int, var seconds: Int){

    companion object {
        fun parseString( stringVal: String) : EntryDuration {
            val list = stringVal.split(":")
            val actDur = EntryDuration(0,0,0)
            if(list.size < 3) throw Exception("Invalid duration input")
            actDur.hours = list[0].toInt()
            actDur.minutes = list[1].toInt()
            actDur.seconds = list[2].toInt()
            return actDur
        }
    }

    constructor() : this(0,0, 0) {
    }

    public override fun toString() : String {
        return "${hours}H ${minutes}m ${seconds}s"
    }

    public fun add(dur: EntryDuration) : EntryDuration {
        var actDur = EntryDuration(hours, minutes, seconds)

        actDur.hours += dur.hours
        actDur.minutes += dur.minutes
        actDur.seconds += dur.seconds

        if(actDur.seconds >= 60){
            actDur.seconds -= 60
            actDur.minutes++
        }

        if(actDur.minutes >= 60){
            actDur.minutes -= 60
            actDur.hours++
        }

        return actDur
    }

    public fun getDurationInSeconds() : Int {
        return seconds + (60 * minutes) + (3600 * hours)
    }

    public fun fromSeconds(inSec: Int) {
        minutes = inSec / 60
        seconds = inSec % 60
        hours = minutes / 60
        minutes %= 60
    }
}