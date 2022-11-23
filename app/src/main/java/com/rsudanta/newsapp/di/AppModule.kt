package com.rsudanta.newsapp.di

import android.content.Context
import androidx.room.Room
import com.rsudanta.newsapp.data.local.ArticleDatabase
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
    fun provideArticleDatabase(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

}