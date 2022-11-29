package com.rsudanta.newsapp.repository

import com.rsudanta.newsapp.data.local.ArticleDatabase
import com.rsudanta.newsapp.data.remote.NewsAPI
import javax.inject.Inject

class NewsRepository @Inject constructor(val db: ArticleDatabase, val api: NewsAPI) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

//    suspend fun searchNews
}