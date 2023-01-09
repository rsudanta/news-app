package com.rsudanta.newsapp.data.local

import androidx.room.*
import com.rsudanta.newsapp.models.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSearchHistory(searchHistory: SearchHistory)

    @Query("SELECT * FROM searchhistory ORDER BY id DESC")
    fun getSearchHistory(): Flow<List<SearchHistory>>

    @Delete
    suspend fun deleteSearchHistory(searchHistory: SearchHistory)
}