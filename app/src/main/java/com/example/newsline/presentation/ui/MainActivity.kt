package com.example.newsline.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsline.R
import com.example.newsline.data.remoteDatasource.enums.Category
import com.example.newsline.data.remoteDatasource.enums.Country
import com.example.newsline.databinding.ActivityMainBinding
import com.example.newsline.domain.LocationService
import com.example.newsline.presentation.App
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var articlesAdapter: RecyclerAdapter
    private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        locationService = (applicationContext as App).instanceProvider.provideLocationService()

        setUpUi()

        mainViewModel.articles.observe(this) {
            articlesAdapter.setNewData(it)
            Log.d("ddd", "articles.observed : ${it.size}")
        }
        mainViewModel.isFetchingNews.observe(this) { loading: Boolean ->
            if (loading)  binding.findMoreButton.text = resources.getString(R.string.loading)
            else binding.findMoreButton.text = resources.getString(R.string.loadMore)
            binding.findMoreButton.isEnabled = !loading
            Log.d("ddd", "isFetchingNews.observed : $loading")
        }
    }

    private fun setUpUi() {
        // articles adapter
        articlesAdapter = RecyclerAdapter(this@MainActivity)
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = articlesAdapter
        }
        // country spinner
        val countrySpinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Country.values())
        binding.countrySpinner.adapter = countrySpinnerAdapter
        binding.countrySpinner.setSelection(
            countrySpinnerAdapter.getPosition(locationService.getCurrentLocationCountryCode())
        )
        // category spinner
        binding.categorySpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Category.values())

        binding.filterRelativeLayout.visibility = GONE
        // open filters button
        binding.filterImageButton.setOnClickListener {
            if (binding.filterRelativeLayout.visibility == GONE) {
                binding.filterRelativeLayout.visibility = VISIBLE
            } else {
                binding.filterRelativeLayout.visibility = GONE
                binding.countrySpinner.setSelection(
                    countrySpinnerAdapter.getPosition(
                        locationService.getCurrentLocationCountryCode()
                    )
                )
            }
        }
        // clear filters button
        binding.filterOffImageButton.setOnClickListener {
            mainViewModel.setFilters(
                "",
                locationService.getCurrentLocationCountry(),
                ""
            )
            mainViewModel.loadArticles()
            binding.filterRelativeLayout.visibility = GONE
        }
        // apply filters button
        binding.findImageButton.setOnClickListener {
            mainViewModel.setFilters(
                binding.keywordsEditText.text.toString(),
                binding.countrySpinner.selectedItem.toString().lowercase(Locale.getDefault()),
                binding.categorySpinner.selectedItem.toString().lowercase(Locale.getDefault())
            )
            mainViewModel.loadArticles()
        }
        // more articles button
        binding.findMoreButton.setOnClickListener { mainViewModel.loadArticles() }
    }
}