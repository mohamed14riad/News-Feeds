package com.linkdev.newsfeeds.di

import android.content.Context
import com.linkdev.newsfeeds.BuildConfig
import com.linkdev.newsfeeds.data.api.NewsApi
import com.linkdev.newsfeeds.data.repo.NewsRepo
import com.linkdev.newsfeeds.utils.AppUtils
import com.linkdev.newsfeeds.utils.LanguageContextWrapper
import com.linkdev.newsfeeds.utils.NetworkHelper
import com.linkdev.newsfeeds.utils.PrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideBaseUrl() = AppUtils.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()

        okHttpBuilder.connectTimeout(1, TimeUnit.MINUTES)
        okHttpBuilder.readTimeout(1, TimeUnit.MINUTES)
        okHttpBuilder.writeTimeout(1, TimeUnit.MINUTES)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpBuilder.addInterceptor(loggingInterceptor)
        }

        return okHttpBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepo(newsApi: NewsApi): NewsRepo {
        return NewsRepo(newsApi)
    }

    @Provides
    @Singleton
    fun providePrefManager(@ApplicationContext appContext: Context): PrefManager {
        return PrefManager(appContext)
    }

    @Provides
    @Singleton
    fun provideLanguageContextWrapper(@ApplicationContext appContext: Context): LanguageContextWrapper {
        return LanguageContextWrapper(appContext)
    }

    @Provides
    @Singleton
    fun provideNetworkHelper(@ApplicationContext appContext: Context): NetworkHelper {
        return NetworkHelper(appContext)
    }
}