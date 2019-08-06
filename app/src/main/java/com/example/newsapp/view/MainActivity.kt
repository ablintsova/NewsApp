package com.example.newsapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.*
import com.example.newsapp.application.NewsApplication
import com.example.newsapp.model.Article
import com.example.newsapp.model.network.NetworkState
import com.example.newsapp.presenter.MainPresenter
import com.example.newsapp.view.util.RecyclerViewAdapter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    IMainView {

    companion object {
        const val CURRENT_PAGE = "currentPage"
        const val DEFAULT_PAGE = 1
    }

    @Inject lateinit var mainPresenter: MainPresenter
    @Inject lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        (application as NewsApplication).newsComponent.inject(this)
        setRecyclerView()
        setSwipeContainer()

        mainPresenter.setInteractor(appContext)
        val page = savedInstanceState?.getInt(CURRENT_PAGE) ?: DEFAULT_PAGE
        mainPresenter.showArticlesByPage(page)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_PAGE, mainPresenter.currentPageNumber() ?: DEFAULT_PAGE)
    }

    private fun setRecyclerView() {
        val adapter = RecyclerViewAdapter {
            mainPresenter.retry()
        }

        val layoutManager = LinearLayoutManager(this)
        rvArticleList.layoutManager = layoutManager

        val itemDivider = DividerItemDecoration(rvArticleList.context, layoutManager.orientation)
        rvArticleList.addItemDecoration(itemDivider)
        rvArticleList.adapter = adapter

        mainPresenter.articles.observe(this, Observer<PagedList<Article>> {
            adapter.submitList(it)
        })
        mainPresenter.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    private fun setSwipeContainer() {
        mainPresenter.refreshState.observe(this, Observer {
            swipeContainer.isRefreshing = it == NetworkState.LOADING
        })
        swipeContainer.setOnRefreshListener {
            mainPresenter.refresh()
            swipeContainer.isRefreshing = false
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    /* IMainView */

    override fun showArticles(data: List<Article>) {
        /*if (swipeContainer.isRefreshing) {
            recyclerViewAdapter.clearArticleList()
            recyclerViewAdapter.rewriteArticleList(data)
            swipeContainer.isRefreshing = false
        } else {
            recyclerViewAdapter.addArticlesToEndOfList(data)
            isLoading = false
        }*/
    }

    override fun onError(exception: Exception) {
        swipeContainer.isRefreshing = false
        Toast.makeText(this, getString(R.string.article_list_error_message), Toast.LENGTH_LONG).show()
        Log.e("Main Activity", exception.message!!)
    }
}
