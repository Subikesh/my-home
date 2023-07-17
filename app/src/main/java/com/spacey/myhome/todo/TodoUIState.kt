package com.spacey.myhome.todo

import com.spacey.myhome.data.todo.TodoEntity

sealed class TodoUIState {
    object Loading : TodoUIState()
    data class Success(val data: List<TodoEntity>) : TodoUIState()
    data class Error(val errorMessage: String) : TodoUIState()
}