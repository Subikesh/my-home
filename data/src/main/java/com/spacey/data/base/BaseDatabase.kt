package com.spacey.data.base

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.spacey.data.AppComponent
import com.spacey.data.service.DateRecurrence
import com.spacey.data.service.Expense
import com.spacey.data.service.ServiceDao
import com.spacey.data.service.Service
import com.spacey.data.service.ServiceRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors

@Database(
    entities = [
        Expense::class,
        Service::class,
        ServiceRegistry::class,
        DateRecurrence::class
    ], version = 8
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serviceDao(): ServiceDao

    companion object {
        @Volatile
        var instance: AppDatabase? = null

        private val initialServices = listOf(Service("Milk", InputType.COUNTER), Service("Gas", InputType.CHECKBOX))

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: createDatabase(context).also { instance = it }
        }

        // TODO: Handle migration
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "my-home.db")
                .setQueryCallback({ query, args -> Log.d("Query", "DB Query: $query, $args") }, Executors.newSingleThreadExecutor())
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            AppComponent.expenseDao.insertService(initialServices)
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

enum class InputType(private val label: String) {
    AMOUNT("Amount"), COUNTER("Counter"), CHECKBOX("Checkbox");

    override fun toString(): String = label
}

class Converters {
    @TypeConverter
    fun dateToString(date: LocalDate?): Int? = date?.format(DateTimeFormatter.BASIC_ISO_DATE)?.toInt()

    @TypeConverter
    fun stringToDate(date: Int?): LocalDate? = date?.let { LocalDate.parse(date.toString(), DateTimeFormatter.BASIC_ISO_DATE) }

    @TypeConverter
    fun inputTypeToString(type: InputType?): String? = type?.name

    @TypeConverter
    fun stringToInputType(type: String?): InputType? = type?.let { InputType.valueOf(it) }
}
