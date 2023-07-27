package com.example.newsline

import android.app.Application
import com.example.newsline.di.InstancesProvider
import com.example.newsline.di.ServiceLocator

class App : Application() {

    lateinit var instanceProvider: InstancesProvider

    override fun onCreate() {
        super.onCreate()
        instanceProvider = ServiceLocator(this).instanceProvider
    }
}