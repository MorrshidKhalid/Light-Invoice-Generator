package com.example.utilityui

import android.database.Cursor
import java.text.DateFormat
import java.util.Date

class Utility {

    companion object {


        // Get the column data as string from the database.
        fun getColumnDataAsString(cursor: Cursor, clm: String): String {
            return cursor.getString(cursor.getColumnIndexOrThrow(clm))
        }


        // Get the Primary-Key.
        fun getID(cursor: Cursor, clm: String): Long {
            return cursor.getLong(cursor.getColumnIndexOrThrow(clm))
        }


        fun currentDate(): String {
            val date = Date()
            val formatter = DateFormat.getDateInstance()

            return formatter.format(date)
        }
    }

}