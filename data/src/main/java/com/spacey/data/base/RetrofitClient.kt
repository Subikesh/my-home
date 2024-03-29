package com.spacey.data.base

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Deprecated("Not in use")
internal object RetrofitClient {
    private const val BASE_URL = "https://api.npoint.io/"
//    private const val BASE_URL = "http://192.168.1.7:8000/myhome/"

    val retrofit: Retrofit by lazy {
        val httpClient = OkHttpClient.Builder().hostnameVerifier { _, _ -> true }.build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}