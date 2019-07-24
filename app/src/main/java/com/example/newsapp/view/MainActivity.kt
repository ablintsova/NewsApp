package com.example.newsapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
    lateinit var mainPresenter: IMainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainPresenter = MainPresenter(this)
        setRecyclerView()

        mainPresenter.getArticles()
    }

    private fun setRecyclerView() {
        rvArticleList.layoutManager = LinearLayoutManager(this)
        rvArticleList.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                rvArticleList,
                this
            )
        )
        rvArticleList.adapter = recyclerViewAdapter
    }

    /* IMainView */
    override fun showArticles(data: List<Article>) {
        recyclerViewAdapter.updateArticleList(data)
    }

    override fun onError(exception: Exception) {
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
