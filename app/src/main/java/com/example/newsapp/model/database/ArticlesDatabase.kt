package com.example.newsapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.model.Article

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
abstract class ArticlesDatabase : RoomDatabase() {
    abstract fun articles(): ArticlesDao
}