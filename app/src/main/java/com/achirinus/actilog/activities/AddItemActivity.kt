package com.achirinus.actilog.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
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
    var currentType = EntryType.Running

    lateinit var addButton : Button
    lateinit var startButton : Button
    lateinit var cancelButton : Button
    lateinit var pauseButton : Button
    lateinit var resumeButton : Button
    lateinit var exerciseSwitch: Switch
    lateinit var learningSwitch: Switch
    lateinit var practiceSwitch: Switch
    lateinit var funSwitch: Switch
    lateinit var durationEdit: EditText
    var entryItem: EntryItem? = null


    var durationRunnable: Runnable? = null
    val durationHandler = Handler(Looper.getMainLooper())

    var onMyClick: (isStarting: Boolean) -> EntryItem = {EntryItemSkill(EntryDateTime(), EntryDuration()) }

    companion object{
        const val ITEM_TYPE = "type"
        const val ITEM_INDEX = "item_index"
    }

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras
        if(extras == null)
        {
            Log.d(TAG, "Activity started without extras in intent")
            return
        }
        val enumName: String? = extras.getString(ITEM_TYPE)
        val entryIndex: Int = extras.getInt(ITEM_INDEX, -1)
        if(entryIndex >=0)
        {
            entryItem = MainActivity.actData.getStartedAtIndex(entryIndex)
            if(entryItem == null)
            {
                Log.d(TAG, "Activity started with entry index but no entry was found")
                return
            }

            currentType = entryItem!!.type
        }
        else if(enumName != null)
        {
            val type = EntryType.valueOf(enumName)
            currentType = type
        }
        else
        {
            Log.d(TAG, "Activity started without info in extras")
            return
        }

        when (currentType){
            EntryType.Running -> {
                setContentView(R.layout.activity_add_running)

                if(entryItem != null)
                {
                    val entry = entryItem as EntryItemRunning
                    val distanceEdit: EditText = findViewById(R.id.editDistance)
                    distanceEdit.setText(entry.distance.toString())
                }
                onMyClick = {
                    val act = makeBaseActivity(EntryType.Running, it) as EntryItemRunning
                    val distanceEdit: EditText = findViewById(R.id.editDistance)
                    act.distance = getIntValueFromEdit(distanceEdit)
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

                if(entryItem != null)
                {
                    val entry = entryItem as EntryItemWeightLifting
                    val editSets: EditText = findViewById(R.id.editSets)
                    val editReps: EditText = findViewById(R.id.editReps)
                    val editKg: EditText = findViewById(R.id.editKg)
                    editItem.setText(entry.itemName)
                    editSets.setText(entry.sets.toString())
                    editReps.setText(entry.reps.toString())
                    editKg.setText(entry.kg.toString())
                }

                onMyClick = {
                    val act = makeBaseActivity(EntryType.WeightLifting, it) as EntryItemWeightLifting

                    val editItemName: EditText = findViewById(R.id.editItemName)
                    val editSets: EditText = findViewById(R.id.editSets)
                    val editReps: EditText = findViewById(R.id.editReps)
                    val editKg: EditText = findViewById(R.id.editKg)

                    act.itemName = editItemName.text.toString()
                    act.sets = getIntValueFromEdit(editSets)
                    act.reps = getIntValueFromEdit(editReps)
                    act.kg = getIntValueFromEdit(editKg)
                    setEntryTagsFromSwitch(act)
                    act
                }

            }
            EntryType.Treadmill -> {
                setContentView(R.layout.activity_add_treadmill)

                if(entryItem != null)
                {
                    val entry = entryItem as EntryItemTreadMill
                    val editSpeed: EditText = findViewById(R.id.editSpeed)
                    val editIncline: EditText = findViewById(R.id.editIncline)
                    editSpeed.setText(entry.speed.toString())
                    editIncline.setText(entry.incline.toString())
                }
                onMyClick = {
                    val act = makeBaseActivity(EntryType.Treadmill, it) as EntryItemTreadMill

                    val editSpeed: EditText = findViewById(R.id.editSpeed)
                    val editIncline: EditText = findViewById(R.id.editIncline)

                    act.speed = getIntValueFromEdit(editSpeed)
                    act.incline = getIntValueFromEdit(editIncline)
                    setEntryTagsFromSwitch(act)
                    act
                }

            }
            EntryType.HeavyBag -> {
                setContentView(R.layout.activity_add_heavybag)

                onMyClick = {
                    val act = makeBaseActivity(EntryType.HeavyBag, it)
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

                if(entryItem != null)
                {
                    val entry = entryItem as EntryItemReading
                    val editItemName: EditText = findViewById(R.id.editItemName)
                    val editPages: EditText = findViewById(R.id.editPages)
                    editItemName.setText(entry.itemName)
                    editPages.setText(entry.pages.toString())
                }

                onMyClick = {
                    val act = makeBaseActivity(EntryType.Reading, it) as EntryItemReading

                    val editItemName: EditText = findViewById(R.id.editItemName)
                    val editPages: EditText = findViewById(R.id.editPages)

                    act.pages = getIntValueFromEdit(editPages)
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

                if(entryItem != null)
                {
                    val entry = entryItem as EntryItemLanguage
                    val editItemName: EditText = findViewById(R.id.editItemName)
                    editItemName.setText(entry.itemName)
                }
                onMyClick = {
                    val act = makeBaseActivity(EntryType.Language, it) as EntryItemLanguage
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

                if(entryItem != null)
                {
                    val entry = entryItem as EntryItemSkill
                    val editItemName: EditText = findViewById(R.id.editItemName)
                    editItemName.setText(entry.itemName)
                }
                onMyClick = {
                    val act = makeBaseActivity(EntryType.Skill, it) as EntryItemSkill

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
                if(entryItem != null)
                {
                    val entry = entryItem as EntryItemGaming
                    val editItemName: EditText = findViewById(R.id.editItemName)
                    editItemName.setText(entry.itemName)
                }
                onMyClick = {
                    val act = makeBaseActivity(EntryType.Gaming, it) as EntryItemGaming

                    val editItemName: EditText = findViewById(R.id.editItemName)

                    act.itemName = editItemName.text.toString()
                    setEntryTagsFromSwitch(act)
                    act
                }
            }
        }

        addButton = findViewById(R.id.addButton)
        startButton  = findViewById(R.id.startButton)
        cancelButton  = findViewById(R.id.cancelButton)
        pauseButton  = findViewById(R.id.pauseButton)
        resumeButton  = findViewById(R.id.resumeButton)
        exerciseSwitch = findViewById(R.id.exerciseSwitch)
        learningSwitch = findViewById(R.id.learningSwitch)
        practiceSwitch = findViewById(R.id.practiceSwitch)
        funSwitch = findViewById(R.id.funSwitch)
        durationEdit = findViewById(R.id.editDuration)

        if(entryItem != null)
        {
            if(entryItem!!.status == EntryStatus.Paused)
            {
                durationEdit.setText(entryItem!!.duration.toEditString())

                pauseButton.visibility = View.GONE
                resumeButton.visibility = View.VISIBLE
            }
            else
            {
                pauseButton.visibility = View.VISIBLE
                resumeButton.visibility = View.GONE
                startUpdatingDuration()
            }
            cancelButton.visibility = View.VISIBLE
            startButton.visibility = View.GONE
        }
        else
        {
            startButton.visibility = View.VISIBLE
            cancelButton.visibility = View.GONE
            pauseButton.visibility = View.GONE
            resumeButton.visibility = View.GONE
            durationEdit.doAfterTextChanged {
                if((it?.length == 2) || (it?.length == 5))
                {
                    var strArray = it.split(":")
                    if(strArray.size < 3)
                    {
                        it.append(":")
                    }
                }
            }
        }

        durationEdit.setOnFocusChangeListener { view, b -> if(b) durationEdit.hint = "00:00:00"; }

        val itemNameText: TextView = findViewById(R.id.itemNameText)
        itemNameText.text = currentType.getCustomName()

        setSwitchFromEntryTags()

    }

    override fun onDestroy() {
        super.onDestroy()
        stopUpdatingDuration()
    }

    fun stopUpdatingDuration() {
        if(durationRunnable != null)
        {
            durationHandler.removeCallbacks(durationRunnable!!)
        }
    }

    private fun startUpdatingDuration() {
        durationRunnable = Runnable {
            val duration = entryItem!!.durationToNow()
            durationEdit.setText(duration.toEditString())
            durationHandler.postDelayed(durationRunnable!!, 1000) }

        durationHandler.post (durationRunnable!!)
    }

    private fun getIntValueFromEdit(view: EditText) : Int {
        var intRes = 0
        val strVal = view.text.toString()
        if(!strVal.isEmpty())
        {
            intRes = strVal.toInt()
        }
        return intRes
    }

    private fun makeBaseActivity(type: EntryType, isStarted: Boolean) : EntryItem {
        val timeNow = LocalDateTime.now()

        if(entryItem == null)
        {
            val durationString = durationEdit.text.toString()
            val dur: EntryDuration = EntryDuration.parseString(durationString)
            val act = EntryItemBuilder.build(type, EntryDateTime(timeNow), dur)
            return act
        }
        return entryItem!!
    }

    fun setSwitchFromEntryTags() {
        val act = EntryItemBuilder.build(currentType, EntryDateTime(), EntryDuration(0,0,0))

        exerciseSwitch.isChecked = act.tags.contains(EntryTag.Exercise)
        learningSwitch.isChecked = act.tags.contains(EntryTag.Learning)
        practiceSwitch.isChecked = act.tags.contains(EntryTag.Practice)
        funSwitch.isChecked = act.tags.contains(EntryTag.Fun)
    }

    fun setEntryTagsFromSwitch(act: EntryItem) {
        act.tags.clear()

        if(exerciseSwitch.isChecked) act.tags.add(EntryTag.Exercise)
        if(learningSwitch.isChecked) act.tags.add(EntryTag.Learning)
        if(practiceSwitch.isChecked) act.tags.add(EntryTag.Practice)
        if(funSwitch.isChecked) act.tags.add(EntryTag.Fun)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v !is TextView) return
        val inputText = v.text

        val arr = MainActivity.actData.getAlreadyEnteredItemArray(currentType)
        for (str in arr) {
            if (str.startsWith(inputText)) {
                menu?.add(0, v.id, 0, str)
            }
        }
    }

    fun onAddClicked(view: View) {
        entryItem = onMyClick.invoke(false)
        entryItem?.status = EntryStatus.Finished
        firestoreDAO.finishEntry(entryItem!!, this)
    }

    fun onStartClicked(view: View) {
        Log.d(TAG, "Starting entry")
        startUpdatingDuration()
        entryItem = onMyClick.invoke(true)
        entryItem?.status = EntryStatus.Started
        firestoreDAO.addEntry(entryItem!!, this)
    }

    fun onCancelClicked(view: View) {
        if(entryItem != null)
        {
            stopUpdatingDuration()
            Log.d(TAG, "Stopping entry")
            firestoreDAO.removeEntry(entryItem!!, this)
        }
    }

    fun onPauseClicked(view: View) {
        val durationString = durationEdit.text.toString()
        val dur: EntryDuration = EntryDuration.parseString(durationString)
        entryItem?.duration = dur
        entryItem?.status = EntryStatus.Paused
        firestoreDAO.updateEntry(entryItem!!, this)
    }

    fun onResumeClicked(view: View) {
        val dateNow = EntryDateTime(LocalDateTime.now())
        entryItem!!.status = EntryStatus.Started
        entryItem!!.resumedDate = dateNow
        firestoreDAO.updateEntry(entryItem!!, this)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val editItemName: EditText = findViewById(R.id.editItemName)
        editItemName.setText(item.title)
        return true
    }

    override fun onAddEntrySuccess(item: EntryItem) {
        Log.d(TAG, "Started entry success")
        MainActivity.actData.addEntry(item)
        cancelButton.visibility = View.VISIBLE
        startButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        resumeButton.visibility = View.GONE
    }

    override fun onAddEntryFail() {
        Log.d(TAG, "Failed to add entry to firestore")
    }

    override fun onUpdateEntrySuccess(item: EntryItem) {

        if(item.status == EntryStatus.Paused)
        {
            Log.d(TAG, "Successfully added paused entry to firestore")
            pauseButton.visibility = View.GONE
            resumeButton.visibility = View.VISIBLE
            stopUpdatingDuration()
        }
        else
        {
            Log.d(TAG, "Successfully added resumed entry to firestore")
            startUpdatingDuration()
            pauseButton.visibility = View.VISIBLE
            resumeButton.visibility = View.GONE
        }
        MainActivity.actData.updateStartedEntry(item)
    }

    override fun onUpdateEntryFail() {
        Log.d(TAG, "Failed to update entry to firestore")
    }

    override fun onFinishEntrySuccess(item: EntryItem) {
        Log.d(TAG, "Finished entry added to firestore")
        MainActivity.actData.addEntry(item)
        val intent = Intent(this@AddItemActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onFinishEntryFail() {
        Log.d(TAG, "Failed adding finished entry to firestore")
    }

    override fun onRemoveEntrySuccess(item: EntryItem) {
        Log.d(TAG, "Removed entry ${item.getUID()} from firestore")
        startButton.visibility = View.VISIBLE
        cancelButton.visibility = View.GONE
        pauseButton.visibility = View.GONE
        resumeButton.visibility = View.GONE
        MainActivity.actData.removeStartedEntry(item)
        durationEdit.setText("")
        entryItem = null
    }

    override fun onRemoveEntryFail() {
        Log.d(TAG, "Failed to remove entry")
    }

    override fun getUser(): ActiLogUser {
        val user = Firebase.auth.currentUser
        if(user != null) return ActiLogUser(user)
        return ActiLogUser()
    }
}