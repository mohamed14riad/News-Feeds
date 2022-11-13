package com.linkdev.newsfeeds.screens.home.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkdev.newsfeeds.data.model.Article
import com.linkdev.newsfeeds.data.model.Result
import com.linkdev.newsfeeds.data.repo.NewsRepo
import com.linkdev.newsfeeds.utils.AppUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repo: NewsRepo
) : ViewModel() {

    companion object {
        private const val TAG = "NewsViewModel"
    }

    private val _articlesLiveData = MutableLiveData<Result<List<Article>>>()
    val articlesLiveData: LiveData<Result<List<Article>>> = _articlesLiveData

    init {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
//            throwable.printStackTrace()
            Log.e(TAG, "coroutineExceptionHandler: ", throwable)

            _articlesLiveData.postValue(
                Result.error(
                    message = "Request Status Is Error with exception: ${throwable.message}",
                )
            )
        }

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _articlesLiveData.postValue(
                Result.loading(
                    message = "Request Status Is Loading"
                )
            )

            val response1 = async {
                repo.getNews(AppUtils.NEWS_SOURCE_1, AppUtils.API_KEY)
            }.await()
            val response2 = async {
                repo.getNews(AppUtils.NEWS_SOURCE_2, AppUtils.API_KEY)
            }.await()

            if (response1.isSuccessful && response2.isSuccessful) {
                val articles = mutableListOf<Article>()
                articles.addAll(response1.body()!!.articles)
                articles.addAll(response2.body()!!.articles)

                _articlesLiveData.postValue(
                    Result.complete(
                        data = articles,
                        message = "Request Status Is Complete",
                    )
                )
            } else {
                _articlesLiveData.postValue(
                    Result.error(
                        message = "Request Status Is Error with response:" +
                                "\n${response1.errorBody()?.string()}" +
                                "\n${response2.errorBody()?.string()}",
                    )
                )
            }
        }
    }
}