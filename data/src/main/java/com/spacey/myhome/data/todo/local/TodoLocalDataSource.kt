package com.spacey.myhome.data.todo.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoLocalDataSource {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllTodos(): List<TodoDBEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(todoList: List<TodoDBEntity>)

    @Delete
    fun delete(todo: TodoDBEntity)

    companion object {
        private const val TABLE_NAME = "Todo"
    }
}