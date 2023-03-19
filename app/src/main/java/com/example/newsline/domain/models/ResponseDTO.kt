package com.example.newsline.domain.models

data class ResponseDTO(val status: String,
                       val totalResults: Int,
                       val articles: Array<Article>) {
}
