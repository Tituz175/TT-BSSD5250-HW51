package com.example.tt_bssd5250_hw51

import android.util.Log
import org.json.JSONObject
import java.util.*


class Note(var name: String, var desc: String, var date: String?, var priority: String) {
    init {
        if (date == null) {
            date = Date().toString()
        }
    }

    fun toJSON():JSONObject {
        val jsonObject = JSONObject().apply {
            put("name", name)
            put("date", date)
            put("desc", desc)
            put("priority", priority)
        }
        Log.d("note", jsonObject.toString())
        return jsonObject
    }

    override fun toString(): String {
        return "$name, $date, $desc, $priority"
    }
}