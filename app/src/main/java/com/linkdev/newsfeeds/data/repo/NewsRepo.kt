package com.linkdev.newsfeeds.data.repo

import com.linkdev.newsfeeds.data.api.NewsApi
import javax.inject.Inject

class NewsRepo @Inject constructor(private val api: NewsApi) {

    suspend fun getNews(
        source: String,
        apiKey: String,
    ) = api.getNews(source, apiKey)
}