package com.example.newsline.domain.repository

import com.example.newsline.domain.models.Article

interface ArticleRepository {

    suspend fun get(pageNumber: Int,
                    countryCode: String = "us"): List<Article>

    suspend fun getFiltered(pageNumber: Int,
                             keyWords: String,
                             countryCode: String,
                             category: String): List<Article>

}