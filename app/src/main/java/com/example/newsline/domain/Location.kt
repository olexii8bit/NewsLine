package com.example.newsline.domain

import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.telephony.TelephonyManager
import android.util.Log
import com.example.newsline.domain.Application.Companion.appContext
import com.example.newsline.api.enums.Country

interface Location {

    fun getCurrentLocationCountry(): String

    fun getCurrentCountryCode(): Country

    class Base(): Location {
        override fun getCurrentLocationCountry(): String {
            val telephonyManager = appContext.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val currentCode = telephonyManager.networkCountryIso
            Log.d("Country code", currentCode)
            Country.values().forEach {
                if (it.value == currentCode) return currentCode
            }
            return "us"
        }

        override fun getCurrentCountryCode(): Country {
            val telephonyManager = appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val currentCode = telephonyManager.networkCountryIso
            Log.d("Country code", currentCode)
            Country.values().forEach {
                if (it.value == currentCode) return it
            }
            return Country.US
        }
    }
}