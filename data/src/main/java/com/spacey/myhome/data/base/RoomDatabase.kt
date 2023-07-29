package com.spacey.myhome.data.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.spacey.myhome.data.expense.ExpenseDBEntity
import com.spacey.myhome.data.expense.ExpenseLocalDataSource
import com.spacey.myhome.data.metadata.FieldDBEntity
import com.spacey.myhome.data.metadata.MetadataLocalDataSource
import com.spacey.myhome.data.todo.local.TodoDBEntity
import com.spacey.myhome.data.todo.local.TodoLocalDataSource

@Database(entities = [TodoDBEntity::class, FieldDBEntity::class, ExpenseDBEntity::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoLocalDataSource(): TodoLocalDataSource
    abstract fun metadataLocalDataSource(): MetadataLocalDataSource
    abstract fun expenseLocalDataSource(): ExpenseLocalDataSource

    companion object {
        fun createDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "my-home.db").fallbackToDestructiveMigration().build()
    }
}
