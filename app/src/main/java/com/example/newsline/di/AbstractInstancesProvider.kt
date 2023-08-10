package com.example.newsline.di

import android.content.Context
import com.example.newsline.data.remoteDatasource.ApiService
import com.example.newsline.data.remoteDatasource.RetrofitInstance
import com.example.newsline.data.repository.NewsRepositoryImpl
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.LocationService
import com.example.newsline.domain.repository.NewsRepository
import com.example.newsline.domain.usecase.GetCachedNewsUseCase
import com.example.newsline.domain.usecase.LoadNewsToCacheUseCase
import com.example.newsline.domain.usecase.SetFiltersUseCase
import com.example.newsline.presentation.LocationServiceImpl
import com.example.newsline.presentation.ManageMessage

abstract class AbstractInstancesProvider: InstancesProvider {

    abstract val context: Context

    private val newsRepository by lazy {
        NewsRepositoryImpl(
            provideDataErrorHandler(),
            provideApiService()
        )
    }

    override fun providePresentationErrorHandler(): HandleError =
    HandleError.PresentationError(provideToastMessageManager())

    override fun provideDataErrorHandler(): HandleError =
        HandleError.DataError()

    override fun provideNewsRepository(): NewsRepository = newsRepository

    override fun provideLoadNewsToCacheUseCase(): LoadNewsToCacheUseCase =
        LoadNewsToCacheUseCase.Base(provideNewsRepository())

    override fun provideGetCachedNewsUseCase(): GetCachedNewsUseCase =
        GetCachedNewsUseCase.Base(provideNewsRepository())

    override fun provideSetFiltersUseCase(): SetFiltersUseCase =
        SetFiltersUseCase.Base(provideNewsRepository())

    override fun provideApiService(): ApiService =
        RetrofitInstance.service

    override fun provideToastMessageManager(): ManageMessage =
        ManageMessage.ManageToastMessage(context)

    override fun provideLocationService(): LocationService =
        LocationServiceImpl(context)
}