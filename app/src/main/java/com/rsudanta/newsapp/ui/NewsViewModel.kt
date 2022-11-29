package com.rsudanta.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsudanta.newsapp.models.News
import com.rsudanta.newsapp.repository.NewsRepository
import com.rsudanta.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {
    val news: MutableLiveData<Resource<News>> = MutableLiveData()
    val newsPage = 1

    init {
        getBreakingNews("id")
    }

    private fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            news.postValue(Resource.Loading())
            val response = newsRepository.getBreakingNews(countryCode, newsPage)
            news.postValue(handleNewsResponse(response))
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
}