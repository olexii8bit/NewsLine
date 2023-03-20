package com.example.newsline.data.repository

import android.util.Log
import android.widget.Toast
import api.RetrofitInstance
import com.example.newsline.PAGE_SIZE
import com.example.newsline.domain.Application
import com.example.newsline.domain.Location
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.models.ResponseDTO
import com.example.newsline.domain.repository.RemoteArticleRepository

class RemoteArticleRepositoryImpl : RemoteArticleRepository {

    override suspend fun getArticles(pageNumber: Int): List<Article> {
        val data = mutableListOf<Article>()
        suspend fun getResult() = RetrofitInstance.service.getHeadlines(
            country = Location.Base().getCurrentLocationCountry(),
            category = "",
            pageSize = PAGE_SIZE,
            q = "",
            page = pageNumber)


        val response: ResponseDTO? = try {
            getResult()
        } catch (e: Exception) {
            Toast.makeText(Application.appContext, e.message, Toast.LENGTH_LONG).show()
            return data
        }

        if (response != null) {
            Log.d("totalResults", response.totalResults.toString())

            if ((PAGE_SIZE * pageNumber) >= response.totalResults) {
                Toast.makeText(Application.appContext, "No more results", Toast.LENGTH_SHORT).show()
                return data
            }

            Log.d("Response", "articles count ${response.articles.size}")

            for (article in response.articles) {
                data.add(article)
            }
        } else {
            Log.d("Response", "error")
            Toast.makeText(Application.appContext, "Request-Response error", Toast.LENGTH_SHORT).show()
        }
        return data
    }

    override suspend fun getArticlesFiltered(pageNumber: Int,
                                             keyWords: String,
                                             countryCode: String,
                                             category: String): List<Article> {
        val data = mutableListOf<Article>()
        suspend fun getResult() = RetrofitInstance.service.getHeadlines(
            country = countryCode,
            category = category,
            pageSize = PAGE_SIZE,
            q = keyWords,
            page = pageNumber)

        val response: ResponseDTO? = try {
            getResult()
        } catch (e: Exception) {
            Toast.makeText(Application.appContext, e.message, Toast.LENGTH_LONG).show()
            return data
        }

        if (response != null) {
            Log.d("totalResults", response.totalResults.toString())

            if ((PAGE_SIZE * pageNumber) >= response.totalResults) {
                Toast.makeText(Application.appContext, "No more results", Toast.LENGTH_SHORT).show()
                return data
            }

            Log.d("Response", "articles count ${response.articles.size}")

            for (article in response.articles) {
                data.add(article)
            }
        } else {
            Log.d("Response", "error")
            Toast.makeText(Application.appContext, "Request-Response error", Toast.LENGTH_SHORT).show()
        }
        return data
    }
}