package com.example.newsapp.presenter

import com.example.newsapp.model.Article
import com.example.newsapp.model.IMainInteractor
import com.example.newsapp.view.IMainView
import java.lang.Exception
import javax.inject.Inject

class MainPresenter @Inject constructor(private val mainInteractor: IMainInteractor) : IMainPresenter {

    private lateinit var mainView: IMainView

    /* IMainPresenter */

    override fun setModules(view: IMainView) {
        mainView = view
        mainInteractor.setPresenter(this)
    }

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