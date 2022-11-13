package com.linkdev.newsfeeds.data.model

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("status") val status: String,
    @SerializedName("source") val source: String,
    @SerializedName("sortBy") val sortBy: String,
    @SerializedName("articles") val articles: List<Article>,
)