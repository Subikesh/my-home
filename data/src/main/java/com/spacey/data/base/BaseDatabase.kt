package com.spacey.data.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.spacey.data.service.DateRecurrence
import com.spacey.data.service.Expense
import com.spacey.data.service.ExpenseDao
import com.spacey.data.service.RecurrenceType
import com.spacey.data.service.Service
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

@Database(
    entities = [
        Expense::class,
        Service::class,
        DateRecurrence::class
    ], version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile var instance: AppDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: createDatabase(context).also { instance = it }
        }

        // TODO: Handle migration
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "my-home.db")
//                .addCallback(object : Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        CoroutineScope(Dispatchers.IO).launch {
//                            // TODO: Initial pre-population
//                        }
//                    }
//
//                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
//                        super.onDestructiveMigration(db)
//                        onCreate(db)
//                    }
//                })
                .fallbackToDestructiveMigration()
                .build()
    }
}

object DBConstant {
    const val SEPARATOR = "^_^"
}

enum class InputType(private val label: String) {
    AMOUNT("Amount"), COUNTER("Counter"), CHECKBOX("Checkbox");

    override fun toString(): String = label
}

class Converters {
    @TypeConverter
    fun dateToString(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun stringToDate(date: String?): LocalDate? = date?.let { LocalDate.parse(date, DateTimeFormatter.ISO_DATE) }

    @TypeConverter
    fun inputTypeToString(type: InputType?): String? = type?.name

    @TypeConverter
    fun stringToInputType(type: String?): InputType? = type?.let { InputType.valueOf(it) }

    @TypeConverter
    fun recurrenceToString(recurrence: RecurrenceType?): String? = recurrence?.asString()

    @TypeConverter
    fun stringToRecurrence(recurrence: String?): RecurrenceType? {
        if (recurrence == null) return null
        val values = recurrence.split(DBConstant.SEPARATOR)
        val (type, value) = values.first() to values.subList(1, values.size)
        return when (type) {
            RecurrenceType.THIS_MONTH -> RecurrenceType.OnlyThisMonth
            RecurrenceType.TODAY -> RecurrenceType.OnlyToday
            RecurrenceType.WEEKLY -> RecurrenceType.Weekly(value.map { DayOfWeek.valueOf(it) }.toSet())
            RecurrenceType.MONTHLY -> RecurrenceType.Monthly(value.map { Month.valueOf(it) }.toSet())
            else -> RecurrenceType.OnlyToday
        }
    }
}
