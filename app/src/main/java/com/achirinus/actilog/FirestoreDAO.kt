package com.achirinus.actilog

import com.achirinus.actilog.entry.*
import com.achirinus.actilog.interfaces.FirestoreCaller
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirestoreDAO {

    private val db = Firebase.firestore

    private val userCollectionName = "Users"
    private val userEntryCollectionName = "Entries"

    fun addEntry(item: EntryItem, caller: FirestoreCaller) {
        var task = db.collection(userCollectionName).document(caller.getUser().getID())
            .collection(userEntryCollectionName).document(item.getUID()).set(item)
        task = task.addOnSuccessListener {
            caller.onAddEntrySuccess(item)
        }
        task.addOnFailureListener {
            caller.onAddEntryFail()
        }
    }

    fun updateEntry(item: EntryItem, caller: FirestoreCaller) {
        var task = db.collection(userCollectionName).document(caller.getUser().getID())
            .collection(userEntryCollectionName).document(item.getUID()).set(item)
        task = task.addOnSuccessListener {
            caller.onUpdateEntrySuccess(item)
        }
        task.addOnFailureListener {
            caller.onUpdateEntryFail()
        }
    }

    fun finishEntry(item: EntryItem, caller: FirestoreCaller) {
        var task = db.collection(userCollectionName).document(caller.getUser().getID())
            .collection(userEntryCollectionName).document(item.getUID()).set(item)
        task = task.addOnSuccessListener {
            caller.onFinishEntrySuccess(item)
        }
        task.addOnFailureListener {
            caller.onFinishEntryFail()
        }
    }

    fun addUser(item: ActiLogUser, caller: FirestoreCaller) {

        var task = db.collection(userCollectionName).document(item.getID()).set(item)
        task = task.addOnSuccessListener {
            caller.onAddUserSuccess(item)
        }
        task.addOnFailureListener {
            caller.onAddUserFail()
        }
    }

    fun loadEntries(caller: FirestoreCaller) {
        var task = db.collection(userCollectionName).document(caller.getUser().getID())
            .collection(userEntryCollectionName).get()
        task = task.addOnSuccessListener {
            val documents = it.documents

            val entries = EntryDataLists()

            for(doc in documents) {
                val itemType = doc.get("type", EntryType::class.java)
                var entryItem: EntryItem? = EntryItem()
                when(itemType)
                {
                    EntryType.Running -> {
                        entryItem = doc.toObject(EntryItemRunning::class.java)
                    }
                    EntryType.WeightLifting -> {
                        entryItem = doc.toObject(EntryItemWeightLifting::class.java)
                    }
                    EntryType.Treadmill -> {
                        entryItem = doc.toObject(EntryItemTreadMill::class.java)
                    }
                    EntryType.HeavyBag -> {
                        entryItem = doc.toObject(EntryItemHeavyBag::class.java)
                    }
                    EntryType.Reading -> {
                        entryItem = doc.toObject(EntryItemReading::class.java)
                    }
                    EntryType.Language -> {
                        entryItem = doc.toObject(EntryItemLanguage::class.java)
                    }
                    EntryType.Skill -> {
                        entryItem = doc.toObject(EntryItemSkill::class.java)
                    }
                    EntryType.Gaming -> {
                        entryItem = doc.toObject(EntryItemGaming::class.java)
                    }
                }
                if(entryItem != null)
                {
                    entries.add(entryItem)
                }
            }
            caller.onLoadEntriesSuccess(entries)
        }
        task.addOnFailureListener {
            caller.onLoadEntriesFail()
        }
    }

    fun removeEntry(item: EntryItem, caller: FirestoreCaller) {
        var task = db.collection(userCollectionName).document(caller.getUser().getID())
            .collection(userEntryCollectionName).document(item.getUID()).delete()
        task = task.addOnSuccessListener {
            caller.onRemoveEntrySuccess(item)
        }
        task.addOnFailureListener {
            caller.onRemoveEntryFail()
        }
    }
}