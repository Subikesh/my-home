package com.spacey.myhome.data.network

import com.google.gson.JsonObject
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth-token")
    suspend fun userAuth(
        @Field("username") userName: String,
        @Field("password") password: String
    ): JsonObject
}