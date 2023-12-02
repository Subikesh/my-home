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

// TODO: Setup one to one or other relations
@Entity(tableName = Table.SERVICE)
data class Service(
    @ColumnInfo(ServiceCol.TYPE) val type: InputType,
    @ColumnInfo(ServiceCol.AMOUNT) val amount: Double,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

@Entity(tableName = Table.EXPENSE, foreignKeys = [
    ForeignKey(Service::class, ["id"], [ExpenseCol.SERVICE]),
    ForeignKey(DateRecurrence::class, ["id"], [ExpenseCol.DATE_RECURRENCE])
])
data class Expense(
    @ColumnInfo(ExpenseCol.SERVICE) val service: Service,
    @ColumnInfo(ExpenseCol.AMOUNT) val amount: Float,
    @ColumnInfo(ExpenseCol.DATE_RECURRENCE) val dateRecurrence: DateRecurrence,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

// TODO: On insertion of date recursion, if that date already exist for that expense, split it and add the latest one on top
@Entity(tableName = Table.DATE_RECURRENCE)
data class DateRecurrence(
    @ColumnInfo(DateRecurrenceCol.FROM_DATE) val fromDate: LocalDate,
    @ColumnInfo(DateRecurrenceCol.TO_DATE) val toDate: LocalDate?,
    @ColumnInfo(DateRecurrenceCol.RECURRENCE) val recurrence: RecurrenceType,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

sealed class RecurrenceType(private val type: String, private val value: String?) {
    data object OnlyThisMonth : RecurrenceType(THIS_MONTH, null)
    data object OnlyToday : RecurrenceType(TODAY, null)
    data class Weekly(val weekdays: List<DayOfWeek>) :
        RecurrenceType(WEEKLY, weekdays.joinToString(DBConstant.SEPARATOR) { it.name })
    data class Monthly(val months: List<Month>) :
        RecurrenceType(MONTHLY, months.joinToString(DBConstant.SEPARATOR) { it.name })

    override fun toString(): String {
        return "$type${DBConstant.SEPARATOR}$value"
    }

    companion object {
        const val THIS_MONTH = "OnlyThisMonth"
        const val TODAY = "OnlyToday"
        const val WEEKLY = "Weekly"
        const val MONTHLY = "Monthly"
    }
}

object Table {
    const val EXPENSE = "Expense"
    const val SERVICE = "Service"
    const val DATE_RECURRENCE = "DateRecurrence"
}

object ExpenseCol {
    const val SERVICE = "service"
    const val AMOUNT = "amount"
    const val DATE_RECURRENCE = "date_recurrence"
}

object DateRecurrenceCol {
    const val FROM_DATE = "from_date"
    const val TO_DATE = "to_date"
    const val RECURRENCE = "recurrence"
}