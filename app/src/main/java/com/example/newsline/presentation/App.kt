package com.example.newsline.presentation

import android.app.Application
import com.example.newsline.core.InstancesProvider
import com.example.newsline.core.ServiceLocator

class App : Application() {

    lateinit var instanceProvider: InstancesProvider

    override fun onCreate() {
        super.onCreate()
        instanceProvider = ServiceLocator(this).instanceProvider
    }
}