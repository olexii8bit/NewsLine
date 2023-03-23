package com.example.newsline.domain.usecase

import com.example.newsline.domain.models.Article
import com.example.newsline.domain.repository.RemoteArticleRepository
import com.example.newsline.exceptionHandler.HandleError

class GetHeadlinesUseCase(
    private val remoteArticleRepository: RemoteArticleRepository,
    private val handleError: HandleError = HandleError.DomainError()
) {
    private var pageNumber = 0
    suspend fun execute(): List<Article> {
        return try { remoteArticleRepository.getArticles(++pageNumber) }
        catch (e: Exception) {
            --pageNumber
            handleError.handle(e)
            return emptyList()
        }
    }

}