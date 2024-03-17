package com.example.utilityui

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText

object InputValidation {

    // Check if text is empty on editText.
    fun isEmpty(editText: EditText?): Boolean {

        if (editText!!.text!!.isEmpty()) {
            editText.error = "Fill require"
            return true
        }

        return false
    }


    // Check if text is empty on autoCompleteTextView.
    fun isEmpty(autoCompleteTextView: AutoCompleteTextView): Boolean {

        if (autoCompleteTextView.text!!.isEmpty()) {
            autoCompleteTextView.error = "Fill require"
            return true
        }

        return false
    }


    // Get text from the user using editText .
    fun text(editText: EditText?, defValue: Boolean = false, msg: String = "Not defined"): String {

        if (isEmpty(editText) && defValue) {
            editText!!.error = null
            return msg // Return default text.
        }

        return editText?.text.toString().trim()
    }

    // Get text from the user using autoCompleteTextView.
    fun text(autoCompleteTextView: AutoCompleteTextView, defValue: Boolean = false, msg: String = "Not defined"): String {

        if (isEmpty(autoCompleteTextView) && defValue) {
            autoCompleteTextView.error = null
            return msg // Return default text.
        }

        return autoCompleteTextView.text.toString().trim()
    }

    // AutoCompleteTextView
    fun setAutoTextAdapter(autoCompleteTextView: AutoCompleteTextView, array: Array<String>, context: Context) {

        val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, array)
        autoCompleteTextView.setAdapter(arrayAdapter)
    }

}