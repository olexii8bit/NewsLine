package com.example.newsline.domain.repository

import com.example.newsline.domain.models.Article

interface RemoteArticleRepository {

    suspend fun get(pageNumber: Int,
                    countryCode: String = "us"): List<Article>

    /*suspend fun getArticlesFiltered(pageNumber: Int,
                                    keyWords: String,
                                    countryCode: String,
                                    category: String): List<Article>*/

}