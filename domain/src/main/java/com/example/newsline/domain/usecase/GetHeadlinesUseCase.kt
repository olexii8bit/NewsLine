package com.example.newsline.domain.usecase

import com.example.newsline.domain.exceptionHandler.HandleError
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.repository.RemoteArticleRepository

class GetHeadlinesUseCase(
    private val remoteArticleRepository: RemoteArticleRepository,
    private val handleError: HandleError = HandleError.DomainError(),
    private val countryCode: String = "us"
) {
    private var pageNumber = 0

    fun newCountry(countryCode: String) =
        GetHeadlinesUseCase(
            this.remoteArticleRepository,
            this.handleError,
            countryCode
    )

    suspend fun execute(): List<Article> {
        return try { remoteArticleRepository.getArticles(++pageNumber, countryCode) }
        catch (e: Exception) {
            --pageNumber
            handleError.handle(e)
            return emptyList()
        }
    }

}