package com.example.data.todo

import com.google.gson.annotations.SerializedName

// Domain Result
sealed class TodoResult<out T> {
    data class Success(val data: List<TodoEntity>) : TodoResult<List<TodoEntity>>()
    data class Error(val throwable: Throwable? = null, val errorMessage: String? = null) : TodoResult<Nothing>()
}

// Data entity
open class TodoEntity(
    @SerializedName(TodoNetworkKeyConstants.ID) open val id: Int,
    @SerializedName(TodoNetworkKeyConstants.TITLE) open val title: String,
    @SerializedName(TodoNetworkKeyConstants.IS_DONE) open val isCompleted: Boolean,
    @SerializedName(TodoNetworkKeyConstants.CREATED_TIME) open val createdTime: String
)

