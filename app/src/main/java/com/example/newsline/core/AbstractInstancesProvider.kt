package com.example.newsline.core

import com.example.newsline.data.newsApi.ApiService
import com.example.newsline.data.newsApi.RetrofitInstance
import com.example.newsline.data.repository.ArticleRepositoryImpl
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.repository.ArticleRepository
import com.example.newsline.domain.usecase.GetFilteredHeadlinesUseCase
import com.example.newsline.domain.usecase.GetHeadlinesUseCase

abstract class AbstractInstancesProvider: InstancesProvider {

    override fun providePresentationErrorHandler(): HandleError =
        HandleError.PresentationError(provideToastMessageManager())

    override fun provideDomainErrorHandler(): HandleError =
        HandleError.DomainError()

    override fun provideDataErrorHandler(): HandleError =
        HandleError.DataError()

    override fun provideArticleRepository(): ArticleRepository =
        ArticleRepositoryImpl(
            provideDataErrorHandler(),
            provideApiService()
        )

    override fun provideGetHeadlinesUseCase(): GetHeadlinesUseCase =
        GetHeadlinesUseCase(
            provideArticleRepository(),
            provideDomainErrorHandler(),
            provideLocationService()
        )

    override fun provideGetFilteredHeadlinesUseCase(): GetFilteredHeadlinesUseCase =
        GetFilteredHeadlinesUseCase(
            provideArticleRepository(),
            provideDomainErrorHandler(),
        )

    override fun provideApiService(): ApiService =
        RetrofitInstance.service
}