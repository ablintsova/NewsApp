package com.example.newsapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "articles")
data class Article(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @SerializedName("title")
    var title: String,

    @SerializedName("publishedAt")
    var date: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("url")
    var articleURL: String,

    @ColumnInfo(name = "page")
    var page: Int,

    @SerializedName("urlToImage")
    var imageURL: String? = ""
)

class ArticleResponse {

    @SerializedName("articles")
    var articleList: List<Article>? = null
}
