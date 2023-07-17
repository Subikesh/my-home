package com.spacey.myhome.data

import android.content.Context
import com.spacey.myhome.data.base.AppDatabase
import com.spacey.myhome.data.base.RetrofitClient
import com.spacey.myhome.data.expense.ExpenseRepository
import com.spacey.myhome.data.expense.MetadataRepository
import com.spacey.myhome.data.expense.local.MetadataLocalDataSource
import com.spacey.myhome.data.expense.remote.MetadataRemoteDataSource
import com.spacey.myhome.data.todo.TodoRepository
import com.spacey.myhome.data.todo.local.TodoLocalDataSource
import com.spacey.myhome.data.todo.remote.TodoRemoteDataSource

object DIServiceLocator {

    private lateinit var connectivityHelper: ConnectivityHelper
    private lateinit var roomDatabase: AppDatabase

    // This function should be initially called before getting any other dependencies
    fun initializeApp(context: Context) {
        connectivityHelper = ConnectivityHelper(context)
        roomDatabase = AppDatabase.createDatabase(context)
    }

    val metadataRepository: MetadataRepository by lazy {
        MetadataRepository(
            metadataRemoteDataSource,
            metadataLocalDatabase,
            connectivityHelper
        )
    }
    val expenseRepository: ExpenseRepository by lazy {
        ExpenseRepository(metadataRepository, connectivityHelper)
    }
    val todoRepository: TodoRepository by lazy {
        TodoRepository(
            todoRemoteDataSource,
            todoLocalDataSource,
            connectivityHelper
        )
    }

    private val metadataRemoteDataSource: MetadataRemoteDataSource by lazy {
        RetrofitClient.retrofit.create(MetadataRemoteDataSource::class.java)
    }

    private val todoRemoteDataSource: TodoRemoteDataSource by lazy {
        RetrofitClient.retrofit.create(TodoRemoteDataSource::class.java)
    }

    private val metadataLocalDatabase: MetadataLocalDataSource by lazy {
        roomDatabase.metadataLocalDataSource()
    }

    private val todoLocalDataSource: TodoLocalDataSource by lazy {
        roomDatabase.todoLocalDataSource()
    }
}