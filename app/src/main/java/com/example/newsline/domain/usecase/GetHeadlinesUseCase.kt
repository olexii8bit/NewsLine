package com.example.newsline.domain.usecase

import com.example.newsline.domain.models.Article
import com.example.newsline.domain.repository.RemoteArticleRepository

class GetHeadlinesUseCase(
    private val remoteArticleRepository: RemoteArticleRepository,
) {
    private var pageNumber = 0
    suspend fun execute(): List<Article> {
        return remoteArticleRepository.getArticles(++pageNumber)
    }

}