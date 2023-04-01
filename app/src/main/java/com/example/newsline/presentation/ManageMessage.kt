package com.example.newsline.presentation

import android.content.Context
import android.widget.Toast

interface ManageMessage {

    fun show(message: String,
            duration: Int)

    class ManageToastMessage(private val context: Context): ManageMessage {
        override fun show(message: String,
                          duration: Int) =
            Toast.makeText(context, message, duration).show()
    }
}