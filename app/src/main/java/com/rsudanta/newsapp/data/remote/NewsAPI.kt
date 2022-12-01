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
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("pageSize")
        pageSize: Int = 5
    ): Response<News>

    @GET("v2/top-headlines")
    @Headers("x-api-key:${API_KEY}")
    suspend fun getNews(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("category")
        category: String = ""
    ): Response<News>

    @GET("v2/everything")
    @Headers("x-api-key:${API_KEY}")
    suspend fun searchNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1
    ): Response<News>
}