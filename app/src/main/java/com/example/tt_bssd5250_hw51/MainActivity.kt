package com.example.tt_bssd5250_hw51

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.commit
import org.json.JSONArray
import java.io.*

class MainActivity : NotesData.NotesDataUpdateListener, AppCompatActivity() {

    override fun updateNotesDependents() {
        createNoteFragments()
    }

    private fun removeExistingNotes() {
        for (noteF in supportFragmentManager.fragments){
            supportFragmentManager.commit {
                remove(noteF)
            }
        }
    }

    private fun createNoteFragments(){
        removeExistingNotes()
        for (i in 0 until NotesData.getNoteList().size){
            supportFragmentManager.commit {
                add(fid, NoteFragment.newInstance(i), null)
            }
        }
    }

    var noteTotal = 0

    private var fid = 0

    override fun onPause() {
        super.onPause()
        writeDataToFile()
    }

    override fun onResume() {
        super.onResume()
        val jsonArray = readDataAsJSON()
        if (jsonArray != null) {
            loadJSONNotes(jsonArray)
            Log.d("array", jsonArray.toString())
            createNoteFragments()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotesData.registerListener(this)

        val fragmentLinearLayout = LinearLayoutCompat(this).apply {
            id = View.generateViewId()
            fid = id
            orientation = LinearLayoutCompat.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }

        val addButton = Button(this).apply {
            text = "New Todo"
            id = View.generateViewId()
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                supportFragmentManager.commit {
                    if (noteTotal < 2) {
                        NotesData.addNote(Note("", "", null, "false"))
                        val uniqueID = NotesData.getNoteList().size - 1
                        add(
                            fid,
                            NoteFragment.newInstance(uniqueID),
                            null
                        )
                        noteTotal++
                    }
                }
            }
        }
        val redAddButton = Button(this).apply {
            text = "New Todo"
            setTextColor(Color.RED)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                supportFragmentManager.commit {
                    if (noteTotal < 2) {
                        NotesData.addNote(Note("", "", null, "true"))
                        val uniqueID = NotesData.getNoteList().size - 1
                        add(
                            fid,
                            NoteFragment.newInstance(uniqueID),
                            null
                        )
                        noteTotal++
                    }
                }
            }
        }

        val buttonLayout = LinearLayoutCompat(this).apply {
            orientation = LinearLayoutCompat.HORIZONTAL
            addView(addButton)
            addView(redAddButton)
        }


        val linearLayout = LinearLayoutCompat(this).apply {
            setBackgroundColor(Color.LTGRAY)
            orientation = LinearLayoutCompat.VERTICAL
            addView(buttonLayout)
            addView(fragmentLinearLayout)
        }

        setContentView(linearLayout)

    }

    private fun readDataAsJSON(): JSONArray? {
        try {
            var fileInputStream: FileInputStream? = openFileInput("notes.json")
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String?
            while (run {
                    text = bufferedReader.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }
            fileInputStream?.close()
            return JSONArray(stringBuilder.toString())
        } catch (e: FileNotFoundException) {
            return null
        }
    }

    private fun loadJSONNotes(data: JSONArray) {
        NotesData.loadNotes(data)
    }


    private fun writeDataToFile() {
        val file = "notes.json"
        val data: String = NotesData.toJSON().toString()

        try {
            val fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

}