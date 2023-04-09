package com.example.newsline.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsline.R
import com.example.newsline.databinding.ActivityMainBinding
import androidx.activity.viewModels
import com.example.newsline.data.newsApi.enums.Category
import com.example.newsline.data.newsApi.enums.Country
import com.example.newsline.domain.LocationService
import com.example.newsline.presentation.App
import java.util.*

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val locationService: LocationService =
            (applicationContext as App).instanceProvider.provideLocationService()

        val headlinesAdapter = RecyclerAdapter(this@MainActivity)
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = headlinesAdapter
        }

        val countrySpinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Country.values())
        binding.countrySpinner.adapter = countrySpinnerAdapter
        binding.countrySpinner.setSelection(countrySpinnerAdapter.getPosition(locationService.getCurrentLocationCountryCode()))

        binding.categorySpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Category.values())

        binding.filterRelativeLayout.visibility = GONE
        binding.filterImageButton.setOnClickListener {
            if (binding.filterRelativeLayout.visibility == GONE) {
                binding.filterRelativeLayout.visibility = VISIBLE
                binding.filterRelativeLayout.gravity = Gravity.TOP
            } else {
                binding.filterRelativeLayout.visibility = GONE
                binding.countrySpinner.setSelection(
                    countrySpinnerAdapter.getPosition(
                        locationService.getCurrentLocationCountryCode()
                    )
                )
            }
        }

        binding.filterOffImageButton.setOnClickListener {
            mainViewModel.setFilteredFinding(false)
            headlinesAdapter.clear()
            mainViewModel.loadData()
        }

        binding.findImageButton.setOnClickListener {
            mainViewModel.setFilters(
                binding.keywordsEditText.text.toString(),
                binding.countrySpinner.selectedItem.toString().lowercase(Locale.getDefault()),
                binding.categorySpinner.selectedItem.toString().lowercase(Locale.getDefault())
            )
            headlinesAdapter.clear()
            mainViewModel.setFilteredFinding(true)
            mainViewModel.loadMoreHeadlines()
        }

        mainViewModel.newsLiveData.observe(this) { updatedArticlesList ->
            Log.d("AAA", "observed : " + updatedArticlesList.size)
            headlinesAdapter.updateData(updatedArticlesList)
        }

        binding.findMoreButton.setOnClickListener { button ->
            button.isEnabled = false
            mainViewModel.loadMoreHeadlines()
            button.isEnabled = true
        }
    }
}