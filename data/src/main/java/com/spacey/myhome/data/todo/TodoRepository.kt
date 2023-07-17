package com.spacey.myhome.data.todo

import com.spacey.myhome.data.ConnectivityHelper
import com.spacey.myhome.data.base.Data
import com.spacey.myhome.data.todo.local.TodoDBEntity
import com.spacey.myhome.data.todo.local.TodoLocalDataSource
import com.spacey.myhome.data.todo.remote.TodoRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TodoRepository(
    private val todoRemoteDataSource: TodoRemoteDataSource,
    private val todoLocalDataSource: TodoLocalDataSource,
    private val connectivityHelper: ConnectivityHelper
) {
    suspend fun fetchTodos(): Flow<Data<List<TodoEntity>>> {
        return flow {
            try {
                // From local db
                val todoData = getTodosFromDB()
                if (todoData is Data.Success) {
                    emit(todoData)
                }
                emit(Data.Success(todoLocalDataSource.getAllTodos()))

                if (connectivityHelper.isInternetConnected()) {
                    val todoList = todoRemoteDataSource.getTodoList()
                    todoLocalDataSource.insertAll(todoList.map { it.toDBEntity() })
                    emit(Data.Success(todoLocalDataSource.getAllTodos()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Data.Error(e))
            }

        }.flowOn(Dispatchers.IO)
    }

    private fun getTodosFromDB(): Data<List<TodoEntity>> =
        todoLocalDataSource.getAllTodos().let {
            if (it.isEmpty()) {
                Data.Error(errorMessage = "No Todos found")
            } else {
                Data.Success(it)
            }
        }

    private fun TodoEntity.toDBEntity(): TodoDBEntity =
        TodoDBEntity(id, title, isCompleted, createdTime)

}