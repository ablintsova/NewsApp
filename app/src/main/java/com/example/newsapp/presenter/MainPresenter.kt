package com.example.newsapp.presenter

import com.example.newsapp.model.Article
import com.example.newsapp.model.IMainInteractor
import com.example.newsapp.model.MainInteractor
import com.example.newsapp.view.IMainView
import java.lang.Exception

class MainPresenter(private val mainView: IMainView) : IMainPresenter {

    private val mainInteractor: IMainInteractor

    init {
        mainInteractor = MainInteractor(this)
    }

    /* IMainPresenter */

    override fun getArticles() {
        mainInteractor.getArticles()
    }

    override fun showArticles(data: List<Article>) {
        mainView.showArticles(data)
    }

    override fun onError(exception: Exception) {
        mainView.onError(exception)
    }
}