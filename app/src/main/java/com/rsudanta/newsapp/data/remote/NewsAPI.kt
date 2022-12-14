package com.rsudanta.newsapp.data.remote

import com.rsudanta.newsapp.models.News
import com.rsudanta.newsapp.util.ApiKey.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    @Headers("x-api-key:${API_KEY}")
    suspend fun getNews(
        @Query("country")
        country: String,
        @Query("page")
        pageNumber: Int,
        @Query("category")
        category: String,
        @Query("pageSize")
        pageSize: Int?
    ): Response<News>

    @GET("v2/everything")
    @Headers("x-api-key:${API_KEY}")
    suspend fun searchNews(
        @Query("q")
        keyword: String,
        @Query("page")
        pageNumber: Int
    ): Response<News>
}