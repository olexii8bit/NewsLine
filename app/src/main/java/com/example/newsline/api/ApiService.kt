package api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/everything?apiKey=" + RetrofitInstance.apiKey)
    fun getAllNews(@Query("q") q: String,
                   @Query("searchIn") searchIn: String = "title",
                   @Query("sources") sources: String = "",
                   @Query("domains") domains: String = "",
                   @Query("from") from: String = "",
                   @Query("to") to: String = "",
                   @Query("language") language: String = "",
                   @Query("sortBy") sortBy: String = "publishedAt",
                   @Query("pageSize") pageSize: Int = 10,
                   @Query("page") page: Int = 1): Call<ResponseDTO>?

    @GET("v2/top-headlines?apiKey=5ff64f827b2f40778e1cdf7e6879f6dc")
    fun getHeadlines(@Query("country") country: String,
                     @Query("category") category: String = "",
                     @Query("sources") sources: String = "",
                     @Query("q") q: String = "",
                     @Query("pageSize") pageSize: Int = 10,
                     @Query("page") page: Int = 1): Call<ResponseDTO>?
}
//language=ru&q=qt&