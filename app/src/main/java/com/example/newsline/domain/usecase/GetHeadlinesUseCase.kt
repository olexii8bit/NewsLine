package com.example.newsline.domain.usecase

import com.example.newsline.domain.repository.ArticleRepository
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.LocationService

class GetHeadlinesUseCase(
    private val articleRepository: ArticleRepository,
    private val handleError: HandleError = HandleError.DomainError(),
    private val locationService: LocationService
) {
    private var pageNumber = 0
    private val countryCode: String = locationService.getCurrentLocationCountry()

    fun reset() { pageNumber = 0 }

    suspend fun execute(): List<Article> {
        return try { articleRepository.get(++pageNumber, countryCode) }
        catch (e: Exception) {
            --pageNumber
            handleError.handle(e)
            return emptyList()
        }
    }
}