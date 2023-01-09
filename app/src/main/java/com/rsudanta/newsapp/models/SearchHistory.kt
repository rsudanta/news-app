package com.rsudanta.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val keyword: String
)