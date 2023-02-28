package com.example.tt_bssd5250_hw51

import android.util.Log
import org.json.JSONArray


object NotesData {
    private val notesData = ArrayList<Note>()

    interface NotesDataUpdateListener {
        fun updateNotesDependents()
    }

    private var mListener: NotesDataUpdateListener? = null

    fun registerListener(listener: NotesDataUpdateListener) {
        mListener = listener
    }

    fun addNote(note: Note) {
        notesData.add(note)
    }

    fun deleteNote(index:Int){
        notesData.removeAt(index)
        mListener?.updateNotesDependents()
    }

    fun getNoteList(): ArrayList<Note> {
        return notesData
    }

    fun toJSON(): JSONArray {
        val jsonArray = JSONArray()
        for (note in notesData) {
            jsonArray.put(note.toJSON())
        }
        return jsonArray
    }

    fun loadNotes(data: JSONArray) {
        Log.d("NotesData", data.length().toString())
        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            addNote(
                Note(
                    obj.getString("name"),
                    obj.getString("desc"),
                    obj.getString("date"),
                    obj.getString("priority")
                )
            )
        }
    }

    override fun toString(): String {
        var allNotes = ""
        for (note in notesData) {
            allNotes += note.toString()
        }
        return allNotes
    }
}
