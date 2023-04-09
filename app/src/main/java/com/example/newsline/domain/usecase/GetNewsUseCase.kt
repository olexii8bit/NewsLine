package com.example.newsline.domain.usecase

import com.example.newsline.domain.repository.NewsRepository
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.LocationService
import com.example.newsline.domain.NoMoreResultsException

class GetNewsUseCase(
    private val newsRepository: NewsRepository,
    private val handleError: HandleError = HandleError.DomainError(),
    private val locationService: LocationService
) {
    private val data = mutableListOf<Article>()

    private var pageNumber = 0
    private val countryCode: String = locationService.getCurrentLocationCountry()

    fun getData(): List<Article> = data

    suspend fun tryToFetchData() {
        try { newsRepository.get(++pageNumber, countryCode).let{
                if (it.isEmpty()) throw NoMoreResultsException()
                else data.addAll(it)
            }
        } catch (e: Exception) {
            --pageNumber
            handleError.handle(e)
        }
    }
}