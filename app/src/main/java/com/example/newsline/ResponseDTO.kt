package com.example.newsline

data class ResponseDTO(val status: String,
                       val totalResults: Int,
                       val articles: Array<Article>) {
    companion object{
        data class Source (val id: String, val name: String) { }
        data class Article(val source: Source,
                           val author: String,
                           val title: String,
                           val description: String,
                           val url: String,
                           val urlToImage: String,
                           val publishedAt: String,
                           val content: String) { }
    }
}
