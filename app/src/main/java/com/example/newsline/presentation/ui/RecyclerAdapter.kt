package com.example.newsline.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.newsline.R
import com.example.newsline.databinding.NewsListElementBinding
import com.example.newsline.domain.models.Article
import java.text.SimpleDateFormat
import java.util.Date

class ArticlesDiffCallback(
    private val oldItems: List<Article>,
    private val newItems: List<Article>,
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }

}

class RecyclerAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val items = mutableListOf<Article>()

    fun setNewData(newItems: List<Article>) {
        val diffCallback = ArticlesDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = 
            NewsListElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(items.isNotEmpty()) { holder.bind(items[position]) }
    }

    @Suppress("UselessCallOnNotNull")
    @SuppressLint("SimpleDateFormat")
    class ViewHolder(private val binding: NewsListElementBinding, private val context: Context)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Article) {
            binding.apply {
                if (!item.urlToImage.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(item.urlToImage)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean,
                            ): Boolean {
                                imageCardView.visibility = View.GONE
                                return true
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean,
                            ): Boolean {
                                imageView.setImageDrawable(resource)
                                return true
                            }

                        })
                        .placeholder(R.drawable.ic_image_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(imageView)
                } else imageCardView.visibility = View.GONE

                if (!item.title.isNullOrEmpty()) {
                    titleTextView.text = item.title
                } else titleTextView.visibility = View.GONE

                if (!item.author.isNullOrEmpty()) {
                    authorTextView.text = item.title
                } else authorTextView.visibility = View.GONE

                if (!item.description.isNullOrEmpty()) {
                    descriptionTextView.text = item.description
                } else descriptionTextView.visibility = View.GONE

                if (!item.publishedAt.isNullOrEmpty()) {
                    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    val date: Date = inputDateFormat.parse(item.publishedAt)!!
                    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
                    val outputDateString = outputDateFormat.format(date)
                    publishedAtTextView.text = outputDateString
                } else publishedAtTextView.visibility = View.GONE

                if (!item.url.isNullOrEmpty()) {
                    toArticleHyperLinkTextView.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(item.url)
                        ContextCompat.startActivity(context, intent, null)
                    }
                } else toArticleHyperLinkTextView.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = items.size
}
