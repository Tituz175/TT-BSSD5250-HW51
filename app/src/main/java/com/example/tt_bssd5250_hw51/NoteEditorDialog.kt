package com.example.tt_bssd5250_hw51

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.DialogFragment

class NoteEditorDialog : DialogFragment() {

    var targetResultKey: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val editName = EditText(requireContext()).apply {
            setHint(R.string.name_place_holder)
            setText(NotesData.getNoteList()[indexVal].name)
        }
        val editDate = EditText(requireContext()).apply {
            setText(NotesData.getNoteList()[indexVal].date)
        }
        val editDesc = EditText(requireContext()).apply {
            setHint(R.string.desc_place_holder)
            setText(NotesData.getNoteList()[indexVal].desc)
        }

        val linearLayout = LinearLayoutCompat(requireContext()).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(editName)
            addView(editDate)
            addView(editDesc)
        }

        val ad = AlertDialog.Builder(requireContext()).apply {
            setTitle("Note Editor")
            setTitle("Edit Content")
            setView(linearLayout)
            setPositiveButton("Save") { _, _ ->
                NotesData.getNoteList()[indexVal].name = editName.toString()
                NotesData.getNoteList()[indexVal].date = editDate.toString()
                NotesData.getNoteList()[indexVal].desc = editDesc.toString()
            }
            setNegativeButton("Cancel") { _, _ -> }
        }
        return ad.create()
    }

    companion object {
        const val TAG = "NoteEditorDialog"

        var existingData: Bundle = Bundle.EMPTY
        var indexVal: Int = -1

        @JvmStatic
        fun newInstance(target: String, existing: Bundle, unique: Int) =
            NoteEditorDialog().apply {
                indexVal = unique
                targetResultKey = target
                existingData = existing
            }
    }
}