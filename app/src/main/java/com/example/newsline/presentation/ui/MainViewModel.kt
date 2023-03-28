package com.example.newsline.presentation.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.usecase.GetHeadlinesUseCase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val getHeadlinesUseCase: GetHeadlinesUseCase,
) : ViewModel() {

    val headlinesLive = MutableLiveData(mutableListOf<Article>())

    init {
        MainScope().launch { loadMoreHeadlines() }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AAA", "VM cleared")
    }

    suspend fun loadMoreHeadlines() {
        getHeadlinesUseCase.execute().let { newHeadlines ->
            headlinesLive.value!!.addAll(newHeadlines)
            headlinesLive.value = headlinesLive.value
            Log.d("AAA", "load LiveData size : " + headlinesLive.value!!.size + "\n" +
                    "has active observer : " + headlinesLive.hasActiveObservers())
        }
    }
}