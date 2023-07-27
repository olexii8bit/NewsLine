package com.example.newsline.domain

data class Filters(
    val keyWords: String,
    val countryCode: String,
    val category: String
)
