package com.rsudanta.newsapp.repository

import com.rsudanta.newsapp.data.local.ArticleDatabase
import com.rsudanta.newsapp.data.remote.NewsAPI
import com.rsudanta.newsapp.models.Category
import javax.inject.Inject

class NewsRepository @Inject constructor(val db: ArticleDatabase, val api: NewsAPI) {

    suspend fun getNews(countryCode: String, pageNumber: Int, category: String, pageSize:Int? = null) =
        api.getNews(countryCode, pageNumber, category, pageSize)
}