package com.example.newsline.domain.usecase

import android.util.Log
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.repository.NewsRepository

interface GetCachedNewsUseCase {

    fun execute(): List<Article>

    class Base(
        private val newsRepository: NewsRepository,
    ): GetCachedNewsUseCase {
        override fun execute(): List<Article> = newsRepository.getCache().let {

            Log.d("dddd", "GetCachedNewsUseCase execute " + it)
            return it
        }
    }
}