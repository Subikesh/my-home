package com.example.data.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.todo.local.TodoDBEntity
import com.example.data.todo.local.TodoLocalDataSource

@Database(entities = [TodoDBEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoLocalDataSource(): TodoLocalDataSource

    companion object {
        fun createDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "my-home").fallbackToDestructiveMigration().build()
    }
}
