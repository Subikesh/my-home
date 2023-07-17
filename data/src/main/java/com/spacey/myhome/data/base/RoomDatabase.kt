package com.spacey.myhome.data.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.spacey.myhome.data.expense.local.FieldDBEntity
import com.spacey.myhome.data.expense.local.MetadataLocalDataSource
import com.spacey.myhome.data.todo.local.TodoDBEntity
import com.spacey.myhome.data.todo.local.TodoLocalDataSource

@Database(entities = [TodoDBEntity::class, FieldDBEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoLocalDataSource(): TodoLocalDataSource
    abstract fun metadataLocalDataSource(): MetadataLocalDataSource

    companion object {
        fun createDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "my-home.db").fallbackToDestructiveMigration().build()
    }
}
