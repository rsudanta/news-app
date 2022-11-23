package com.rsudanta.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rsudanta.newsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}