package com.rsudanta.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rsudanta.newsapp.NewsApplication
import com.rsudanta.newsapp.models.Article
import com.rsudanta.newsapp.models.News
import com.rsudanta.newsapp.models.SearchHistory
import com.rsudanta.newsapp.repository.HistoryRepository
import com.rsudanta.newsapp.repository.NewsRepository
import com.rsudanta.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    application: Application,
    private val newsRepository: NewsRepository,
    private val historyRepository: HistoryRepository
) : AndroidViewModel(application) {
    val breakingNews: MutableLiveData<Resource<News>> = MutableLiveData()
    val news: MutableLiveData<Resource<News>> = MutableLiveData()
    var categorizedNews: MutableLiveData<Resource<News>> = MutableLiveData()
    var searchResult: MutableLiveData<Resource<News>> = MutableLiveData()
    val savedArticle: MutableLiveData<Resource<List<Article>>> = MutableLiveData()

    var searchHistory: MutableLiveData<List<SearchHistory>> = MutableLiveData()


    private val newsPage = 1

    init {
        getBreakingNews()
        getNews()
        getSavedNews()
    }

    private fun getBreakingNews() {
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            try {
                if (hasInternetConnection()) {
                    val response = newsRepository.getNews(pageNumber = 1, pageSize = 5)
                    breakingNews.postValue(handleNewsResponse(response))
                } else {
                    breakingNews.postValue(Resource.Error("No internet connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                    else -> breakingNews.postValue(Resource.Error("Conversion Error"))
                }
            }
        }
    }

    private fun getNews() {
        viewModelScope.launch {
            news.postValue(Resource.Loading())
            try {
                if (hasInternetConnection()) {
                    val response = newsRepository.getNews(pageNumber = 2)
                    news.postValue(handleNewsResponse(response))
                } else {
                    news.postValue(Resource.Error("No internet connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> news.postValue(Resource.Error("Network Failure"))
                    else -> news.postValue(Resource.Error("Conversion Error"))
                }
            }
        }
    }

    fun getCategorizedNews(category: String) {
        viewModelScope.launch {
            categorizedNews.postValue(Resource.Loading())
            try {
                if (hasInternetConnection()) {
                    val response = newsRepository.getNews(pageNumber = 1, category = category)
                    categorizedNews.postValue(handleNewsResponse(response))
                } else {
                    categorizedNews.postValue(Resource.Error("No internet connection"))
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> categorizedNews.postValue(Resource.Error("Network Failure"))
                    else -> categorizedNews.postValue(Resource.Error("Conversion Error"))
                }
            }

        }
    }

    private fun handleNewsResponse(response: Response<News>): Resource<News> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
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

    private fun getSavedNews() {
        viewModelScope.launch {
            news.postValue(Resource.Loading())
            historyRepository.getSavedNews().collect { articles ->
                savedArticle.postValue(Resource.Success(articles))
            }
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            historyRepository.saveArticle(article)
        }
    }

    fun deleteSavedArticle(url: String) {
        viewModelScope.launch {
            historyRepository.deleteSavedArticle(url)
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}