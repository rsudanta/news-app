package com.rsudanta.newsapp.ui

import androidx.lifecycle.ViewModel
import com.rsudanta.newsapp.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(val newsRepository: NewsRepository) : ViewModel() {
}