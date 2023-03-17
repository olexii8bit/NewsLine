package com.example.newsline

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.RetrofitInstance
import com.example.newsline.ResponseDTO.Companion.Article
import com.example.newsline.api.enums.Category
import com.example.newsline.api.enums.Country
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

const val PAGE_SIZE = 10

class MainActivity : AppCompatActivity() {
    var pagesLoaded = 0
    private lateinit var recyclerView: RecyclerView

    private lateinit var keywordsEditText: EditText
    private lateinit var countrySpinner: Spinner
    private lateinit var categorySpinner: Spinner

    private lateinit var filterLinearLayout: LinearLayout
    private lateinit var filterRelativeLayout: RelativeLayout
    private lateinit var filterImageButton: ImageButton
    private lateinit var findButton: ImageButton

    private lateinit var findMoreButton: Button

    private var currentQuery: Call<ResponseDTO>? = null

    private val data = mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*data.add(Article(ResponseDTO.Companion.Source("", ""),
            "",
            "",
            "",
            "https://context.reverso.net/%D0%BF%D0%B5%D1%80%D0%B5%D0%B2%D0%BE%D0%B4/%D0%B0%D0%BD%D0%B3%D0%BB%D0%B8%D0%B9%D1%81%D0%BA%D0%B8%D0%B9-%D1%80%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9/headlines",
            "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.imgworlds.com%2Flanguage%2Fen%2Fcontact-us%2F&psig=AOvVaw0cdJbEeEatkE0-aYW-Y9tP&ust=1679074868672000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCIDr77r_4P0CFQAAAAAdAAAAABAE",
            "",
            ""))*/

        recyclerView = findViewById(R.id.rv_news)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerAdapter(data, this@MainActivity)


        keywordsEditText = findViewById(R.id.keywordsEditText)

        countrySpinner = findViewById(R.id.countrySpinner)
        countrySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Country.values())
        val countrySpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Country.values())
        countrySpinner.setSelection(countrySpinnerAdapter.getPosition(getCurrentCountryCode()))

        categorySpinner = findViewById(R.id.categorySpinner)
        categorySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Category.values())

        filterLinearLayout = findViewById(R.id.filterLinearLayout)

        filterRelativeLayout = findViewById(R.id.filterRelativeLayout)
        filterRelativeLayout.visibility = GONE

        filterImageButton = findViewById(R.id.filterImageButton)
        filterImageButton.setOnClickListener {
            if (filterRelativeLayout.visibility == GONE) {
                filterRelativeLayout.visibility = VISIBLE
            } else {
                filterRelativeLayout.visibility = GONE
                countrySpinner.setSelection(countrySpinnerAdapter.getPosition(getCurrentCountryCode()))
            }
        }

        findButton = findViewById(R.id.findImageButton)
        findButton.setOnClickListener {
            findMoreButton.visibility = VISIBLE
            getNews()
        }

        findMoreButton = findViewById(R.id.button)
        findMoreButton.setOnClickListener {
            getNews()
        }
        getNews()
    }

    private fun getNews() {
        var keyWords = ""
        var countryCode = getCurrentCountryCodeString()
        var category = ""

        if (filterRelativeLayout.visibility == VISIBLE) {
            keyWords = keywordsEditText.text.toString()
            countryCode = countrySpinner.selectedItem.toString().lowercase(Locale.getDefault())
            category = categorySpinner.selectedItem.toString().lowercase(Locale.getDefault())
        }

        currentQuery = RetrofitInstance.service.getHeadlines(
            country = countryCode,
            category = category,
            pageSize = PAGE_SIZE,
            q = keyWords,
            page = ++pagesLoaded)

        currentQuery?.enqueue(object : Callback<ResponseDTO> {
            override fun onResponse(call: Call<ResponseDTO>, response: Response<ResponseDTO>) {
                if (!response.body()?.articles.isNullOrEmpty()) {
                    if (response.body()?.totalResults!! <= 0) TODO("change LadeMoreButton visibility when totalResults == 0")
                    Log.d("request-response", (response.body()?.articles.isNullOrEmpty().toString()) + " $pagesLoaded")
                    for (article in response.body()?.articles!!) {
                        data.add(article)
                        recyclerView.adapter?.notifyItemInserted(data.size - 1)
                    }
                } else {
                    Toast.makeText(this@MainActivity, response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            override fun onFailure(call: Call<ResponseDTO>, t: Throwable) {
                Log.d("request-failure", t.message.toString())
            }
        })
    }

    private fun getCurrentCountryCode(): Country {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val currentCode = telephonyManager.networkCountryIso
        Log.d("Country code", currentCode)
        Country.values().forEach {
            if (it.value == currentCode) return it
        }
        return Country.US
    }

    private fun getCurrentCountryCodeString(): String {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val currentCode = telephonyManager.networkCountryIso
        Log.d("Country code", currentCode)
        Country.values().forEach {
            if (it.value == currentCode) return currentCode
        }
        return "us"
    }
}