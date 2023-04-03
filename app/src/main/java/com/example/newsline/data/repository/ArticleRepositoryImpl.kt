package com.example.newsline.data.repository

import com.example.newsline.data.newsApi.RetrofitInstance
import com.example.newsline.data.newsApi.ApiService
import com.example.newsline.domain.repository.ArticleRepository
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.models.Article

class ArticleRepositoryImpl(
    private val handleError: HandleError = HandleError.DataError(),
    private val apiService: ApiService = RetrofitInstance.service,
) : ArticleRepository {

    override suspend fun get(
        pageNumber: Int,
        countryCode: String,
    ): List<Article> {
        return try {
            apiService.getHeadlines(
                country = countryCode,
                page = pageNumber
            ).articles.toList()
        } catch (e: Exception) {
            handleError.handle(e)
            return emptyList()
        }
    }

    override suspend fun getFiltered(
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
            handleError.handle(e)
            return emptyList()
        }
    }
}