package com.example.newsapp.application

import android.app.Application
import com.example.newsapp.dagger.AppComponent
import com.example.newsapp.dagger.AppModule
import com.example.newsapp.dagger.DaggerAppComponent

class NewsApplication : Application() {

    lateinit var newsComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        newsComponent = initDagger(this)
    }

    private fun initDagger(app: NewsApplication): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()
}