package com.example.newsapp.model.api

import com.example.newsapp.model.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/everything")
    fun getArticles(
        @Query("q") query: String,
        @Query("from") date: String,
        @Query("sortBy") sortBy: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("apiKey") apiKey: String) : Call<ArticleResponse>
}