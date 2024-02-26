package com.spacey.data.service

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.spacey.data.base.InputType
import java.time.DayOfWeek
import java.time.LocalDate

@Entity(
    tableName = Table.EXPENSE,
    indices = [Index(ExpenseCol.SERVICE_REGISTRY_ID, ExpenseCol.IS_DAILY, unique = true)],
    foreignKeys = [
        ForeignKey(ServiceRegistry::class, ["id"], [ExpenseCol.SERVICE_REGISTRY_ID]),
        ForeignKey(DateRecurrence::class, ["id"], [ExpenseCol.DATE_RECURRENCE_ID], onDelete = ForeignKey.CASCADE)
    ]
)
data class Expense(
    @ColumnInfo(ExpenseCol.SERVICE_REGISTRY_ID) val serviceRegistryId: Long,
    @ColumnInfo(ExpenseCol.DATE_RECURRENCE_ID) val dateRecurrenceId: Long,
    @ColumnInfo(ExpenseCol.AMOUNT) val amount: Double,
    @ColumnInfo(ExpenseCol.IS_DAILY) val isDaily: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Entity(
    tableName = Table.SERVICE_REGISTRY,
    foreignKeys = [ForeignKey(Service::class, ["id"], [ServiceInputCol.SERVICE_ID])]
)
data class ServiceRegistry(
    @ColumnInfo(ServiceInputCol.SERVICE_ID) val serviceId: Long,
    @ColumnInfo(ServiceInputCol.AMOUNT) val amount: Double,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Entity(tableName = Table.SERVICE, indices = [Index(ServiceCol.NAME, unique = true)])
data class Service(
    @ColumnInfo(ServiceCol.NAME) val name: String,
    @ColumnInfo(ServiceCol.TYPE) val type: InputType,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Entity(tableName = Table.DATE_RECURRENCE)
data class DateRecurrence(
    @ColumnInfo(DateRecurrenceCol.START_DATE) val startDate: LocalDate,
    @ColumnInfo(DateRecurrenceCol.WEEK_DAYS) val weekDays: List<DayOfWeek>,
    @ColumnInfo(DateRecurrenceCol.UPDATED_TIME) val updatedTime: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

object Table {
    const val EXPENSE = "Expense"
    const val SERVICE = "Service"
    const val SERVICE_REGISTRY = "ServiceRegistry"
    const val DATE_RECURRENCE = "DateRecurrence"
}

object ExpenseCol {
    const val SERVICE_REGISTRY_ID = "service_registry_id"
    const val DATE_RECURRENCE_ID = "date_recurrence_id"
    const val IS_DAILY = "is_daily"
    const val AMOUNT = "amount"
}

object ServiceCol {
    const val NAME = "name"
    const val TYPE = "type"
}

object ServiceInputCol {
    const val SERVICE_ID = "service_id"
    const val AMOUNT = "amount"
}

object DateRecurrenceCol {
    const val START_DATE = "start_date"
    const val WEEK_DAYS = "week_days"
    const val UPDATED_TIME = "updated_time"
}