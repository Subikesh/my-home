package com.spacey.myhome.data.network

import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

class RetrofitService {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
    }

    private val authApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    companion object {
        private const val BASE_URL = "http://127.0.0.1:8000/api/"
    }
}