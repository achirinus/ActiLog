package com.achirinus.actilog.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.achirinus.actilog.ActiLogUser
import com.achirinus.actilog.FirestoreDAO
import com.achirinus.actilog.R
import com.achirinus.actilog.entry.*
import com.achirinus.actilog.interfaces.FirestoreCaller
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

class AddItemActivity : AppCompatActivity(), FirestoreCaller {
    private val TAG = "AddItemActivity"
    val firestoreDAO: FirestoreDAO = FirestoreDAO

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

            return act
        }catch (e: Exception) {
             throw e
        }
    }

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var miscFunc: () -> EntryItem = {EntryItemSkill(EntryDateTime(), EntryDuration()) }

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
                    act
                }
            }
            EntryType.WeightLifting -> {
                setContentView(R.layout.activity_add_weightlift)

                val editItem: AutoCompleteTextView = findViewById(R.id.editItemName)
                val arr = MainActivity.actData.getAlreadyEnteredItemArray(currentType)
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr)
                editItem.setAdapter(adapter)

                miscFunc = {
                    val act = makeBaseActivity(EntryType.WeightLifting) as EntryItemWeightLifting

                    val editItemName: EditText = findViewById(R.id.editItemName)
                    val editSets: EditText = findViewById(R.id.editSets)
                    val editReps: EditText = findViewById(R.id.editReps)
                    val editKg: EditText = findViewById(R.id.editKg)

                    act.itemName = editItemName.text.toString()
                    act.sets = editSets.text.toString().toInt()
                    act.reps = editReps.text.toString().toInt()
                    act.kg = editKg.text.toString().toInt()
                    setEntryTagsFromSwitch(act)
                    act
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
                    act
                }

            }
            EntryType.HeavyBag -> {
                setContentView(R.layout.activity_add_heavybag)
                miscFunc = {
                    val act = makeBaseActivity(EntryType.HeavyBag)
                    setEntryTagsFromSwitch(act)
                    act
                }

            }
            EntryType.Reading -> {
                setContentView(R.layout.activity_add_reading)

                val editItem: AutoCompleteTextView = findViewById(R.id.editItemName)
                val arr = MainActivity.actData.getAlreadyEnteredItemArray(currentType)
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr)
                editItem.setAdapter(adapter)
                miscFunc = {
                    val act = makeBaseActivity(EntryType.Reading) as EntryItemReading

                    val editItemName: EditText = findViewById(R.id.editItemName)
                    val editPages: EditText = findViewById(R.id.editPages)

                    act.pages = editPages.text.toString().toInt()
                    act.itemName = editItemName.text.toString()
                    setEntryTagsFromSwitch(act)
                    act
                }

            }
            EntryType.Language -> {
                setContentView(R.layout.activity_add_language)

                val editItem: AutoCompleteTextView = findViewById(R.id.editItemName)
                val arr = MainActivity.actData.getAlreadyEnteredItemArray(currentType)
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr)
                editItem.setAdapter(adapter)
                miscFunc = {
                    val act = makeBaseActivity(EntryType.Language) as EntryItemLanguage

                    val editItemName: EditText = findViewById(R.id.editItemName)

                    act.itemName = editItemName.text.toString()
                    setEntryTagsFromSwitch(act)
                    act
                }

            }
            EntryType.Skill -> {
                setContentView(R.layout.activity_add_skills)

                val editItem: AutoCompleteTextView = findViewById(R.id.editItemName)
                val arr = MainActivity.actData.getAlreadyEnteredItemArray(currentType)
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr)
                editItem.setAdapter(adapter)
                miscFunc = {
                   val act = makeBaseActivity(EntryType.Skill) as EntryItemSkill

                    val editItemName: EditText = findViewById(R.id.editItemName)

                    act.itemName = editItemName.text.toString()
                    setEntryTagsFromSwitch(act)
                    act
                }
            }
            EntryType.Gaming -> {
                setContentView(R.layout.activity_add_gaming)

                val editItem: AutoCompleteTextView = findViewById(R.id.editItemName)
                val arr = MainActivity.actData.getAlreadyEnteredItemArray(currentType)
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr)
                editItem.setAdapter(adapter)

                miscFunc = {
                    val act = makeBaseActivity(EntryType.Gaming) as EntryItemGaming

                    val editItemName: EditText = findViewById(R.id.editItemName)

                    act.itemName = editItemName.text.toString()
                    setEntryTagsFromSwitch(act)
                    act
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
                val entryItem = miscFunc.invoke()
                firestoreDAO.addEntry(entryItem, this)
            }catch (e: Exception) {
                Log.d(TAG, e.toString())
                Toast.makeText(this@AddItemActivity, "Invalid duration input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this@AddItemActivity, MainActivity::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
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

        if(v !is TextView) return
        val textView = v
        val inputText = textView.text

        val arr = MainActivity.actData.getAlreadyEnteredItemArray(currentType)
        for(str in  arr)
        {
            if(str.startsWith(inputText))
            {
                menu?.add(0, v.id, 0, str)
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val editItemName: EditText = findViewById(R.id.editItemName)
        editItemName.setText(item.title)
        return true
    }

    override fun onAddEntrySuccess(item: EntryItem) {
        MainActivity.actData.addEntry(item)
        Log.d(TAG, "Added entry to firestore")
    }

    override fun onAddEntryFail() {
        Log.d(TAG, "Failed to add entry to firestore")
    }

    override fun getUser(): ActiLogUser {
        val user = Firebase.auth.currentUser
        if(user != null) return ActiLogUser(user)
        return ActiLogUser()
    }
}