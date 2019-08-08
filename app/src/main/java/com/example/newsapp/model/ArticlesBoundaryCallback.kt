package com.example.newsapp.model

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.newsapp.model.api.NewsApi
import com.example.newsapp.model.util.PagingRequestHelper
import com.example.newsapp.model.util.QueryConstants
import com.example.newsapp.model.util.createStatusLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class ArticlesBoundaryCallback(
    private val webservice: NewsApi,
    private val handleResponse: (Int, ArticleResponse?) -> Unit,
    private val ioExecutor: Executor,
    private val page: Int)
    : PagedList.BoundaryCallback<Article>() {

    private val queryConstants = QueryConstants
    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            webservice.getArticles(
                page = page,
                pageSize = queryConstants.PAGE_SIZE,
                date = queryConstants.DATE,
                sortBy = queryConstants.SORTING_TYPE,
                apiKey = queryConstants.API_KEY,
                query = queryConstants.QUERY
                ).enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: Article) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            webservice.getArticles(
                page = page,
                pageSize = queryConstants.PAGE_SIZE,
                date = queryConstants.DATE,
                sortBy = queryConstants.SORTING_TYPE,
                apiKey = queryConstants.API_KEY,
                query = queryConstants.QUERY
            ).enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */

    private fun insertItemsIntoDb(page: Int,
                                  response: Response<ArticleResponse>,
                                  it: PagingRequestHelper.Request.Callback) {
        ioExecutor.execute {
            handleResponse(page,response.body())
            it.recordSuccess()
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Article) {
        // ignored, since we only ever append to what's in the DB
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<ArticleResponse> {
        return object : Callback<ArticleResponse> {
            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                it.recordFailure(t)
            }

            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>) {
                insertItemsIntoDb(page,response, it)
            }
        }
    }
}
