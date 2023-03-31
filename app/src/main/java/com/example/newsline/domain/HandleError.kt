package com.example.newsline.domain

import com.example.newsline.presentation.ManageMessage
import java.net.UnknownHostException


interface HandleError {
    fun handle(exception: Exception)

    class PresentationError(private val manageMessage: ManageMessage): HandleError {
        override fun handle(exception: Exception) {
            //Log.d("Data exception type : ", exception::class.java.simpleName)
            when (exception) {
                is NoInternetConnectionException -> manageMessage.show("No internet connection", 3)
                is ServiceUnavailableException -> manageMessage.show("News service unavailable", 3)
                else -> manageMessage.show("Something went wrong", 3)
            }
        }
    }

    class DomainError: HandleError {
        override fun handle(exception: Exception) {
            //Log.d("Domain exception type : ", exception::class.java.simpleName)
            throw exception
        }
    }

    class DataError: HandleError {
        override fun handle(exception: Exception) {
            //Log.d("Data exception type : ", exception::class.java.simpleName)
            when (exception) {
                is UnknownHostException -> throw NoInternetConnectionException()
                else -> throw ServiceUnavailableException()
            }
        }
    }

}