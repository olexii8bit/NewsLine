package com.example.newsline.data.repository

import android.util.Log
import com.example.newsline.data.remoteDatasource.ApiService
import com.example.newsline.data.remoteDatasource.RetrofitInstance
import com.example.newsline.domain.Filters
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.NoMoreResultsException
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val handleError: HandleError = HandleError.DataError(),
    private val apiService: ApiService = RetrofitInstance.service,
) : NewsRepository {

    private val cache = mutableListOf<Article>()

    private var pageNumber = 0

    private var filters: Filters = Filters("", "us", "")

    override fun getCache(): List<Article> = cache

    override suspend fun loadMoreNews() {
        try {
            apiService.getHeadlines(
                q = filters.keyWords,
                country = filters.countryCode,
                category = filters.category,
                page = ++pageNumber
            ).articles.toList().let {
                if(it.isEmpty()) throw NoMoreResultsException()
                cache.addAll(it)
            }
        } catch (e: Exception) {
            Log.d("ddd", e.message.toString())
            --pageNumber
            handleError.handle(e)
        }
    }

    override fun setFilters(filters: Filters) {
        this.filters = filters
        cache.clear()
        pageNumber = 0
    }
}