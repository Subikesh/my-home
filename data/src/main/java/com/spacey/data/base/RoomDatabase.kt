package com.spacey.myhome.data.base

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ServiceDbEntity::class,
        RecurrenceType::class,
//        ExpenseDBEntity::class
    ], version = 0
)
abstract class AppDatabase : RoomDatabase() {
//    abstract fun metadataLocalDataSource(): MetadataLocalDataSource
//    abstract fun expenseLocalDataSource(): ExpenseLocalDataSource

    companion object {
        fun createDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "my-home.db")
                .fallbackToDestructiveMigration().build()
    }
}

@Entity(
    tableName = "Service",
    foreignKeys = [ForeignKey(RecurrenceType::class, ["id"], ["recurrence"])]
)
data class ServiceDbEntity(val type: String, val amount: Float, val recurrence: RecurrenceType)

@Entity(tableName = "Recurrence")
data class RecurrenceType(val value: String)