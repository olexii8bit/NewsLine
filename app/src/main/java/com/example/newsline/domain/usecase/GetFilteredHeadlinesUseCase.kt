package com.example.newsline.domain.usecase

import com.example.newsline.domain.Location
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.repository.RemoteArticleRepository
import com.example.newsline.presentation.PagesLoaded

class GetFilteredHeadlinesUseCase(
    private val remoteArticleRepository: RemoteArticleRepository,
    private val keyWords: String = "",
    private val countryCode: String = "",
    private val category: String = "",
) {
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
        PagesLoaded.resultsEnded = false

        val countryCode = Location.Base().getCurrentLocationCountry()

        return remoteArticleRepository.getArticlesFiltered(
            keyWords,
            if (countryCode == "") countryCode else this.countryCode,
            category
        )
    }

}