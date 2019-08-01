package com.example.newsapp.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/everything")
    fun getArticles(
        @Query("q") query: String,
        @Query("from") date: String,
        @Query("sortBy") sortBy: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String) : Call<ArticleResponse>

}