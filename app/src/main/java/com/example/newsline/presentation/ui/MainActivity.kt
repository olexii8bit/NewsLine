package com.example.newsline.presentation.ui

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsline.LocationService
import com.example.newsline.R
import com.example.newsline.data.repository.RemoteArticleRepositoryImpl
import com.example.newsline.databinding.ActivityMainBinding
import com.example.newsline.domain.models.Article
import com.example.newsline.domain.usecase.GetHeadlinesUseCase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val articleList = mutableListOf<Article>()

    private lateinit var mainViewModel: MainViewModel

    private val remoteArticleRepository = RemoteArticleRepositoryImpl()
    private val getHeadlinesUseCase = GetHeadlinesUseCase(remoteArticleRepository)
    //private val getFilteredHeadlinesUseCase = GetFilteredHeadlinesUseCase(remoteArticleRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = RecyclerAdapter(articleList, this@MainActivity)

        /*binding.countrySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Country.values())
        val countrySpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Country.values())
        binding.countrySpinner.setSelection(countrySpinnerAdapter.getPosition(Location.Base().getCurrentCountryCode()))

        binding.categorySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Category.values())

        binding.filterRelativeLayout.visibility = GONE
        binding.filterImageButton.setOnClickListener {
            if (binding.filterRelativeLayout.visibility == GONE) {
                binding.filterRelativeLayout.visibility = VISIBLE
                binding.filterRelativeLayout.gravity = Gravity.TOP
            } else {
                binding.filterRelativeLayout.visibility = GONE
                binding.countrySpinner.setSelection(countrySpinnerAdapter.getPosition(Location.Base().getCurrentCountryCode()))
            }
        }*/

        /*binding.findImageButton.setOnClickListener {
            binding.findMoreButton.visibility = VISIBLE
            if (listData.isNotEmpty()) {
                binding.recyclerView.adapter?.notifyItemRangeRemoved(0, listData.size)
                listData.clear()
            }

            getFilteredHeadlinesUseCase = getFilteredHeadlinesUseCase.updateFilters(
                binding.keywordsEditText.text.toString(),
                binding.countrySpinner.selectedItem.toString().lowercase(Locale.getDefault()),
                binding.categorySpinner.selectedItem.toString().lowercase(Locale.getDefault())
            )

            MainScope().launch {
                getHeadlinesUseCase.execute().let {
                    if (it.isNotEmpty()) {
                        listData.addAll(getFilteredHeadlinesUseCase.execute())
                        binding.recyclerView.adapter?.notifyItemInserted(listData.size - 1)
                    }
                }
            }
            // make findMoreButton showing filtered articles
            binding.findMoreButton.setOnClickListener { button ->
                MainScope().launch {
                    getHeadlinesUseCase.execute().let {
                        if (it.isNotEmpty()) {
                            listData.addAll(getFilteredHeadlinesUseCase.execute())
                            binding.recyclerView.adapter?.notifyItemInserted(listData.size - 1)
                        } else button.visibility = GONE
                    }
                }
            }
        }*/

        binding.findMoreButton.setOnClickListener { button ->
            MainScope().launch {
                button.isEnabled = false
                getHeadlinesUseCase.execute().let {
                    if (it.isNotEmpty()) {
                        articleList.addAll(it)
                        binding.recyclerView.adapter?.notifyItemInserted(articleList.size - 1)
                    }
                }
                button.isEnabled = true
            }
        }

        binding.findMoreButton.visibility = GONE
        MainScope().launch {
            getHeadlinesUseCase.execute().let {
                if (it.isNotEmpty()) {
                    articleList.addAll(it)
                    binding.recyclerView.adapter?.notifyItemInserted(articleList.size - 1)
                }
            }
            binding.findMoreButton.visibility = VISIBLE
        }

        Log.d("DATA1", articleList.size.toString())
    }

}