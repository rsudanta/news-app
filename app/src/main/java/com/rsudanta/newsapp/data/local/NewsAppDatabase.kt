package com.rsudanta.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rsudanta.newsapp.models.Article
import com.rsudanta.newsapp.models.SearchHistory

@Database(
    entities = [Article::class, SearchHistory::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NewsAppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}