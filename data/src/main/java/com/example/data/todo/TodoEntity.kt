package com.example.data.todo

import com.google.gson.annotations.SerializedName

// Domain Result
sealed class TodoResult {
    object Loading : TodoResult()
    sealed class TodoData : TodoResult() {
        data class Success(val data: List<TodoEntity>) : TodoData()
        data class Error(val throwable: Throwable? = null, val errorMessage: String? = null) :
            TodoData()
    }
}

// Data entity
data class TodoEntity(
    @SerializedName(TodoNetworkKeyConstants.TITLE) val title: String,
    @SerializedName(TodoNetworkKeyConstants.IS_DONE) val isCompleted: Boolean,
    @SerializedName(TodoNetworkKeyConstants.CREATED_TIME) val createdTime: String
)
