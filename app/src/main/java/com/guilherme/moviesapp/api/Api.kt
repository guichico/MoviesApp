package com.guilherme.moviesapp.api

import com.google.gson.GsonBuilder
import com.guilherme.moviesapp.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Api {

    companion object {

        val retrofit: Retrofit by lazy {
            var okHttpClient = OkHttpClient.Builder()
                .addInterceptor {
                    var original = it.request()
                    val originalHttpUrl = original.url()

                    var url = originalHttpUrl
                        .newBuilder()
                        .addQueryParameter("api_key", Constants.api_key)
                        .build()

                    var request = original
                        .newBuilder()
                        .url(url)
                        .build()

                    it.proceed(request)
                }
                .build()

            Retrofit.Builder()
                .baseUrl(Constants.api_path)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(okHttpClient)
                .build()
        }
    }
}