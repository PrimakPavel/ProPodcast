package com.pavelprymak.propodcast.model.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

import java.util.concurrent.TimeUnit

object PodcastApiController {
    var podcastApi: PodcastApi? = null
    init {
        if (podcastApi == null) {
            podcastApi = api
        }
    }

    private val api: PodcastApi?
        get() {
            try {
                val httpClient = OkHttpClient.Builder()
                httpClient.cookieJar(MyCookieJar())
                httpClient.connectTimeout(30, TimeUnit.SECONDS)
                httpClient.writeTimeout(30, TimeUnit.SECONDS)
                httpClient.readTimeout(30, TimeUnit.SECONDS)
                httpClient.addNetworkInterceptor(StethoInterceptor())
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(logging)
                val moshi = Moshi.Builder()
                    .build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient.build())
                    .build()
                return retrofit.create(PodcastApi::class.java)
            } catch (e: Exception) {
            }

            return null
        }
}
