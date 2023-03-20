package com.example.newsline.domain.usecase

import com.example.newsline.domain.Location
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.repository.RemoteArticleRepository

class GetFilteredHeadlinesUseCase(
    private val remoteArticleRepository: RemoteArticleRepository,
    private val keyWords: String = "",
    private val countryCode: String = "",
    private val category: String = "",
) {
    private var pageNumber = 0
    fun updateFilters(
        keyWords: String,
        countryCode: String,
        category: String,
    ): GetFilteredHeadlinesUseCase {
        return GetFilteredHeadlinesUseCase(this.remoteArticleRepository,
            keyWords,
            countryCode,
            category)
    }

    suspend fun execute(): List<Article> {
        val countryCode = Location.Base().getCurrentLocationCountry()

        return remoteArticleRepository.getArticlesFiltered(
            ++pageNumber,
            keyWords,
            if (countryCode == "") countryCode else this.countryCode,
            category
        )
    }

}