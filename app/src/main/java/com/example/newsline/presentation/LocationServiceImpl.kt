package com.example.newsline.presentation

import android.content.Context
import android.telephony.TelephonyManager
import com.example.newsline.data.remoteDatasource.enums.Country
import com.example.newsline.domain.LocationService

class LocationServiceImpl(private val currentContext: Context): LocationService {
    override fun getCurrentLocationCountry(): String {
        val telephonyManager = currentContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val currentCode = telephonyManager.networkCountryIso
        Country.values().forEach {
            if (it.value == currentCode) return currentCode
        }
        return "us"
    }

    override fun getCurrentLocationCountryCode(): Country {
        val telephonyManager = currentContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val currentCode = telephonyManager.networkCountryIso
        Country.values().forEach {
            if (it.value == currentCode) return it
        }
        return Country.US
    }
}