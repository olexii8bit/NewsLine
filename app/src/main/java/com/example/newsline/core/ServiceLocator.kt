package com.example.newsline.core

import android.content.Context

class ServiceLocator(context: Context) {

    val instanceProvider: InstancesProvider = BaseInstancesProvider(context)

}