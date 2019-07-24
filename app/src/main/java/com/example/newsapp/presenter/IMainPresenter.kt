package com.example.newsapp.presenter

import com.example.newsapp.model.Article
import java.lang.Exception

interface IMainPresenter {
    fun getArticles()
    fun showArticles(data: List<Article>)
    fun onError(exception: Exception)
}