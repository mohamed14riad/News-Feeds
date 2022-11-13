package com.linkdev.newsfeeds.screens.news_details

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.linkdev.newsfeeds.R
import com.linkdev.newsfeeds.data.model.Article
import com.linkdev.newsfeeds.databinding.ActivityNewsDetailsBinding
import com.linkdev.newsfeeds.screens.base_view.BaseActivity
import com.linkdev.newsfeeds.utils.AppUtils
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class NewsDetailsActivity : BaseActivity() {

    companion object {
        private const val TAG = "NewsDtlsAct"
    }

    private lateinit var viewBinding: ActivityNewsDetailsBinding

    private val newsDetailsViewModel: NewsDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityNewsDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // If the activity has never existed before, the value of savedInstanceState is null.
        if (savedInstanceState == null) {
            val article = intent.getParcelableExtra<Article>(AppUtils.KEY_ARTICLE)

            if (article != null) {
                // Save the article in ViewModel to handle configuration changes
                newsDetailsViewModel.setArticle(article)
            } else {
                Toasty.error(
                    this,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT,
                    true
                ).show()
                finish()
            }
        }

        initViews()
    }

    private fun initViews() {
        viewBinding.icBack.setOnClickListener {
            finish()
        }

        newsDetailsViewModel.articleLiveData.observe(this) { article ->

            viewBinding.btnOpenWebsite.setOnClickListener {
                val defaultColors = CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(ContextCompat.getColor(this, R.color.color_primary))
                    .setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.color_secondary))
                    .build()

                val customTabsIntent = CustomTabsIntent.Builder()
                    .setDefaultColorSchemeParams(defaultColors)
                    .build()

                customTabsIntent.launchUrl(this, Uri.parse(article.url))
            }

            Glide.with(this)
                .load(article.urlToImage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(viewBinding.newsItemImage)

            viewBinding.newsItemTitle.text = article.title

            val authorText = getString(R.string.by_author, article.author)
            viewBinding.newsItemAuthor.text = authorText

            viewBinding.newsItemDescription.text = article.description

            try {
                var mFormatter = SimpleDateFormat(AppUtils.API_DATE_FORMAT, Locale.ENGLISH)
                mFormatter.isLenient = false

                var dateText = article.publishedAt
                val date = mFormatter.parse(dateText)

                mFormatter = SimpleDateFormat(AppUtils.APP_DATE_FORMAT, Locale.ENGLISH)
                mFormatter.isLenient = false

                dateText = mFormatter.format(date!!)

                viewBinding.newsItemDate.text = dateText
            } catch (e: Exception) {
                viewBinding.newsItemDate.text = article.publishedAt
            }
        }
    }
}