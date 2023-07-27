package com.example.newsline.data.repository

import android.util.Log
import com.example.newsline.data.remoteDatasource.RetrofitInstance
import com.example.newsline.data.remoteDatasource.ApiService
import com.example.newsline.domain.repository.NewsRepository
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.models.Article

class NewsRepositoryImpl(
    private val handleError: HandleError = HandleError.DataError(),
    private val apiService: ApiService = RetrofitInstance.service,
) : NewsRepository {
    override suspend fun getArticles(
        pageNumber: Int,
        keyWords: String,
        countryCode: String,
        category: String,
    ): List<Article> {
        return try {
            apiService.getHeadlines(
                q = keyWords,
                country = countryCode,
                category = category,
                page = pageNumber
            ).articles.toList()
        } catch (e: Exception) {
            Log.d("ddd", e.message.toString())
            handleError.handle(e)
            return emptyList()
        }
    }
}