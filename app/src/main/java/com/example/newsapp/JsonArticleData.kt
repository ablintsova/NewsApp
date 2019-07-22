package com.example.newsapp

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.lang.Exception

interface OnDataAvailable {
    fun OnDataAvailable(data: List<Article>)
    fun onError(exception: Exception)
}
class JsonArticleData(private val listener: OnDataAvailable) : AsyncTask<String, Void, ArrayList<Article>>() {

    override fun onPostExecute(result: ArrayList<Article>) {
        super.onPostExecute(result)
        Log.d("Json Article Data", "onPostExecute result: $result")
        listener.OnDataAvailable(result)
    }

    override fun doInBackground(vararg params: String?): ArrayList<Article> {
        val articleList = ArrayList<Article>()

        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("articles")

            for (i in 0 until itemsArray.length()) {
                val jsonArticle = itemsArray.getJSONObject(i)

                val title = jsonArticle.getString("title")
                val date = jsonArticle.getString("publishedAt")
                val description = jsonArticle.getString("description")
                val articleURL = jsonArticle.getString("url")
                val imageURL = jsonArticle.getString("urlToImage")

                val articleObject = Article(title, date, description, articleURL, imageURL)

                articleList.add(articleObject)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("JsonArticleData", "doInBackground: Error processing JSON data - ${e.message}")

            listener.onError(e)
        }

        return articleList
    }
}