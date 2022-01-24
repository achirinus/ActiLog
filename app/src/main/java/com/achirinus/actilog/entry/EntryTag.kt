package com.achirinus.actilog.entry

import android.graphics.Color


enum class EntryTag(val colorLight: Int, val colorDark: Int)
{
    Exercise(Color.YELLOW, Color.rgb(76, 84, 0)) ,
    Learning (Color.GREEN, Color.rgb(17, 84, 0)),
    Practice (Color.CYAN, Color.rgb(0, 57, 84)),
    Fun (Color.MAGENTA, Color.rgb(143, 0, 98))

}