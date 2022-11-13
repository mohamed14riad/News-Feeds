package com.linkdev.newsfeeds.screens.base_view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.linkdev.newsfeeds.utils.LanguageContextWrapper
import com.linkdev.newsfeeds.utils.PrefManager
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import java.util.*

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface LanguageContextWrapperInterface {
        val prefManager: PrefManager
        val languageContextWrapper: LanguageContextWrapper
    }

    override fun attachBaseContext(newBase: Context?) {
        // .. create or get your new Locale object here.
        newBase?.let {
            val wrapperInterface =
                EntryPoints.get(it.applicationContext, LanguageContextWrapperInterface::class.java)

            val newLocale = Locale(wrapperInterface.prefManager.getLanguageCode())
            val context = wrapperInterface.languageContextWrapper.wrap(newLocale)

            super.attachBaseContext(context)
        }
    }
}