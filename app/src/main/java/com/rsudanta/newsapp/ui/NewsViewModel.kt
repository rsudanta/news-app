package com.rsudanta.newsapp.ui

import android.content.res.Resources
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsudanta.newsapp.models.News
import com.rsudanta.newsapp.models.SearchHistory
import com.rsudanta.newsapp.repository.HistoryRepository
import com.rsudanta.newsapp.repository.NewsRepository
import com.rsudanta.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {
    val breakingNews: MutableLiveData<Resource<News>> = MutableLiveData()
    val news: MutableLiveData<Resource<News>> = MutableLiveData()
    var categorizedNews: MutableLiveData<Resource<News>> = MutableLiveData()
    var searchResult: MutableLiveData<Resource<News>> = MutableLiveData()

    var searchHistory: MutableLiveData<List<SearchHistory>> = MutableLiveData()


    private val newsPage = 1

    init {
        getBreakingNews()
        getNews()
    }

    private fun getBreakingNews() {
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val response = newsRepository.getNews(pageNumber = 1, pageSize = 5)
            breakingNews.postValue(handleNewsResponse(response))
        }
    }

    private fun getNews() {
        viewModelScope.launch {
            news.postValue(Resource.Loading())
            val response = newsRepository.getNews(pageNumber = 2)
            news.postValue(handleNewsResponse(response))
        }
    }

    fun getCategorizedNews(category: String) {
        viewModelScope.launch {
            categorizedNews.postValue(Resource.Loading())
            val response = newsRepository.getNews(pageNumber = 1, category = category)
            categorizedNews.postValue(handleNewsResponse(response))
        }
    }

    private fun handleNewsResponse(response: Response<News>): Resource<News> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        Log.e("err", response.toString())
        return Resource.Error(response.message())
    }

    fun saveSearchHistory(searchHistory: SearchHistory) {
        viewModelScope.launch {
            historyRepository.saveSearchHistory(searchHistory)
        }
    }

    fun getSearchHistory() {
        viewModelScope.launch {
            historyRepository.getSearchHistory().collect {
                searchHistory.postValue(it)
            }
        }
    }

    fun deleteSearchHistory(keyword: String) {
        viewModelScope.launch {
            historyRepository.deleteSearchHistory(keyword)
        }
    }

    fun getSearchResult(keyword: String) {
        viewModelScope.launch {
            searchResult.postValue(Resource.Loading())
            val response = newsRepository.searchNews(keyword = keyword, pageNumber = 1)
            searchResult.postValue(handleNewsResponse(response))
        }
    }

}