package com.example.data.todo.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.todo.TodoDBKeyConstants
import com.example.data.todo.TodoEntity

@Entity(tableName = "Todo")
data class TodoDBEntity(
    @PrimaryKey override val id: Int,
    @ColumnInfo(name = TodoDBKeyConstants.TITLE) override val title: String,
    @ColumnInfo(name = TodoDBKeyConstants.IS_DONE) override val isCompleted: Boolean,
    @ColumnInfo(name = TodoDBKeyConstants.CREATED_TIME) override val createdTime: String
) : TodoEntity(id, title, isCompleted, createdTime)

