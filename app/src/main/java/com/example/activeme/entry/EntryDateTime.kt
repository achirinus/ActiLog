package com.example.activeme.entry

import java.time.LocalDateTime

class EntryDateTime :Comparable<EntryDateTime>{
    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0
    var second = 0

    constructor(date: LocalDateTime) {
        year = date.year
        month = date.monthValue
        day = date.dayOfMonth
        hour = date.hour
        minute = date.minute
        second = date.second
    }
    constructor() {
        year = 0
        month = 0
        day = 0
        hour = 0
        minute = 0
        second = 0
    }

    override fun toString() : String{
        return "${day}/${month}/${year} ${hour}:${minute}:${second}"
    }

    fun toDateString() : String {
       return "${day}-${month}-${year}"
    }

    override fun compareTo(other: EntryDateTime): Int {
        if(year > other.year)
        {
            return 1
        }
        else if(year < other.year)
        {
            return -1
        }
        else
        {
            if(month > other.month)
            {
                return 1
            }
            else if(month < other.month)
            {
                return -1
            }
            else
            {
                if(day > other.day)
                {
                    return 1
                }
                else if(day < other.day)
                {
                    return -1
                }
                else
                {
                    if(hour > other.hour)
                    {
                        return 1
                    }
                    else if(hour < other.hour)
                    {
                        return -1
                    }
                    else
                    {
                        if(minute > other.minute)
                        {
                            return 1
                        }
                        else if(minute < other.minute)
                        {
                            return -1
                        }
                        else
                        {
                            if(second > other.second)
                            {
                                return 1
                            }
                            else if(second < other.second)
                            {
                                return -1
                            }
                        }
                    }
                }
            }
        }
        return 0
    }
}