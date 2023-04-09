package com.example.newsline.core

import com.example.newsline.data.newsApi.ApiService
import com.example.newsline.data.newsApi.RetrofitInstance
import com.example.newsline.data.repository.NewsRepositoryImpl
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.repository.NewsRepository
import com.example.newsline.domain.usecase.GetFilteredNewsUseCase
import com.example.newsline.domain.usecase.GetNewsUseCase

abstract class AbstractInstancesProvider: InstancesProvider {

    override fun providePresentationErrorHandler(): HandleError =
        HandleError.PresentationError(provideToastMessageManager())

    override fun provideDomainErrorHandler(): HandleError =
        HandleError.DomainError()

    override fun provideDataErrorHandler(): HandleError =
        HandleError.DataError()

    override fun provideArticleRepository(): NewsRepository =
        NewsRepositoryImpl(
            provideDataErrorHandler(),
            provideApiService()
        )

    override fun provideGetHeadlinesUseCase(): GetNewsUseCase =
        GetNewsUseCase(
            provideArticleRepository(),
            provideDomainErrorHandler(),
            provideLocationService()
        )

    override fun provideGetFilteredHeadlinesUseCase(): GetFilteredNewsUseCase =
        GetFilteredNewsUseCase(
            provideArticleRepository(),
            provideDomainErrorHandler(),
        )

    override fun provideApiService(): ApiService =
        RetrofitInstance.service
}