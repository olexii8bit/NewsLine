package com.example.newsline.domain.usecase

import android.util.Log
import android.widget.Toast
import api.RetrofitInstance
import com.example.newsline.domain.Application
import com.example.newsline.PAGE_SIZE
import com.example.newsline.domain.Location
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.models.ResponseDTO
import com.example.newsline.domain.repository.RemoteArticleRepository

class GetHeadlinesUseCase(
    private val remoteArticleRepository: RemoteArticleRepository,
) {

    suspend fun execute(): List<Article> {
        return remoteArticleRepository.getArticles()
    }

}