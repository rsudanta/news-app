package com.rsudanta.newsapp.repository

import com.rsudanta.newsapp.data.local.ArticleDao
import com.rsudanta.newsapp.data.local.NewsAppDatabase
import com.rsudanta.newsapp.data.local.SearchHistoryDao
import com.rsudanta.newsapp.data.remote.NewsAPI
import com.rsudanta.newsapp.models.Article
import com.rsudanta.newsapp.models.SearchHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao,
    private val articleDao: ArticleDao
) {

    suspend fun saveArticle(article: Article) = articleDao.saveArticle(article)

    fun getSavedNews() = articleDao.getSavedArticle()

    suspend fun deleteSavedArticle(url: String) = articleDao.deleteSavedArticle(url)

    suspend fun saveSearchHistory(searchHistory: SearchHistory) =
        searchHistoryDao.saveSearchHistory(searchHistory)

    fun getSearchHistory() =
        searchHistoryDao.getSearchHistory()

    suspend fun deleteSearchHistory(keyword: String) =
        searchHistoryDao.deleteSearchHistory(keyword)
}