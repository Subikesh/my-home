package com.spacey.myhome.todo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacey.myhome.data.DIServiceLocator
import com.spacey.myhome.data.base.Data
import com.spacey.myhome.data.todo.TodoEntity
import com.spacey.myhome.data.todo.TodoRepository
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

private fun Data<List<TodoEntity>>.toUIState(): TodoUIState =
    when (this) {
        is Data.Success -> TodoUIState.Success(data)
        is Data.Error -> TodoUIState.Error(
            errorMessage ?: exception?.stackTraceToString() ?: "Somme error"
        )
    }