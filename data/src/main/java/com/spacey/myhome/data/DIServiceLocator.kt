package com.spacey.myhome.data

import android.content.Context
import com.spacey.myhome.data.base.AppDatabase
import com.spacey.myhome.data.base.RetrofitClient
import com.spacey.myhome.data.expense.ExpenseLocalDataSource
import com.spacey.myhome.data.expense.ExpenseRemoteDataSource
import com.spacey.myhome.data.expense.ExpenseRepository
import com.spacey.myhome.data.metadata.MetadataRepository
import com.spacey.myhome.data.metadata.MetadataLocalDataSource
import com.spacey.myhome.data.metadata.MetadataRemoteDataSource
import com.spacey.myhome.data.todo.TodoRepository
import com.spacey.myhome.data.todo.local.TodoLocalDataSource
import com.spacey.myhome.data.todo.remote.TodoRemoteDataSource
import retrofit2.create

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
        ExpenseRepository(connectivityHelper, expenseLocalDataSource, expenseRemoteDataSource)
    }
    val todoRepository: TodoRepository by lazy {
        TodoRepository(
            todoRemoteDataSource,
            todoLocalDataSource,
            connectivityHelper
        )
    }

    private val metadataRemoteDataSource: MetadataRemoteDataSource by getRetrofitService()
    private val todoRemoteDataSource: TodoRemoteDataSource by getRetrofitService()
    private val expenseRemoteDataSource: ExpenseRemoteDataSource by getRetrofitService()

    private inline fun <reified T> getRetrofitService() = lazy { RetrofitClient.retrofit.create<T>() }

    private val metadataLocalDatabase: MetadataLocalDataSource by lazy {
        roomDatabase.metadataLocalDataSource()
    }

    private val todoLocalDataSource: TodoLocalDataSource by lazy {
        roomDatabase.todoLocalDataSource()
    }

    private val expenseLocalDataSource: ExpenseLocalDataSource by lazy {
        roomDatabase.expenseLocalDataSource()
    }
}