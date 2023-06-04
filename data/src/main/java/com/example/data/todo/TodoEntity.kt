package com.example.data.todo

// Domain Result
sealed class TodoResult {
    object Loading : TodoResult()
    sealed class TodoData : TodoResult() {
        data class Success(val data: List<TodoEntity>) : TodoData()
        data class Error(val throwable: Throwable? = null, val errorMessage: String? = null) : TodoData()
    }
}

// Data entity
data class TodoEntity(val title: String, val isCompleted: Boolean, val createdTime: Long)
