package com.example.newsline.presentation.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsline.domain.usecase.GetNewsUseCase
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.usecase.GetFilteredNewsUseCase
import com.example.newsline.presentation.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val getNewsUseCase: GetNewsUseCase =
        (app as App).instanceProvider.provideGetHeadlinesUseCase()
    private val getFilteredNewsUseCase: GetFilteredNewsUseCase =
        (app as App).instanceProvider.provideGetFilteredHeadlinesUseCase()
    private val handleError: HandleError =
        (app as App).instanceProvider.providePresentationErrorHandler()

    val newsLiveData = MutableLiveData(mutableListOf<Article>())

    private var filteredFinding = false

    init {
        loadMoreHeadlines()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AAA", "VM cleared")
    }

    fun setFilteredFinding(isFiltered: Boolean) {
        filteredFinding = isFiltered
    }

    fun setFilters(keyWords: String,
                   countryCode: String,
                   category: String) =
        getFilteredNewsUseCase.setFilters(keyWords, countryCode, category)

    fun loadData() = if (filteredFinding) {
        newsLiveData.postValue(getFilteredNewsUseCase.getData() as MutableList<Article>)
    } else {
        newsLiveData.postValue(getNewsUseCase.getData() as MutableList<Article>)
    }

    fun loadMoreHeadlines() {
        if (filteredFinding) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    getFilteredNewsUseCase.tryToFetchData()
                } catch (e: Exception) {
                    viewModelScope.launch(Dispatchers.Main) { handleError.handle(e) }
                    return@launch
                }
                Log.d("AAA", "no Exception | filtered")
                loadData()
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    getNewsUseCase.tryToFetchData()
                } catch (e: Exception) {
                    viewModelScope.launch(Dispatchers.Main) { handleError.handle(e) }
                    return@launch
                }
                loadData()
            }
        }
    }
}