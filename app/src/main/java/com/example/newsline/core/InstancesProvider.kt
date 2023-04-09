package com.example.newsline.core

import com.example.newsline.data.newsApi.ApiService
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.repository.NewsRepository
import com.example.newsline.domain.usecase.GetNewsUseCase
import com.example.newsline.domain.LocationService
import com.example.newsline.domain.usecase.GetFilteredNewsUseCase
import com.example.newsline.presentation.ManageMessage

interface InstancesProvider: ErrorHandlerInstancesProvider, ArticleInstancesProvider {
    fun provideToastMessageManager(): ManageMessage
    fun provideLocationService(): LocationService
}

interface ErrorHandlerInstancesProvider {
    fun providePresentationErrorHandler(): HandleError
    fun provideDomainErrorHandler(): HandleError
    fun provideDataErrorHandler(): HandleError
}

interface ArticleInstancesProvider {
    fun provideArticleRepository(): NewsRepository
    fun provideGetHeadlinesUseCase(): GetNewsUseCase
    fun provideGetFilteredHeadlinesUseCase(): GetFilteredNewsUseCase
    fun provideApiService(): ApiService
}