package com.example.newsline.domain.repository

import com.example.newsline.domain.models.Article

interface NewsRepository {
    suspend fun getArticles(pageNumber: Int,
                             keyWords: String,
                             countryCode: String,
                             category: String): List<Article>

}