package com.example.newsline.domain.repository

import com.example.newsline.domain.Filters
import com.example.newsline.domain.models.Article

interface NewsRepository {
    fun getCache(): List<Article>
    suspend fun loadMoreNews()
    fun setFilters(filters: Filters)
}