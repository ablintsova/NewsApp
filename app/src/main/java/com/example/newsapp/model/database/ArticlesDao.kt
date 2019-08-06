package com.example.newsapp.model.database

import androidx.room.*
import com.example.newsapp.model.Article
import androidx.paging.DataSource

@Dao
interface ArticlesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleList: List<Article>)

    @Query("SELECT * FROM articles WHERE page = :page")
    fun getArticlesOnPage(page: Int): DataSource.Factory<Int, Article>

    @Query("DELETE FROM articles WHERE page = :page")
    fun deleteArticlesOnPage(page: Int)
}