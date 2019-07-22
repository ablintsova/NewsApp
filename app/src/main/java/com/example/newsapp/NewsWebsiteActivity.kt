package com.example.newsapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.activity_news_website.*
import kotlinx.android.synthetic.main.activity_news_website.toolbar

class NewsWebsiteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_website)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
