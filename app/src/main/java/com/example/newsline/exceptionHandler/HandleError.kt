package com.example.newsline.exceptionHandler

import java.net.UnknownHostException


interface HandleError {
    fun handle(exception: Exception)

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