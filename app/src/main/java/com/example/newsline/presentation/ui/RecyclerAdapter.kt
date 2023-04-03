package com.example.newsline.presentation.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsline.R
import com.example.newsline.databinding.NewsListElementBinding
import com.example.newsline.domain.models.Article
import java.security.PrivateKey
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.Collection

class RecyclerAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val items = mutableListOf<Article>()

    fun setNewData(list: List<Article>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun updateData(list: List<Article>) {
        for (newItem in list) {
            if(!items.contains(newItem)) {
                items.add(newItem)
                notifyItemInserted(items.size - 1)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = NewsListElementBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.news_list_element, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if (!items[position].urlToImage.isNullOrEmpty()) {
                Glide.with(context)
                    .load(items[position].urlToImage)
                    .placeholder(androidx.appcompat.R.drawable.abc_ic_menu_selectall_mtrl_alpha)
                    .into(imageView)
            } else imageCardView.visibility = GONE

            titleTextView.text = items[position].title

            authorTextView.text = items[position].author

            if (!items[position].description.isNullOrEmpty()) {
                descriptionTextView.text = items[position].description
            } else descriptionTextView.visibility = GONE

            if (!items[position].publishedAt.isNullOrEmpty()) {
                val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val date: Date = inputDateFormat.parse(items[position].publishedAt)!!
                val outputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
                val outputDateString = outputDateFormat.format(date)
                publishedAtTextView.text = outputDateString
            } else publishedAtTextView.visibility = GONE

            if (!items[position].url.isNullOrEmpty()) {
                toArticleHyperLinkTextView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(items[position].url)
                    startActivity(context, intent, null)
                }
            } else toArticleHyperLinkTextView.visibility = GONE
        }
    }

    override fun getItemCount() = items.size
}
