package com.example.newsline.presentation.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsline.domain.usecase.GetHeadlinesUseCase
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.models.Article
import com.example.newsline.presentation.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val getHeadlinesUseCase: GetHeadlinesUseCase =
        (app as App).instanceProvider.provideGetHeadlinesUseCase()
    private val handleError: HandleError =
        (app as App).instanceProvider.providePresentationErrorHandler()

    val headlinesLiveData = MutableLiveData(mutableListOf<Article>())

    init {
        loadMoreHeadlines()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AAA", "VM cleared")
    }

    fun loadData() = headlinesLiveData.postValue(getHeadlinesUseCase.getData() as MutableList<Article>)

    fun loadMoreHeadlines() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getHeadlinesUseCase.tryToFetchData()
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) { handleError.handle(e) }
                return@launch
            }
            Log.d("AAA", "no Exception")
            loadData()
        }
    }
}