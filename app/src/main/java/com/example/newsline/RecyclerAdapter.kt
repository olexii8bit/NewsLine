package com.example.newsline

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import api.ResponseDTO
import com.bumptech.glide.Glide

class RecyclerAdapter(private val news: List<ResponseDTO.Companion.Article>, private val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.news_list_element, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(news[position].urlToImage)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.imageView);
        holder.titleTextView.text = news[position].title
        holder.authorTextView.text = news[position].author
        holder.descriptionTextView.text = news[position].description
    }

    override fun getItemCount() = news.size

}