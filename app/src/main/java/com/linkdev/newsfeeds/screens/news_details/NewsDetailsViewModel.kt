package com.linkdev.newsfeeds.screens.news_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.linkdev.newsfeeds.data.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor() : ViewModel() {

    private val _articleLiveData = MutableLiveData<Article>()
    val articleLiveData: LiveData<Article> = _articleLiveData

    fun setArticle(article: Article) {
        _articleLiveData.value = article
    }
}