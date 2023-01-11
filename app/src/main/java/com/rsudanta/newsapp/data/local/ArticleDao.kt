package com.rsudanta.newsapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rsudanta.newsapp.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article: Article)

    @Query("SELECT * FROM article")
    fun getSavedArticle(): Flow<List<Article>>

    @Query("DELETE FROM article WHERE url = :url")
    suspend fun deleteSavedArticle(url: String)
}