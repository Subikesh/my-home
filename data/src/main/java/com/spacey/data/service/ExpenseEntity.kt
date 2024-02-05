package com.spacey.data.service

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.spacey.data.base.DBConstant
import com.spacey.data.base.InputType
import com.spacey.data.base.ServiceCol
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

@Entity(
    tableName = Table.EXPENSE,
    foreignKeys = [ForeignKey(Service::class, ["id"], [ExpenseCol.SERVICE_ID])]
)
data class Expense(
    @ColumnInfo(ExpenseCol.SERVICE_ID) val serviceId: Long,
    @ColumnInfo(ExpenseCol.AMOUNT) val amount: Double,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Entity(tableName = Table.SERVICE)
data class Service(
    @ColumnInfo(ServiceCol.NAME) val name: String,
    @ColumnInfo(ServiceCol.TYPE) val type: InputType,
    @ColumnInfo(ServiceCol.AMOUNT) val amount: Double,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Entity(
    tableName = Table.DATE_RECURRENCE,
    foreignKeys = [ForeignKey(Expense::class, ["id"], [DateRecurrenceCol.EXPENSE_ID])]
)
data class DateRecurrence(
    @ColumnInfo(DateRecurrenceCol.START_DATE) val startDate: LocalDate,
    @ColumnInfo(DateRecurrenceCol.RECURRENCE) val recurrence: RecurrenceType,
    @ColumnInfo(DateRecurrenceCol.EXPENSE_ID) val expenseId: Long = -1L,
    @ColumnInfo(DateRecurrenceCol.UPDATED_TIME) val updatedTime: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

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
    const val DATE_RECURRENCE = "DateRecurrence"
}

object ExpenseCol {
    const val SERVICE_ID = "service_id"
    const val AMOUNT = "amount"
}

object DateRecurrenceCol {
    const val START_DATE = "start_date"
    const val RECURRENCE = "recurrence"
    const val EXPENSE_ID = "expense_id"
    const val UPDATED_TIME = "updated_time"
}