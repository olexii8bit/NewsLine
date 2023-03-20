package com.example.newsline.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    init {
        Log.d("AAA", "VM created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AAA", "VM cleared")
    }


}