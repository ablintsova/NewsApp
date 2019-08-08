package com.example.newsapp.model.interactor

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.room.Room
import com.example.newsapp.model.Article
import com.example.newsapp.model.ArticleResponse
import com.example.newsapp.model.ArticlesBoundaryCallback
import com.example.newsapp.model.Listing
import com.example.newsapp.model.api.NewsApi
import com.example.newsapp.model.network.NetworkState
import com.example.newsapp.model.database.ArticlesDao

import com.example.newsapp.model.database.ArticlesDatabase
import com.example.newsapp.model.util.QueryConstants
import com.example.newsapp.presenter.IMainPresenter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.lang.Exception

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.Executors
import javax.inject.Singleton
import javax.net.ssl.*

class MainInteractor : IMainInteractor {

    private lateinit var mainPresenter: IMainPresenter
    private lateinit var applicationContext: Context
    @Singleton
    private lateinit var database: ArticlesDatabase
    private lateinit var newsApi: NewsApi
    private lateinit var dao: ArticlesDao

    private val queryConstants = QueryConstants
    private val ioExecutor = Executors.newSingleThreadExecutor()

    private val tag = "MainInteractor"

    /* IMainInteractor */

    override fun setModules(presenter: IMainPresenter, context: Context) {
        mainPresenter = presenter
        applicationContext = context

        newsApi = getRetrofitClient().create(NewsApi::class.java)
        database = Room.databaseBuilder(
            applicationContext,
            ArticlesDatabase::class.java, "database"
        ).build()
        dao = database.articles()
    }

    override fun getArticlesByPage(page: Int): Listing<Article> {
        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = ArticlesBoundaryCallback(
            webservice = newsApi,
            page = page,
            handleResponse = this::insertResultIntoDb,
            ioExecutor = ioExecutor
        )
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh(page)
        }

        val livePagedList = LivePagedListBuilder(dao.getArticlesOnPage(page), queryConstants.PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return Listing(
            pagedList = livePagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    /* Private methods */

    private fun insertResultIntoDb(page: Int, body: ArticleResponse?) {
        database.runInTransaction {
            val items: List<Article> = body!!.articleList!!
            items.forEach { article ->
                article.page = page
            }
            dao.insert(items)
        }
    }

    @MainThread
    private fun refresh(page: Int): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        val call = newsApi.getArticles(
            queryConstants.QUERY,
            queryConstants.DATE,
            queryConstants.SORTING_TYPE,
            page,
            queryConstants.PAGE_SIZE,
            queryConstants.API_KEY
        )
        call.enqueue(object : Callback<ArticleResponse> {
            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                // retrofit calls this on main thread so safe to call set value
                networkState.value = NetworkState.error(t.message)
            }

            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                ioExecutor.execute {
                    database.runInTransaction {
                        dao.deleteArticlesOnPage(page)
                        insertResultIntoDb(page, response.body())
                    }
                    // since we are in bg thread now, post the result.
                    networkState.postValue(NetworkState.LOADED)
                }
                mainPresenter.updatePageNumber()
            }
        }
        )
        return networkState
    }


    private fun getRetrofitClient(): Retrofit {
        val httpClient = getUnsafeOkHttpClient()
        return Retrofit.Builder()
            .baseUrl(queryConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    // Unsafe workaround for HTTPS and API <20
    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            // Using logging to see the full api request
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .addInterceptor(logging)
                .hostnameVerifier(object : HostnameVerifier {
                    override fun verify(hostname: String, session: SSLSession): Boolean {
                        return true
                    }
                })
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}