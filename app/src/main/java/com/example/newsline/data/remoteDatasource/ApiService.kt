package com.example.newsline.data.remoteDatasource

import com.example.newsline.domain.models.ResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "5ff64f827b2f40778e1cdf7e6879f6dc"
const val PAGE_SIZE = 10
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