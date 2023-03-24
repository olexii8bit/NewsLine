package com.example.newsline

import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.telephony.TelephonyManager

interface LocationService {

    fun getCurrentLocationCountry(): String

    class Base(private val currentContext: Context): LocationService {
        override fun getCurrentLocationCountry(): String {
            val telephonyManager = currentContext.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val currentCode = telephonyManager.networkCountryIso
            com.example.newsline.data.newsApi.enums.Country.values().forEach {
                if (it.value == currentCode) return currentCode
            }
            return "us"
        }
    }
}