package com.example.newsapp.model

import android.net.Uri
import android.util.Log
import com.example.newsapp.presenter.IMainPresenter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.lang.Exception

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainInteractor(private val mainPresenter: IMainPresenter) :
    IMainInteractor {

    private val tag = "MainInteractor"

    /* IMainInteractor */

    override fun getArticles() {

        // Using logging to see the full api request
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        val service = retrofit.create(NewsApiService::class.java)
        val call = service.getArticles("android",
            "2019-08-01",
            "publishedAt",
            1,
            "26eddb253e7840f988aec61f2ece2907")

        call.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                when (response.code()) {
                    200 -> {
                        Log.d(tag, response.message())
                        val articleList = response.body()?.articleList
                        if (articleList != null) {
                            onDataAvailable(articleList)
                        } else {
                            onError(Exception("Article list is empty"))
                        }
                    }
                    else -> onError(Exception(response.message()))

                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                onError(Exception(t.message))
            }
        })
    }

    override fun onDataAvailable(data: List<Article>) {
        mainPresenter.showArticles(data)
    }

    override fun onError(exception: Exception) {
        Log.e(tag, exception.message!!)
        mainPresenter.onError(exception)
    }
}