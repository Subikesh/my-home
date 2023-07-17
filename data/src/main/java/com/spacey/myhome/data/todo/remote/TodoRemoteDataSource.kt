package com.spacey.myhome.data.todo.remote

import com.spacey.myhome.data.todo.TodoEntity
import retrofit2.http.GET

interface TodoRemoteDataSource {
    @GET("bbdf34a4b91af425c896")
    suspend fun getTodoList(): List<TodoEntity>
}
