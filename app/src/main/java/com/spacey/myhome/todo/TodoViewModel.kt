package com.spacey.myhome.todo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DIServiceLocator
import com.example.data.todo.TodoEntity
import com.example.data.todo.TodoRepository
import com.example.data.todo.TodoResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewModel : ViewModel() {

    private val _todoUIState = MutableStateFlow<List<TodoUIState>>(emptyList())
    val todoUIState: StateFlow<List<TodoUIState>> = _todoUIState

    private val todoRepository: TodoRepository = DIServiceLocator.repository

    fun fetchTodos() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    todoRepository.fetchTodos().collect { todoResult ->
                        Log.d("Todo", "Todo list fetched: $todoResult")

                        when (todoResult) {
                            is TodoResult.Loading -> {
                                updateTodoMessage("Todos are loading")
                            }
                            is TodoResult.TodoData.Success -> {
                                updateTodos(todoResult.data)
                            }
                            is TodoResult.TodoData.Error -> {
                                updateTodoMessage(todoResult.errorMessage ?: todoResult.throwable?.stackTraceToString() ?: "Somme error")
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateTodoMessage(todoMessage: String) {
        updateTodos(listOf(TodoEntity(todoMessage, false, 0)))
    }

    private fun updateTodos(todoResult: List<TodoEntity>) {
        val newTodoList = todoResult.map {
            TodoUIState(it.title, it.isCompleted)
        }
        if (todoUIState.value != newTodoList) {
            _todoUIState.value = newTodoList
        }
    }
}