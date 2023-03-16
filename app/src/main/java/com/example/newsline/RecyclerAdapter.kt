package com.example.newsline

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
import java.text.SimpleDateFormat
import java.util.*


class RecyclerAdapter(private val news: List<ResponseDTO.Companion.Article>, private val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val publishedAtTextView: TextView = itemView.findViewById(R.id.publishedAtTextView)
        val toArticleHyperLinkTextView: TextView = itemView.findViewById(R.id.toArticleHyperLinkTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.news_list_element, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (news[position].urlToImage.isNullOrEmpty()) {
            holder.imageView.visibility = GONE
        } else {
            Glide.with(context)
                .load(news[position].urlToImage)
                .centerCrop()
                .placeholder(androidx.appcompat.R.drawable.abc_ic_menu_selectall_mtrl_alpha)
                .into(holder.imageView)
        }

        holder.titleTextView.text = news[position].title

        holder.authorTextView.text = news[position].author

        if (news[position].description.isNullOrEmpty()) {
            holder.descriptionTextView.visibility = GONE
        } else holder.descriptionTextView.text = news[position].description

        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date: Date = inputDateFormat.parse(news[position].publishedAt)
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val outputDateString = outputDateFormat.format(date)
        holder.publishedAtTextView.text = outputDateString

        holder.toArticleHyperLinkTextView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(news[position].url)
            startActivity(context, intent, null)
        }
    }

    override fun getItemCount() = news.size

}