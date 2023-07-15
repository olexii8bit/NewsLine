package com.example.newsline.domain.usecase

import com.example.newsline.domain.Filters
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.NoMoreResultsException
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.repository.NewsRepository


interface GetNewsUseCase {

    fun getCache(): List<Article>
    fun setFilters(filters: Filters)
    suspend fun fetchData()

    class GetNewsUseCaseImpl(
        private val newsRepository: NewsRepository,
        private val handleError: HandleError = HandleError.DomainError(),
    ): GetNewsUseCase {
        private val cache = mutableListOf<Article>()

        private var pageNumber = 0

        private var filters: Filters = Filters("", "us", "")

        override fun getCache(): List<Article> = cache
        override fun setFilters(filters: Filters) {
            this.filters = filters
            cache.clear()
            pageNumber = 0
        }
        override suspend fun fetchData() {
            try {
                newsRepository.getArticles(
                    ++pageNumber,
                    keyWords = filters.keyWords,
                    countryCode = filters.countryCode,
                    category = filters.category
                ).let { resultData: List<Article> ->
                    if (resultData.isEmpty()) throw NoMoreResultsException()
                    else cache.addAll(resultData)
                }
            } catch (e: Exception) {
                --pageNumber
                handleError.handle(e)
            }
        }
    }
}

