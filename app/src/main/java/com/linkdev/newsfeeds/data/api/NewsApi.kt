package com.linkdev.newsfeeds.data.api

import com.linkdev.newsfeeds.data.model.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("articles")
    suspend fun getNews(
        @Query("source") source: String,
        @Query("apiKey") apiKey: String,
    ): Response<News>
}