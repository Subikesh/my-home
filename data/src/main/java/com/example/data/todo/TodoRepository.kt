package com.example.data.todo

import android.util.Log
import com.example.data.ConnectivityHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TodoRepository(
    private val todoRemoteDataSource: TodoRemoteDataSource,
    private val connectivityHelper: ConnectivityHelper
) {
    suspend fun fetchTodos(): Flow<TodoResult> {
        return flow {
            emit(TodoResult.Loading)
            // From local db
//            emit(TodoLocalDataSource.getTodoList())

            if (connectivityHelper.isInternetConnected()) {
                try {
                    val todoList = todoRemoteDataSource.getTodoList()
                    Log.d("Todo", "Todo data: $todoList")
                    emit(TodoResult.TodoData.Success(todoList))
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(TodoResult.TodoData.Error(e))
                }
            }

        }.flowOn(Dispatchers.IO)
    }

}