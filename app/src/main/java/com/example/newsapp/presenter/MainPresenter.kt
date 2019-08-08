package com.example.newsapp.presenter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.newsapp.model.interactor.IMainInteractor
import javax.inject.Inject

class MainPresenter @Inject constructor(private val mainInteractor: IMainInteractor) : IMainPresenter {

    private val currentPage = MutableLiveData<Int>()
    private val repoResult = Transformations.map(currentPage) {
        mainInteractor.getArticlesByPage(it)
    }
    val articles = Transformations.switchMap(repoResult) { it.pagedList }!!
    val networkState = Transformations.switchMap(repoResult) { it.networkState }!!
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }!!

    /* IMainPresenter */

    override fun setInteractor(context: Context) {
        mainInteractor.setModules(this, context)
    }

    override fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    override fun showArticlesByPage(page: Int): Boolean {
        if (currentPage.value == page) {
            return false
        }
        currentPage.value = page
        return true
    }

    override fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }

    override fun currentPageNumber(): Int? = currentPage.value

    override fun updatePageNumber() {
        currentPage.value = if (currentPage!!.value!! > 5) {
            1
        } else {
            currentPage!!.value!!.inc()

        }
    }
}