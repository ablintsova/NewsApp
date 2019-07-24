package com.example.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_full_article.toolbar
import kotlinx.android.synthetic.main.content_full_article.*

class FullArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_article)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val articleUrl = intent.getStringExtra("ArticleUrl")
        wvArticle.loadUrl(articleUrl)
    }
}
