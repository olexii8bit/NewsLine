package com.example.newsline.core

import android.content.Context
import com.example.newsline.domain.LocationService
import com.example.newsline.presentation.ManageMessage

class BaseInstancesProvider(
    private val context: Context,
): AbstractInstancesProvider() {
    override fun provideToastMessageManager(): ManageMessage =
        ManageMessage.ManageToastMessage(context)

    override fun provideLocationService(): LocationService =
        LocationService.Base(context)

}