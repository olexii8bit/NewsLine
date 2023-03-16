package com.example.newsline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var pagesLoaded = 0
    private lateinit var recyclerView: RecyclerView;
    private val data = mutableListOf<ResponseDTO.Companion.Article>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_news)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerAdapter(data, this@MainActivity)

        val btn: Button = findViewById(R.id.button)
        getImages()
        btn.setOnClickListener {
            val getAllNews = RetrofitInstance.service.getAllNews(q = "Росія", sortBy = "publishedAt")
            getImages()
        }
    }

    fun getImages() {
        val getHeadlines = RetrofitInstance.service.getHeadlines("us", pageSize = 10, page = ++pagesLoaded)
        getHeadlines?.enqueue(object: Callback<ResponseDTO> {

            override fun onResponse(call: Call<ResponseDTO>, response: Response<ResponseDTO>) {

                if (!response.body()?.articles.isNullOrEmpty() ) {

                    Log.d("request-response", (response.body()?.articles.isNullOrEmpty().toString() ?: "null") + " ${pagesLoaded}")

                    for (article in response.body()?.articles!!) {
                        data.add(article)
                        recyclerView.adapter?.notifyItemInserted(data.size - 1)
                    }

                } else { Log.d("request-response", "articles.isNullOrEmpty() = true") }

            }

            override fun onFailure(call: Call<ResponseDTO>, t: Throwable) {
                Log.d("request-failure", t.message.toString())
            }

        })
    }
}