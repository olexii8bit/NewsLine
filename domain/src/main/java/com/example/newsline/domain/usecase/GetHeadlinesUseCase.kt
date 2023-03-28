package com.example.newsline.domain.usecase

import com.example.newsline.domain.exceptionHandler.HandleError
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.repository.ArticleRepository

class GetHeadlinesUseCase(
    private val articleRepository: ArticleRepository,
    private val handleError: HandleError = HandleError.DomainError(),
    private var countryCode: String = "us"
) {
    private var pageNumber = 0

    fun reset() { pageNumber = 0 }

    fun newCountry(countryCode: String) {
        this.countryCode = countryCode
        reset()
    }

    suspend fun execute(): List<Article> {
        return try { articleRepository.get(++pageNumber, countryCode) }
        catch (e: Exception) {
            --pageNumber
            handleError.handle(e)
            return emptyList()
        }
    }
}