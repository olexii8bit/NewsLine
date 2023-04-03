package com.example.newsline.domain.usecase

import com.example.newsline.domain.repository.ArticleRepository
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.LocationService
import com.example.newsline.domain.NoMoreResultsException

class GetHeadlinesUseCase(
    private val articleRepository: ArticleRepository,
    private val handleError: HandleError = HandleError.DomainError(),
    private val locationService: LocationService
) {
    private val data = mutableListOf<Article>()

    private var pageNumber = 0
    private val countryCode: String = locationService.getCurrentLocationCountry()

    fun getData(): List<Article> = data

    suspend fun tryToFetchData() {
        try { articleRepository.get(++pageNumber, countryCode).let{
                if (it.isEmpty()) throw NoMoreResultsException()
                else data.addAll(it)
            }
        } catch (e: Exception) {
            --pageNumber
            handleError.handle(e)
        }
    }
}