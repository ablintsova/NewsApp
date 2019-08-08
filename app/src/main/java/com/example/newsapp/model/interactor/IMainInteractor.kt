package com.example.newsapp.model.interactor

import android.content.Context
import com.example.newsapp.model.Article
import com.example.newsapp.model.Listing
import com.example.newsapp.presenter.IMainPresenter

interface IMainInteractor {
    fun setModules(presenter: IMainPresenter, context: Context)
    fun getArticlesByPage(page: Int): Listing<Article>
}