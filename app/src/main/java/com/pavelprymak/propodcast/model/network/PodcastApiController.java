package com.pavelprymak.propodcast.model.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.moshi.Moshi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PodcastApiController {
    private static final PodcastApiController mInstance = new PodcastApiController();
    private PodcastApi podcastApi;

    public static PodcastApiController getInstance() {
        return mInstance;
    }

    private PodcastApiController() {
        if (podcastApi == null) {
            podcastApi = getApi();
        }
    }

    public PodcastApi getPodcastApi() {
        return podcastApi;
    }

    private PodcastApi getApi() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            //SAVE Cookie
            httpClient.cookieJar(new MyCookieJar());

            httpClient.connectTimeout(30, TimeUnit.SECONDS);
            httpClient.writeTimeout(30, TimeUnit.SECONDS);
            httpClient.readTimeout(30, TimeUnit.SECONDS);
            //Stetho logging
            httpClient.addNetworkInterceptor(new StethoInterceptor());
            //LOGIN TO CONSOLE
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            // add logging as last interceptor
            httpClient.addInterceptor(logging);
            //moshi protection
            Moshi moshi = new Moshi.Builder()
                    .build();
            //Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    //.addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient.build())
                    .build();
            return retrofit.create(PodcastApi.class);
        } catch (Exception e) {
            //ignore
        }
        return null;
    }
}
