package com.spacey.myhome.data.expense.remote

import com.spacey.myhome.data.expense.MetadataEntity
import retrofit2.http.GET

interface MetadataRemoteDataSource {

    @GET("c4c70e03bdd90b3a8825")
    suspend fun getMetadata(): MetadataEntity
}