package com.example.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_full_article.toolbar

class FullArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_article)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
