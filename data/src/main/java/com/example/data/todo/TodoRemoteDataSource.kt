package com.example.data.todo

import retrofit2.http.GET

interface TodoRemoteDataSource {
    @GET("HXBJ")
    suspend fun getTodoList(): List<TodoEntity>
}
