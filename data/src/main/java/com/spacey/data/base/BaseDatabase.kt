package com.spacey.data.base

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.spacey.data.service.Expense
import com.spacey.data.service.ExpenseDao
import com.spacey.data.service.RecurrenceType
import com.spacey.data.service.Service
import com.spacey.data.service.ServiceDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

@Database(
    entities = [
        Expense::class,
        Service::class
    ], version = 0
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serviceDao(): ServiceDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile var instance: AppDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: createDatabase(context).also { instance = it }
        }

        // TODO: Handle migration
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "my-home.db")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            // TODO: Initial pre-population
                        }
                    }

                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                        onCreate(db)
                    }
                })
                .fallbackToDestructiveMigration()
                .build()

    }
}

@Dao
interface BaseDao<T> {
    @Insert
    suspend fun insertAll(data: List<T>)
    suspend fun insert(data: T) = insertAll(listOf(data))
}

object ServiceCol {
    const val TYPE = "type"
    const val AMOUNT = "amount"
}

object DBConstant {
    const val SEPARATOR = "^_^"
}

enum class InputType {
    CHECKLIST, AMOUNT, COUNTER, CHECKBOX
}

class Converters {
    @TypeConverter
    fun dateToString(date: LocalDate): String = date.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun stringToDate(date: String): LocalDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)

    @TypeConverter
    fun inputTypeToString(type: InputType): String = type.name

    @TypeConverter
    fun stringToInputType(type: String): InputType = InputType.valueOf(type)

    @TypeConverter
    fun recurrenceToString(recurrence: RecurrenceType): String = recurrence.toString()

    @TypeConverter
    fun stringToRecurrence(recurrence: String): RecurrenceType {
        val values = recurrence.split(DBConstant.SEPARATOR)
        val (type, value) = values.first() to values.subList(1, values.size)
        return when (type) {
            RecurrenceType.THIS_MONTH -> RecurrenceType.OnlyThisMonth
            RecurrenceType.TODAY -> RecurrenceType.OnlyToday
            RecurrenceType.WEEKLY -> RecurrenceType.Weekly(value.map { DayOfWeek.valueOf(it) })
            RecurrenceType.MONTHLY -> RecurrenceType.Monthly(value.map { Month.valueOf(it) })
            else -> RecurrenceType.OnlyToday
        }
    }
}
