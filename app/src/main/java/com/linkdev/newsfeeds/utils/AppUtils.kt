package com.linkdev.newsfeeds.utils

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.linkdev.newsfeeds.R

class AppUtils {

    companion object {

        const val BASE_URL = "https://newsapi.org/v1/"

        const val NEWS_SOURCE_1 = "the-next-web"
        const val NEWS_SOURCE_2 = "associated-press"

        const val API_KEY = "533af958594143758318137469b41ba9"

        const val API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val APP_DATE_FORMAT = "MMMM d, yyyy"

        const val KEY_ARTICLE = "Article"

        fun Activity.createProgressDialog(): AlertDialog {
            val builder = AlertDialog.Builder(this, R.style.WrapContentDialogTheme)
            val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            return builder.create()
        }

        fun Fragment.createProgressDialog(): AlertDialog {
            val builder = AlertDialog.Builder(requireActivity(), R.style.WrapContentDialogTheme)
            val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            return builder.create()
        }
    }
}