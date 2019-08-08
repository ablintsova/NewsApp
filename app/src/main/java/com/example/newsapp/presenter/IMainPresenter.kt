package com.example.newsapp.presenter

import android.content.Context

interface IMainPresenter {
    fun setInteractor(context: Context)
    fun refresh()
    fun showArticlesByPage(page: Int): Boolean
    fun retry()
    fun currentPageNumber(): Int?
    fun updatePageNumber()
}