package com.example.newsline.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsline.data.newsApi.ApiService
import com.example.newsline.data.newsApi.RetrofitInstance
import com.example.newsline.data.repository.RemoteArticleRepositoryImpl
import com.example.newsline.domain.exceptionHandler.HandleError
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.usecase.GetHeadlinesUseCase

class MainViewModelFactory(): ViewModelProvider.Factory {

    private val remoteArticleRepository =
        RemoteArticleRepositoryImpl(
            handleError = HandleError.DataError(),
            apiService = RetrofitInstance.service
        )
    private val getHeadlinesUseCase =
        GetHeadlinesUseCase(
            remoteArticleRepository = remoteArticleRepository,
            handleError = HandleError.DomainError(),
            countryCode = "us"
        )
    //private val getFilteredHeadlinesUseCase = GetFilteredHeadlinesUseCase(remoteArticleRepository)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            getHeadlinesUseCase
        ) as T
    }
}