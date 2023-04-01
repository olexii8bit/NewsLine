package com.example.newsline.presentation.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.newsline.domain.usecase.GetHeadlinesUseCase
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.models.Article
import com.example.newsline.presentation.App
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val getHeadlinesUseCase: GetHeadlinesUseCase =
        (app as App).instanceProvider.provideGetHeadlinesUseCase()
    private val handleError: HandleError =
        (app as App).instanceProvider.providePresentationErrorHandler()

    val headlinesLive = MutableLiveData(mutableListOf<Article>())

    init {
        MainScope().launch { loadMoreHeadlines() }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AAA", "VM cleared")
    }

    suspend fun loadMoreHeadlines() {
        try {
            getHeadlinesUseCase.execute().let { newHeadlines ->
                headlinesLive.value!!.addAll(newHeadlines)
                headlinesLive.value = headlinesLive.value
                Log.d("AAA", "load LiveData size : " + headlinesLive.value!!.size + "\n" +
                        "has active observer : " + headlinesLive.hasActiveObservers())
            }
        } catch (e: Exception) {
            handleError.handle(e)
        }
    }
}