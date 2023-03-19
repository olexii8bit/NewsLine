package com.example.newsline.domain.repository

import com.example.newsline.domain.models.Article

interface RemoteArticleRepository {

    suspend fun getArticles(): List<Article>

    suspend fun getArticlesFiltered(keyWords: String,
                                    countryCode: String,
                                    category: String): List<Article>

}