package com.example.newsline.domain.usecase

import com.example.newsline.domain.Filters
import com.example.newsline.domain.repository.NewsRepository


interface SetFiltersUseCase {

    fun execute(filters: Filters)

    class Base(
        private val newsRepository: NewsRepository,
    ): SetFiltersUseCase {
        override fun execute(filters: Filters) {
            newsRepository.setFilters(filters)
        }
    }
}