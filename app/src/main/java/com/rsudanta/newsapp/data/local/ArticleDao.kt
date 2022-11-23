package com.rsudanta.newsapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rsudanta.newsapp.models.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article: Article)

    @Query("SELECT * FROM article")
    fun getSavedArticle(): LiveData<List<Article>>

    @Delete
    suspend fun deleteSavedArticle(article: Article)
}