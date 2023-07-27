package com.example.newsline.di

import com.example.newsline.data.remoteDatasource.ApiService
import com.example.newsline.data.remoteDatasource.RetrofitInstance
import com.example.newsline.data.repository.NewsRepositoryImpl
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.repository.NewsRepository
import com.example.newsline.domain.usecase.GetNewsUseCase
import com.example.newsline.domain.usecase.GetNewsUseCase.GetNewsUseCaseImpl

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

    override fun provideGetNewsUseCase(): GetNewsUseCase =
        GetNewsUseCaseImpl(
            provideArticleRepository(),
            provideDomainErrorHandler()
        )

    override fun provideApiService(): ApiService =
        RetrofitInstance.service
}