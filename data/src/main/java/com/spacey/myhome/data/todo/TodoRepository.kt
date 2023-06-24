package com.example.data.todo

import com.example.data.ConnectivityHelper
import com.example.data.todo.local.TodoDBEntity
import com.example.data.todo.local.TodoLocalDataSource
import com.example.data.todo.remote.TodoRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TodoRepository(
    private val todoRemoteDataSource: TodoRemoteDataSource,
    private val todoLocalDataSource: TodoLocalDataSource,
    private val connectivityHelper: ConnectivityHelper
) {
    suspend fun fetchTodos(): Flow<TodoResult<List<TodoEntity>>> {
        return flow {
            try {
                // From local db
                val todoData = getTodosFromDB()
                if (todoData is TodoResult.Success) {
                    emit(todoData)
                }
                emit(TodoResult.Success(todoLocalDataSource.getAllTodos()))

                if (connectivityHelper.isInternetConnected()) {
                    val todoList = todoRemoteDataSource.getTodoList()
                    todoLocalDataSource.insertAll(todoList.map { it.toDBEntity() })
                    emit(TodoResult.Success(todoLocalDataSource.getAllTodos()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(TodoResult.Error(e))
            }

        }.flowOn(Dispatchers.IO)
    }

    private fun getTodosFromDB(): TodoResult<List<TodoEntity>> =
        todoLocalDataSource.getAllTodos().let {
            if (it.isEmpty()) {
                TodoResult.Error(errorMessage = "No Todos found")
            } else {
                TodoResult.Success(it)
            }
        }

    private fun TodoEntity.toDBEntity(): TodoDBEntity =
        TodoDBEntity(id, title, isCompleted, createdTime)

}