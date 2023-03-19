package com.example.newsline.domain

import android.app.Application
import android.content.Context


class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private lateinit var context: android.content.Context
        val appContext: Context
            get() = context
    }
}