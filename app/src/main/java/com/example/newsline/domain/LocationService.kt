package com.example.newsline.domain

import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.telephony.TelephonyManager
import com.example.newsline.data.newsApi.enums.Country

interface LocationService {

    fun getCurrentLocationCountry(): String
    fun getCurrentLocationCountryCode(): Country

    class Base(private val currentContext: Context): LocationService {
        override fun getCurrentLocationCountry(): String {
            val telephonyManager = currentContext.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val currentCode = telephonyManager.networkCountryIso
            Country.values().forEach {
                if (it.value == currentCode) return currentCode
            }
            return "us"
        }

        override fun getCurrentLocationCountryCode(): Country {
            val telephonyManager = currentContext.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val currentCode = telephonyManager.networkCountryIso
            Country.values().forEach {
                if (it.value == currentCode) return it
            }
            return Country.US
        }
    }
}