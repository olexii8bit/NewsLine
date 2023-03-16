package api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    const val apiKey = "5ff64f827b2f40778e1cdf7e6879f6dc"
    var service: ApiService = retrofit.create(ApiService::class.java)
}