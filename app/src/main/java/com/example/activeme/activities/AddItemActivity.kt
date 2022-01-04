package com.example.activeme.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.activeme.R
import com.example.activeme.entry.*
import java.time.LocalDateTime

class AddItemActivity : AppCompatActivity() {
    companion object{
        const val ITEM_TYPE = "type"
    }

    var currentType = EntryType.Running

    private fun makeBaseActivity(type: EntryType) : EntryItem {
        val timeNow = LocalDateTime.now()

        val durationEdit: EditText = findViewById(R.id.editDuration)

        val durationString = durationEdit.text.toString()
        try{
            val dur: EntryDuration = EntryDuration.parseString(durationString)
            val act = EntryItemBuilder.build(type, EntryDateTime(timeNow), dur)

            MainActivity.actData.addEntry(act)
            return act
        }catch (e: Exception) {
             throw e
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var miscFunc = {}

        val enumName: String = intent?.extras?.getString(ITEM_TYPE)!!
        val type = EntryType.valueOf(enumName)
        currentType = type
        when (type){
            EntryType.Running -> {
                setContentView(R.layout.activity_add_running)

                miscFunc = {
                    val act = makeBaseActivity(EntryType.Running) as EntryItemRunning

                    val distanceEdit: EditText = findViewById(R.id.editDistance)
                    act.distance = distanceEdit.text.toString().toInt()

                    setEntryTagsFromSwitch(act)
                }
            }
            EntryType.WeightLifting -> {
                setContentView(R.layout.activity_add_weightlift)

                val editItem: EditText = findViewById(R.id.editItemName)
                registerForContextMenu(editItem)

                miscFunc = {
                    val act = makeBaseActivity(EntryType.WeightLifting) as EntryItemWeightLifting

                    val editItemName: EditText = findViewById(R.id.editItemName)
                    val editSets: EditText = findViewById(R.id.editSets)
                    val editReps: EditText = findViewById(R.id.editReps)
                    val editKg: EditText = findViewById(R.id.editKg)

                    act.itemNameText = editItemName.text.toString()
                    act.sets = editSets.text.toString().toInt()
                    act.reps = editReps.text.toString().toInt()
                    act.kg = editKg.text.toString().toInt()
                    setEntryTagsFromSwitch(act)
                }

            }
            EntryType.Treadmill -> {
                setContentView(R.layout.activity_add_treadmill)
                miscFunc = {
                    val act = makeBaseActivity(EntryType.Treadmill) as EntryItemTreadMill

                    val editSpeed: EditText = findViewById(R.id.editSpeed)
                    val editIncline: EditText = findViewById(R.id.editIncline)

                    act.speed = editSpeed.text.toString().toInt()
                    act.incline = editIncline.text.toString().toInt()
                    setEntryTagsFromSwitch(act)
                }

            }
            EntryType.HeavyBag -> {
                setContentView(R.layout.activity_add_heavybag)
                miscFunc = {
                    val act = makeBaseActivity(EntryType.HeavyBag)
                    setEntryTagsFromSwitch(act)
                }

            }
            EntryType.Reading -> {
                setContentView(R.layout.activity_add_reading)

                val editItem: EditText = findViewById(R.id.editItemName)
                registerForContextMenu(editItem)
                miscFunc = {
                    val act = makeBaseActivity(EntryType.Reading) as EntryItemReading

                    val editItemName: EditText = findViewById(R.id.editItemName)
                    val editPages: EditText = findViewById(R.id.editPages)

                    act.pages = editPages.text.toString().toInt()
                    act.itemNameText = editItemName.text.toString()
                    setEntryTagsFromSwitch(act)
                }

            }
            EntryType.Language -> {
                setContentView(R.layout.activity_add_language)

                val editItem: EditText = findViewById(R.id.editItemName)
                registerForContextMenu(editItem)
                miscFunc = {
                    val act = makeBaseActivity(EntryType.Language) as EntryItemLanguage

                    val editItemName: EditText = findViewById(R.id.editItemName)

                    act.itemNameText = editItemName.text.toString()
                    setEntryTagsFromSwitch(act)
                }

            }
            EntryType.Skill -> {
                setContentView(R.layout.activity_add_skills)
                miscFunc = {
                   val act = makeBaseActivity(EntryType.Skill) as EntryItemSkill

                    val editItemName: EditText = findViewById(R.id.editItemName)

                    act.itemNameText = editItemName.text.toString()
                    setEntryTagsFromSwitch(act)
                }
            }
            EntryType.Gaming -> {
                setContentView(R.layout.activity_add_gaming)

                val editItem: EditText = findViewById(R.id.editItemName)
                registerForContextMenu(editItem)
                miscFunc = {
                    val act = makeBaseActivity(EntryType.Gaming) as EntryItemGaming

                    val editItemName: EditText = findViewById(R.id.editItemName)

                    act.itemNameText = editItemName.text.toString()
                    setEntryTagsFromSwitch(act)
                }

            }
        }

        val durationEdit: EditText = findViewById(R.id.editDuration)
        durationEdit.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if((s?.length == 2) || (s?.length == 5))
                {
                    s.append(":")
                }
            }

        })
        durationEdit.setOnFocusChangeListener { view, b -> if(b) durationEdit.hint = "00:00:00"; }

        val itemNameText: TextView = findViewById(R.id.itemNameText)
        itemNameText.text = type.getCustomName()

        setSwitchFromEntryTags()

        val addButton : Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {

            try{
                miscFunc.invoke()
                MainActivity.saveEntries(this)
            }catch (e: Exception) {
                Toast.makeText(this@AddItemActivity, "Invalid duration input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this@AddItemActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    fun setSwitchFromEntryTags() {
        val act = EntryItemBuilder.build(currentType, EntryDateTime(), EntryDuration(0,0,0))

        val exerciseSwitch: Switch = findViewById(R.id.exerciseSwitch)
        exerciseSwitch.isChecked = act.tags.contains(EntryTag.Exercise)

        val learningSwitch: Switch = findViewById(R.id.learningSwitch)
        learningSwitch.isChecked = act.tags.contains(EntryTag.Learning)

        val practiceSwitch: Switch = findViewById(R.id.practiceSwitch)
        practiceSwitch.isChecked = act.tags.contains(EntryTag.Practice)

        val funSwitch: Switch = findViewById(R.id.funSwitch)
        funSwitch.isChecked = act.tags.contains(EntryTag.Fun)
    }

    fun setEntryTagsFromSwitch(act: EntryItem) {

        val exerciseSwitch: Switch = findViewById(R.id.exerciseSwitch)
        val learningSwitch: Switch = findViewById(R.id.learningSwitch)
        val practiceSwitch: Switch = findViewById(R.id.practiceSwitch)
        val funSwitch: Switch = findViewById(R.id.funSwitch)

        act.tags.clear()

        if(exerciseSwitch.isChecked) act.tags.add(EntryTag.Exercise)
        if(learningSwitch.isChecked) act.tags.add(EntryTag.Learning)
        if(practiceSwitch.isChecked) act.tags.add(EntryTag.Practice)
        if(funSwitch.isChecked) act.tags.add(EntryTag.Fun)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val arr = MainActivity.actData.getAlreadyEnteredItemArray(currentType)
        for(str in  arr)
        {
            menu?.add(0, v?.id!!, 0, str)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val editItemName: EditText = findViewById(R.id.editItemName)
        editItemName.setText(item.title)
        return true
    }
}