package com.example.newsapp.model

import java.lang.Exception

interface IMainInteractor {
    fun getArticles()
    fun onDataAvailable(data: List<Article>)
    fun onError(exception: Exception)
}