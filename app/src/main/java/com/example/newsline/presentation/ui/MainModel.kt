package com.example.newsline.presentation.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsline.domain.Filters
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.LocationService
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.usecase.GetNewsUseCase
import com.example.newsline.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface MainModelCommunication {
    fun setFilters(
        keyWords: String,
        countryCode: String,
        category: String,
    )
    fun loadCachedArticles()
    fun loadArticles()
}
class MainModel(app: Application) : AndroidViewModel(app), MainModelCommunication {

    private val getNewsUseCase: GetNewsUseCase =
        (app as App).instanceProvider.provideGetNewsUseCase()
    private val handleError: HandleError =
        (app as App).instanceProvider.providePresentationErrorHandler()
    private val locationService: LocationService =
        (app as App).instanceProvider.provideLocationService()

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    private val _filters = MutableLiveData<Filters>()
    val filters: LiveData<Filters> get() = _filters

    private val _isFetchingNews = MutableLiveData<Boolean>()
    val isFetchingNews: LiveData<Boolean> get() = _isFetchingNews

    init {
        _isFetchingNews.value = false
        setFilters("", locationService.getCurrentLocationCountry(), "")
        loadArticles()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AAA", "VM cleared")
    }

    override fun setFilters(
        keyWords: String,
        countryCode: String,
        category: String,
    ) {
        val value = Filters(keyWords, countryCode, category)
        getNewsUseCase.setFilters(value)
        _filters.value = value
        _articles.value = emptyList()
    }

    override fun loadCachedArticles() {
        _articles.value = getNewsUseCase.getCache()
    }

    override fun loadArticles() {
        _isFetchingNews.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getNewsUseCase.fetchData()
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    handleError.handle(e)
                    _isFetchingNews.postValue(false)
                }
                return@launch
            }
            _articles.postValue(getNewsUseCase.getCache())
            _isFetchingNews.postValue(false)
        }

    }
}