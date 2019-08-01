package com.example.newsapp.model

import com.google.gson.annotations.SerializedName

class Article {

    @SerializedName("title")
    var title: String = ""

    @SerializedName("publishedAt")
    var date: String = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("url")
    var articleURL: String = ""

    @SerializedName("urlToImage")
    var imageURL: String = ""
}

class ArticleResponse {

    @SerializedName("articles")
    var articleList: List<Article>? = null
}
