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
open class TodoEntity(
    @SerializedName(TodoNetworkKeyConstants.ID) open val id: Int,
    @SerializedName(TodoNetworkKeyConstants.TITLE) open val title: String,
    @SerializedName(TodoNetworkKeyConstants.IS_DONE) open val isCompleted: Boolean,
    @SerializedName(TodoNetworkKeyConstants.CREATED_TIME) open val createdTime: String
)

