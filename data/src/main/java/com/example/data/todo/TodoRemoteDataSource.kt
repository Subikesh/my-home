package com.example.data.todo

import retrofit2.http.GET

interface TodoRemoteDataSource {
    @GET("bbdf34a4b91af425c896")
    suspend fun getTodoList(): List<TodoEntity>
}
