package com.example.tt_bssd5250_hw51

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [NoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class NoteFragment : Fragment() {
    private var resultKey: String = ""
    private var sentColor: String = ""
    private var indexVal:Int = -1

    private lateinit var nameView: TextView
    private lateinit var dateView: TextView
    private lateinit var descView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(resultKey) { requestKey, bundle ->
            val curNote = NotesData.getNoteList()[indexVal]
            nameView.text = curNote.name
            dateView.text = curNote.date
            descView.text = curNote.desc
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Text for the left side
        nameView = TextView(context).apply {
            setHint(R.string.name_place_holder)
            text = NotesData.getNoteList()[indexVal].name
            textSize = 15f
        }
        dateView = TextView(context).apply {
            setHint(R.string.date_place_holder)
            text = NotesData.getNoteList()[indexVal].date
            textSize = 15f
        }
        descView = TextView(context).apply {
            setHint(R.string.desc_place_holder)
            text = NotesData.getNoteList()[indexVal].desc
            textSize = 15f
        }

        val textHolderL = LinearLayoutCompat(requireContext()).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(nameView)
            addView(dateView)
            addView(descView)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.ALIGN_PARENT_LEFT
            )
        }

        val editButton = Button(requireContext()).apply {
            text = "Edit"
            id = View.generateViewId()
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.ALIGN_PARENT_RIGHT,
            )
            setOnClickListener {
                val currentData = bundleOf(
                    "name" to nameView.text,
                    "date" to dateView.text,
                    "desc" to descView.text,
                )
                NoteEditorDialog.newInstance(resultKey, currentData, indexVal).show(
                    parentFragmentManager, NoteEditorDialog.TAG
                )
            }
        }


        val deleteButton = Button(requireContext()).apply {
            text = "Delete"
            setTextColor(Color.RED)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.LEFT_OF, editButton.id
            )

            setOnClickListener {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete Note?")
                    setPositiveButton("Yes") { _, _ ->
                        activity?.supportFragmentManager?.commit {
                            NotesData.deleteNote(indexVal)
                        }
                        val parentActivity = activity as MainActivity
                        parentActivity.noteTotal--
                    }
                    setNegativeButton("No", null)
                    create()
                    show()
                }
            }
        }

        val relativeLayout = RelativeLayout(requireContext()).apply {
            setBackgroundColor(Color.parseColor(sentColor))
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            addView(editButton)
            addView(deleteButton)
            addView(textHolderL)
        }
        return relativeLayout
    }

    companion object {
        fun newInstance(unique: Int) =
            NoteFragment().apply {
                indexVal = unique
                resultKey = "NoteDataChange$unique"
                val importance = NotesData.getNoteList()[indexVal].priority
                sentColor  = if (importance == "true"){
                    "RED"
                } else{
                    "WHITE"
                }

            }
    }
}