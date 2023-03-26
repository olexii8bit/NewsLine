package com.example.newsline.presentation

import android.app.Application
import android.content.Context

class App : Application() {

    lateinit var appContext: Context

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}