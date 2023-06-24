package com.spacey.myhome.todo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DIServiceLocator
import com.example.data.todo.TodoRepository
import com.example.data.todo.TodoResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {

    private val _todoUIState = MutableStateFlow<TodoUIState>(TodoUIState.Loading)
    val todoUIState: StateFlow<TodoUIState> = _todoUIState

    private val todoRepository: TodoRepository = DIServiceLocator.todoRepository

    fun fetchTodos() {
        viewModelScope.launch {
            try {
                _todoUIState.value = TodoUIState.Loading
                todoRepository.fetchTodos().collect { todoResult ->
                    Log.d("Todo", "Todo list fetched: $todoResult")
                    _todoUIState.value = todoResult.toUIState()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

private fun TodoResult<List<TodoEntity>>.toUIState(): TodoUIState =
    when (this) {
        is TodoResult.Success -> TodoUIState.Success(data)
        is TodoResult.Error -> TodoUIState.Error(
            errorMessage ?: throwable?.stackTraceToString() ?: "Somme error"
        )
    }