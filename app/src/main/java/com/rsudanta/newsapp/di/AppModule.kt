package com.rsudanta.newsapp.di

import android.content.Context
import androidx.room.Room
import com.rsudanta.newsapp.adapter.*
import com.rsudanta.newsapp.data.local.ArticleDao
import com.rsudanta.newsapp.data.local.NewsAppDatabase
import com.rsudanta.newsapp.data.local.SearchHistoryDao
import com.rsudanta.newsapp.data.remote.NewsAPI
import com.rsudanta.newsapp.util.Constant.BASE_URL
import com.rsudanta.newsapp.util.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsAppDatabase(@ApplicationContext context: Context): NewsAppDatabase {
        return Room.databaseBuilder(
            context,
            NewsAppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideArticleDao(database: NewsAppDatabase): ArticleDao =
        database.articleDao()

    @Singleton
    @Provides
    fun provideSearchHistoryDao(database: NewsAppDatabase): SearchHistoryDao =
        database.searchHistoryDao()

    @Provides
    @Singleton
    fun provideBreakingNewsAdapter(): BreakingNewsAdapter =
        BreakingNewsAdapter()

    @Provides
    @Singleton
    fun provideCategoryAdapter(): CategoryAdapter =
        CategoryAdapter()

    @Provides
    @Singleton
    fun provideNewsAdapter(): NewsAdapter =
        NewsAdapter()

    @Provides
    @Singleton
    fun provideSavedNewsAdapter(): SavedNewsAdapter =
        SavedNewsAdapter()

    @Provides
    @Singleton
    fun provideSearchHistoryAdapter(): SearchHistoryAdapter =
        SearchHistoryAdapter()
}

