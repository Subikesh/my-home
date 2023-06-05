package com.example.data

import android.content.Context
import com.example.data.base.AppDatabase
import com.example.data.base.RetrofitClient
import com.example.data.todo.TodoRepository
import com.example.data.todo.local.TodoLocalDataSource
import com.example.data.todo.remote.TodoRemoteDataSource

object DIServiceLocator {

    private lateinit var connectivityHelper: ConnectivityHelper
    private lateinit var roomDatabase: AppDatabase

    // This function should be initially called before getting any other dependencies
    fun initializeApp(context: Context) {
        connectivityHelper = ConnectivityHelper(context)
        roomDatabase = AppDatabase.createDatabase(context)
    }

    val todoRepository: TodoRepository by lazy { TodoRepository(todoRemoteDataSource, todoLocalDataSource, connectivityHelper) }

    private val todoRemoteDataSource: TodoRemoteDataSource by lazy {
        RetrofitClient.retrofit.create(TodoRemoteDataSource::class.java)
    }

    private val todoLocalDataSource: TodoLocalDataSource by lazy {
        roomDatabase.todoLocalDataSource()
    }
}