package com.spacey.myhome.data.todo

import com.google.gson.annotations.SerializedName

open class TodoEntity(
    @SerializedName(TodoNetworkKeyConstants.ID) open val id: Int,
    @SerializedName(TodoNetworkKeyConstants.TITLE) open val title: String,
    @SerializedName(TodoNetworkKeyConstants.IS_DONE) open val isCompleted: Boolean,
    @SerializedName(TodoNetworkKeyConstants.CREATED_TIME) open val createdTime: String
)

