package com.rsudanta.newsapp.repository

import com.rsudanta.newsapp.data.local.NewsAppDatabase
import com.rsudanta.newsapp.data.remote.NewsAPI
import com.rsudanta.newsapp.models.SearchHistory
import javax.inject.Inject

class NewsRepository @Inject constructor(private val api: NewsAPI) {

    suspend fun getNews(
        countryCode: String,
        pageNumber: Int,
        category: String,
        pageSize: Int? = null
    ) =
        api.getNews(countryCode, pageNumber, category, pageSize)
}