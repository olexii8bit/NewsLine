package com.example.newsline.domain.models

data class ResponseDTO(val status: String = "",
                       val code: String = "",
                       val totalResults: Int = 0,
                       val articles: List<Article>)