package com.example.newsapp.view

import com.example.newsapp.model.Article
import java.lang.Exception

interface IMainView {
    fun showArticles(data: List<Article>)
    fun onError(exception: Exception)
}