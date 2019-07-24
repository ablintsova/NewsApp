package com.example.newsapp.model

import android.net.Uri
import android.util.Log
import com.example.newsapp.presenter.IMainPresenter
import java.lang.Exception

class MainInteractor(private val mainPresenter: IMainPresenter) :
    IMainInteractor,
    RawData.IDownloadComplete,
    JsonArticleData.IDataAvailable {

    /* IMainInteractor */

    override fun getArticles() {
        val url = createUri("https://newsapi.org/v2/everything",
            "android",
            "2019-07-22",
            "publishedAt",
            1)
        val rawData = RawData(this)
        rawData.execute(url)
    }

    private fun createUri(baseURL: String, query: String, date: String, sortBy: String, page: Int) : String {
        return Uri.parse(baseURL).
            buildUpon().
            appendQueryParameter("q", query).
            appendQueryParameter("from", date).
            appendQueryParameter("sortBy", sortBy).
            appendQueryParameter("apiKey", "26eddb253e7840f988aec61f2ece2907").
            appendQueryParameter("page", page.toString()).
            build().toString()
    }

    /* RawData.IDownloadComplete */

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            val jsonData = JsonArticleData(this)
            jsonData.execute(data)
        } else {
            Log.e("Main Activity", "onDownloadComplete failed with status $status. Error: $data")
            onError(Exception(data))
        }
    }

    /* JsonArticleData.IDataAvailable */

    override fun onDataAvailable(data: List<Article>) {
        mainPresenter.showArticles(data)
    }

    override fun onError(exception: Exception) {
        Log.e("Main Activity", exception.message!!)
        mainPresenter.onError(exception)
    }
}