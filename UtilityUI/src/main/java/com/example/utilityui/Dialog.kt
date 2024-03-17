package com.example.utilityui

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Dialog {


    fun confirmDelete(context: Context): MaterialAlertDialogBuilder {

        return MaterialAlertDialogBuilder(context)
            .setTitle(R.string.delTitle)
            .setMessage(R.string.delMsg)
            .setCancelable(false)
    }

}