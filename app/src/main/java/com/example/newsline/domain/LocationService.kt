package com.example.newsline.domain

import com.example.newsline.data.remoteDatasource.enums.Country

interface LocationService {

    fun getCurrentLocationCountry(): String
    fun getCurrentLocationCountryCode(): Country


}