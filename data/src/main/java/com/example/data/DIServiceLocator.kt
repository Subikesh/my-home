package com.example.data

import android.content.Context
import com.example.data.base.RetrofitClient
import com.example.data.todo.TodoRemoteDataSource
import com.example.data.todo.TodoRepository

object DIServiceLocator {

    private lateinit var connectivityHelper: ConnectivityHelper

    // This function should be initially called before getting any other dependencies
    fun initializeApp(context: Context) {
        connectivityHelper = ConnectivityHelper(context)
    }

    val repository: TodoRepository by lazy { TodoRepository(todoRemoteDataSource, connectivityHelper) }

    private val todoRemoteDataSource: TodoRemoteDataSource by lazy { RetrofitClient.retrofit.create(TodoRemoteDataSource::class.java) }
}