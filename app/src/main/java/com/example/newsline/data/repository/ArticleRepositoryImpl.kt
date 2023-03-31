package com.example.newsline.data.repository

import com.example.newsline.data.newsApi.RetrofitInstance
import com.example.newsline.data.newsApi.ApiService
import com.example.newsline.domain.repository.ArticleRepository
import com.example.newsline.domain.HandleError
import com.example.newsline.domain.models.Article

class ArticleRepositoryImpl(
    private val handleError: HandleError = HandleError.DataError(),
    private val apiService: ApiService = RetrofitInstance.service) : ArticleRepository {

    override suspend fun get(pageNumber: Int,
                             countryCode: String): List<Article> {
        return try {
            apiService.getHeadlines(
                country = countryCode,
                page = pageNumber
            ).articles.toList()
        } catch (e: Exception) {
            handleError.handle(e)
            return emptyList()
        }
    }



    /*override suspend fun getArticlesFiltered(pageNumber: Int,
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
    }*/
}