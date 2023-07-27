package com.example.newsline.di

import android.content.Context

class ServiceLocator(context: Context) {

    val instanceProvider: InstancesProvider = BaseInstancesProvider(context)

}