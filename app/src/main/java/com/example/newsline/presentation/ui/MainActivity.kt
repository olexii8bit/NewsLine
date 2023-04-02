package com.example.newsline.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsline.R
import com.example.newsline.databinding.ActivityMainBinding
import com.example.newsline.domain.models.Article
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val articlesList = mutableListOf<Article>()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = RecyclerAdapter(this@MainActivity, articlesList)
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

        /*getHeadlinesUseCase.newCountry(
            LocationService
                .Base((application as App).appContext)
                .getCurrentLocationCountry()
        ).let { newUseCase -> getHeadlinesUseCase = newUseCase }*/

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

        mainViewModel.headlinesLiveData.observe(this) { newArticlesList ->
            Log.d("AAA", "observed : " + newArticlesList.size)
            articlesList.clear()
            articlesList.addAll(newArticlesList)
            binding.recyclerView.adapter!!.notifyDataSetChanged()
        }

        binding.findMoreButton.setOnClickListener { button ->
            button.isEnabled = false
            mainViewModel.loadMoreHeadlines()
            button.isEnabled = true
        }
    }
}