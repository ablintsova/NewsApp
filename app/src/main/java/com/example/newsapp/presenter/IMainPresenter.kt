package com.example.newsapp.presenter

import com.example.newsapp.model.Article
import com.example.newsapp.view.IMainView
import java.lang.Exception

interface IMainPresenter {
    fun setModules(view: IMainView)
    fun getArticles()
    fun showArticles(data: List<Article>)
    fun onError(exception: Exception)
}