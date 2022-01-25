package com.achirinus.actilog.entry

enum class EntryType
{
    Running,
    WeightLifting{
        override fun getCustomName() : String = "Weight Lifting";
            },
    Treadmill,
    HeavyBag{
        override fun getCustomName() : String = "Heavy Bag";
            },
    Reading,
    Language,
    Skill,
    Gaming;

    open fun getCustomName() : String = name
}