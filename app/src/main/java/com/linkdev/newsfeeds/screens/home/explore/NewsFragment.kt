package com.linkdev.newsfeeds.screens.home.explore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.linkdev.newsfeeds.R
import com.linkdev.newsfeeds.adapter.NewsAdapter
import com.linkdev.newsfeeds.data.model.Article
import com.linkdev.newsfeeds.data.model.Status
import com.linkdev.newsfeeds.databinding.FragmentNewsBinding
import com.linkdev.newsfeeds.screens.news_details.NewsDetailsActivity
import com.linkdev.newsfeeds.utils.AppUtils
import com.linkdev.newsfeeds.utils.AppUtils.Companion.createProgressDialog
import com.linkdev.newsfeeds.utils.NetworkHelper
import com.linkdev.newsfeeds.utils.PrefManager
import com.linkdev.newsfeeds.utils.VerticalListItemMargin
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : Fragment(),
    NewsAdapter.OnArticleClickListener {

    private val TAG = "NewsFrag"

    private lateinit var viewBinding: FragmentNewsBinding

    private lateinit var progressDialog: AlertDialog

    @Inject
    lateinit var prefManager: PrefManager

    @Inject
    lateinit var networkHelper: NetworkHelper

    private val newsViewModel: NewsViewModel by activityViewModels()

    private lateinit var articles: MutableList<Article>
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentNewsBinding.inflate(inflater, container, false)

        initViews()

        return viewBinding.root
    }

    private fun initViews() {
        progressDialog = createProgressDialog()

        articles = mutableListOf()
        newsAdapter = NewsAdapter(
            requireActivity(),
            mutableListOf(),
            this
        )

        val articlesListColumns = 1
        val articlesItemMargin =
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._16sdp)

        viewBinding.rvNews.addItemDecoration(
            VerticalListItemMargin(
                articlesListColumns,
                articlesItemMargin,
                prefManager.getLanguageCode() == PrefManager.ar
            )
        )
        viewBinding.rvNews.layoutManager =
            GridLayoutManager(
                requireActivity(),
                articlesListColumns,
                RecyclerView.VERTICAL,
                false
            )
        viewBinding.rvNews.adapter = newsAdapter


        getNews()
    }

    private fun getNews() {
        newsViewModel.articlesLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.show()
                    Log.d(TAG, "getNews: LOADING...")
                    Log.d(TAG, "getNews: ${it.message}")
                }
                Status.COMPLETE -> {
                    progressDialog.dismiss()
                    Log.d(TAG, "getNews: COMPLETE")
                    Log.d(TAG, "getNews: ${it.message}")

                    it.data?.let { data ->
                        if (data.isNotEmpty()) {
                            articles.addAll(data)
                            newsAdapter.addItems(data)
                        } else {
                            Toasty.info(
                                requireActivity(),
                                getString(R.string.no_articles_msg),
                                Toast.LENGTH_SHORT,
                                true
                            ).show()
                        }
                    } ?: run {
                        Toasty.error(
                            requireActivity(),
                            getString(R.string.error_msg),
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    Log.e(TAG, "getNews: ERROR")
                    Log.e(TAG, "getNews: ${it.message}")

                    val msg = if (!networkHelper.isNetworkAvailable()) {
                        getString(R.string.check_internet_msg)
                    } else {
                        getString(R.string.error_msg)
                    }
                    Toasty.error(
                        requireActivity(),
                        msg,
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
            }
        }
    }

    override fun onArticleClick(article: Article) {
        val intent = Intent(requireActivity(), NewsDetailsActivity::class.java)
        intent.putExtra(AppUtils.KEY_ARTICLE, article)
        startActivity(intent)
    }
}