package com.rsudanta.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
):Serializable