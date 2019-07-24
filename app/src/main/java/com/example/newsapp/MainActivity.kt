package com.example.newsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(),
    OnDownloadComplete,
    OnDataAvailable,
    OnRecyclerClickListener {

    private val recyclerViewAdapter = RecyclerViewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        rvArticleList.layoutManager = LinearLayoutManager(this)
        rvArticleList.addOnItemTouchListener(RecyclerItemClickListener(this, rvArticleList, this))
        rvArticleList.adapter = recyclerViewAdapter
        val url = createUri("https://newsapi.org/v2/everything",
                            "android",
                            "2019-07-22",
                            "publishedAt",
                            1)
        Log.d("Main Activity", "FINAL URI: $url")
        val rawData = RawData(this)
        rawData.execute(url)
    }

    private fun createUri(baseURL: String, searchCriteria: String, date: String, sortBy: String, page: Int) : String {
        Log.d("Main Activity", ".createUri starts")

        return Uri.parse(baseURL).
            buildUpon().
            appendQueryParameter("q", searchCriteria).
            appendQueryParameter("from", date).
            appendQueryParameter("sortBy", sortBy).
            appendQueryParameter("apiKey", "26eddb253e7840f988aec61f2ece2907").
            appendQueryParameter("page", page.toString()).
            build().toString()
    }
    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            val jsonData = JsonArticleData(this)
            jsonData.execute(data)
        } else {
            Log.e("Main Activity", "onDownloadComplete failed with status $status. Error: $data")
        }
    }

    override fun OnDataAvailable(data: List<Article>) {
        recyclerViewAdapter.updateArticleList(data)
    }

    override fun onError(exception: Exception) {
        Log.e("Main Activity", exception.message!!)
    }

    override fun onItemClick(view: View, position: Int) {
        val article = recyclerViewAdapter.getArticle(position)
        if (article != null) {
            val intent = Intent(this, FullArticleActivity::class.java)
            intent.putExtra("ArticleUrl", article.articleURL)
            startActivity(intent)
        }
    }
}
