package com.example.newsapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapp.*
import com.example.newsapp.model.*
import com.example.newsapp.presenter.IMainPresenter
import com.example.newsapp.presenter.MainPresenter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(),
    IMainView,
    RecyclerItemClickListener.IRecyclerClickListener {

    private val recyclerViewAdapter = RecyclerViewAdapter(ArrayList())
    lateinit var swipeContainer: SwipeRefreshLayout
    lateinit var mainPresenter: IMainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainPresenter = MainPresenter(this)
        setRecyclerView()
        setSwipeContainer()
        mainPresenter.getArticles()
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        rvArticleList.layoutManager = layoutManager
        rvArticleList.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                rvArticleList,
                this
            )
        )
        val itemDivider = DividerItemDecoration(rvArticleList.context, layoutManager.orientation)
        rvArticleList.addItemDecoration(itemDivider)
        rvArticleList.adapter = recyclerViewAdapter
    }

    private fun setSwipeContainer() {
        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            mainPresenter.getArticles()
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
        recyclerViewAdapter.clearArticleList()
        recyclerViewAdapter.updateArticleList(data)
        swipeContainer.isRefreshing = false
    }

    override fun onError(exception: Exception) {
        swipeContainer.isRefreshing = false
        Toast.makeText(this, getString(R.string.article_list_error_message), Toast.LENGTH_LONG).show()
        Log.e("Main Activity", exception.message!!)
    }

    /* RecyclerItemClickListener.IRecyclerClickListener */

    override fun onItemClick(view: View, position: Int) {
        val article = recyclerViewAdapter.getArticle(position)
        if (article != null) {
            val intent = Intent(this, FullArticleActivity::class.java)
            intent.putExtra("ArticleUrl", article.articleURL)
            startActivity(intent)
        }
    }
}
