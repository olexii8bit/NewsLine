package com.example.newsline.data.newsApi

import com.example.newsline.API_KEY
import com.example.newsline.PAGE_SIZE
import com.example.newsline.domain.models.ResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    /*@GET("v2/everything?apiKey=$API_KEY")
    suspend fun getAllNews(@Query("q") q: String,
                   @Query("searchIn") searchIn: String = "title",
                   @Query("sources") sources: String = "",
                   @Query("domains") domains: String = "",
                   @Query("from") from: String = "",
                   @Query("to") to: String = "",
                   @Query("language") language: String = "",
                   @Query("sortBy") sortBy: String = "publishedAt",
                   @Query("pageSize") pageSize: Int = 10,
                   @Query("page") page: Int = 1): ResponseDTO*/

    @GET("v2/top-headlines?apiKey=$API_KEY")
    suspend fun getHeadlines(
        @Query("country") country: String,
        @Query("category") category: String = "",
        @Query("sources") sources: String = "",
        @Query("q") q: String = "",
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("page") page: Int
    ): ResponseDTO

}
//language=ru&q=qt&