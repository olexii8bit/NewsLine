package com.example.newsline.domain.repository

import com.example.newsline.domain.models.Article

interface LocalArticleRepository {
    fun saveArticle(article: Article)
    fun getArticles()
}