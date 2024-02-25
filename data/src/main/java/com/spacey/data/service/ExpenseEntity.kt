package com.spacey.data.service

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.spacey.data.base.DBConstant
import com.spacey.data.base.InputType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

@Entity(
    tableName = Table.EXPENSE,
    indices = [Index(ExpenseCol.SERVICE_REGISTRY_ID, ExpenseCol.IS_DAILY, unique = true)],
    foreignKeys = [
        ForeignKey(ServiceRegistry::class, ["id"], [ExpenseCol.SERVICE_REGISTRY_ID]),
        ForeignKey(DateRecurrence::class, ["id"], [ExpenseCol.DATE_RECURRENCE_ID])
    ]
)
data class Expense(
    @ColumnInfo(ExpenseCol.SERVICE_REGISTRY_ID) val serviceRegistryId: Long,
    @ColumnInfo(ExpenseCol.DATE_RECURRENCE_ID) val dateRecurrenceId: Long? = null,
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
    @ColumnInfo(ServiceInputCol.TYPE) val type: InputType,
    @ColumnInfo(ServiceInputCol.AMOUNT) val amount: Double,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Entity(tableName = Table.SERVICE)
data class Service(
    @ColumnInfo(ServiceCol.NAME) val name: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Entity(tableName = Table.DATE_RECURRENCE)
data class DateRecurrence(
    @ColumnInfo(DateRecurrenceCol.START_DATE) val startDate: LocalDate,
    @ColumnInfo(DateRecurrenceCol.RECURRENCE) val recurrence: RecurrenceType,
    @ColumnInfo(DateRecurrenceCol.UPDATED_TIME) val updatedTime: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

// TODO: Rework recurrence to store in DB
sealed class RecurrenceType(private val type: String, private val value: String?) {
    data object OnlyThisMonth : RecurrenceType(THIS_MONTH, null)
    data object OnlyToday : RecurrenceType(TODAY, null)
    data object EveryDay : RecurrenceType(EVERY_DAY, null)
    data object EveryMonth : RecurrenceType(EVERY_MONTH, null)
    data class Weekly(val weekdays: Set<DayOfWeek>) :
        RecurrenceType(WEEKLY, weekdays.joinToString(DBConstant.SEPARATOR) { it.name })
    data class Monthly(val months: Set<Month>) :
        RecurrenceType(MONTHLY, months.joinToString(DBConstant.SEPARATOR) { it.name })

    companion object {
        const val THIS_MONTH = "OnlyThisMonth"
        const val TODAY = "OnlyToday"
        const val WEEKLY = "Weekly"
        const val MONTHLY = "Monthly"
        const val EVERY_DAY = "EveryDay"
        const val EVERY_MONTH = "EveryMonth"
    }

    fun asString(): String = "$type${DBConstant.SEPARATOR}$value"
}

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
}

object ServiceInputCol {
    const val SERVICE_ID = "service_id"
    const val TYPE = "type"
    const val AMOUNT = "amount"
}

object DateRecurrenceCol {
    const val START_DATE = "start_date"
    const val RECURRENCE = "recurrence"
    const val UPDATED_TIME = "updated_time"
}