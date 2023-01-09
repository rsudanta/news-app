package com.rsudanta.newsapp.data.local

import androidx.room.*
import com.rsudanta.newsapp.models.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSearchHistory(searchHistory: SearchHistory)

    @Query("SELECT DISTINCT keyword FROM searchhistory ORDER BY id DESC")
    fun getSearchHistory(): Flow<List<SearchHistory>>

    @Query("DELETE FROM searchhistory WHERE keyword = :keyword")
    suspend fun deleteSearchHistory(keyword: String)
}