package com.example.newsline.domain.usecase

import com.example.newsline.domain.HandleError
import com.example.newsline.domain.LocationService
import com.example.newsline.domain.NoMoreResultsException
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.repository.ArticleRepository

class GetFilteredHeadlinesUseCase(
    private val articleRepository: ArticleRepository,
    private val handleError: HandleError = HandleError.DomainError()
) {
    private val data = mutableListOf<Article>()

    private var keyWords: String = ""
    private var countryCode: String = ""
    private var category: String = ""

    private var pageNumber = 0

    fun getData(): List<Article> = data

    fun setFilters(keyWords: String,
                   countryCode: String,
                   category: String) {
        this.keyWords = keyWords
        this.countryCode = countryCode
        this.category = category
        pageNumber = 0
        data.clear()
    }

    suspend fun tryToFetchData() {
        try {
            articleRepository.getFiltered(
                ++pageNumber,
                keyWords = keyWords,
                countryCode = countryCode,
                category = category
            ).let {
                if (it.isEmpty()) throw NoMoreResultsException()
                else data.addAll(it)
            }
        } catch (e: Exception) {
            --pageNumber
            handleError.handle(e)
        }
    }
}