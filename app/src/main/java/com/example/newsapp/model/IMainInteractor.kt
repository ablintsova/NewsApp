package com.example.newsapp.model

import com.example.newsapp.presenter.IMainPresenter
import java.lang.Exception

interface IMainInteractor {
    fun setPresenter(presenter: IMainPresenter)
    fun getArticles()
    fun onDataAvailable(data: List<Article>)
    fun onError(exception: Exception)
}