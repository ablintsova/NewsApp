package com.example.newsapp.model

import android.util.Log
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
import javax.net.ssl.*

class MainInteractor : IMainInteractor {

    private lateinit var mainPresenter: IMainPresenter
    private val tag = "MainInteractor"

    /* IMainInteractor */

    override fun setPresenter(presenter: IMainPresenter) {
        mainPresenter = presenter
    }

    override fun getArticles() {

        val retrofit = getRetrofitClient()
        val service = retrofit.create(NewsApiService::class.java)

        val call = service.getArticles("android",
            "2019-08-01",
            "publishedAt",
            1,
            "26eddb253e7840f988aec61f2ece2907")

        call.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                when (response.code()) {
                    200 -> {
                        Log.d(tag, response.message())
                        val articleList = response.body()?.articleList
                        if (articleList != null) {
                            onDataAvailable(articleList)
                        } else {
                            onError(Exception("Article list is empty"))
                        }
                    }
                    else -> onError(Exception(response.message()))

                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                onError(Exception(t.message))
            }
        })
    }

    override fun onDataAvailable(data: List<Article>) {
        mainPresenter.showArticles(data)
    }

    override fun onError(exception: Exception) {
        Log.e(tag, exception.message!!)
        mainPresenter.onError(exception)
    }

    companion object {

        fun getRetrofitClient(): Retrofit {
            val httpClient = getUnsafeOkHttpClient()
            return Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
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
}