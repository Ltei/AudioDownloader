package com.ltei.audiodownloader.web

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

object WebGlobals {

    val defaultClient: OkHttpClient = OkHttpClient.Builder()
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BASIC
//        })
        .readTimeout(Duration.ofSeconds(60))
        .writeTimeout(Duration.ofSeconds(60))
        .build()

    val scrapingClient: OkHttpClient = defaultClient.newBuilder()
        .addInterceptor {
            it.proceed(it.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .build())
        }.build()

    private fun buildRetrofit(baseUrl: String, client: OkHttpClient = defaultClient): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .baseUrl(baseUrl)
        .build()

    inline fun <reified T> buildRetrofitClient(baseUrl: String, client: OkHttpClient = defaultClient, gson: Gson = Gson()): T = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(baseUrl)
        .build()
        .create(T::class.java)

}