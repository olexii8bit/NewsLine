package com.example.newsline.domain.usecase

import com.example.newsline.domain.repository.NewsRepository


interface LoadNewsToCacheUseCase {

    suspend fun execute()

    class Base(
        private val newsRepository: NewsRepository,
    ): LoadNewsToCacheUseCase {
        override suspend fun execute() = newsRepository.loadMoreNews()
    }
}

