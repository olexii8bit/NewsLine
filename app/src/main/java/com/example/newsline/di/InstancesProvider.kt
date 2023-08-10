package com.example.newsline.di

import com.example.newsline.data.remoteDatasource.ApiService
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.LocationService
import com.example.newsline.domain.repository.NewsRepository
import com.example.newsline.domain.usecase.GetCachedNewsUseCase
import com.example.newsline.domain.usecase.LoadNewsToCacheUseCase
import com.example.newsline.domain.usecase.SetFiltersUseCase
import com.example.newsline.presentation.ManageMessage

interface InstancesProvider : ErrorHandlerInstancesProvider, ArticleInstancesProvider, ServiceInstancesProvider

interface ErrorHandlerInstancesProvider {
    fun providePresentationErrorHandler(): HandleError
    fun provideDataErrorHandler(): HandleError
}

interface ArticleInstancesProvider {
    fun provideNewsRepository(): NewsRepository
    fun provideLoadNewsToCacheUseCase(): LoadNewsToCacheUseCase
    fun provideGetCachedNewsUseCase(): GetCachedNewsUseCase
    fun provideSetFiltersUseCase(): SetFiltersUseCase
    fun provideApiService(): ApiService
}

interface ServiceInstancesProvider {
    fun provideToastMessageManager(): ManageMessage
    fun provideLocationService(): LocationService
}