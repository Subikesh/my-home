package com.spacey.data.service

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalDate

@Dao
abstract class ExpenseDao {
    // TODO check if between is inclusive
    @Transaction
    @Query("SELECT Expense.*, start_date, recurrence, updated_time FROM Expense JOIN DateRecurrence ON Expense.id = DateRecurrence.expense_id WHERE" +
            " (recurrence LIKE '${RecurrenceType.TODAY}' AND :date = start_date) " +
            " OR :date >= start_date")
    protected abstract fun getExpenses(date: LocalDate): Flow<List<ExpenseEntity>>

    fun getDateExpenses(date: LocalDate): Flow<List<ExpenseEntity>> {
        return getExpenses(date).map {
            it.filter { expense ->
                when (val recurrence = expense.recurrence) {
                     RecurrenceType.EveryDay -> true
                     RecurrenceType.EveryMonth -> true
                     RecurrenceType.OnlyThisMonth -> date.month == expense.startDate.month
                     RecurrenceType.OnlyToday -> date == expense.startDate
                     is RecurrenceType.Monthly -> date.month in recurrence.months
                     is RecurrenceType.Weekly -> date.dayOfWeek in recurrence.weekdays
                }
            }
        }.distinctUntilChanged()
    }

    @Transaction
    open fun insert(expense: ExpenseEntity): Long {
        val serviceId = insert(expense.service)
        val expenseId = if (expense.id == -1L) {
             insert(Expense(serviceId, expense.amount))
        } else expense.id
        return insert(DateRecurrence(expense.startDate, expense.recurrence, expenseId, expense.updatedTime))
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(expense: Expense): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(service: Service): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(dateRecurrence: DateRecurrence): Long

    @Query("DELETE FROM Expense WHERE id = :id")
    abstract fun delete(id: Long)

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    abstract fun update(expense: Expense): Long
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract fun update(service: Service): Long
}

data class ExpenseEntity(
    @Relation(parentColumn = ExpenseCol.SERVICE_ID, entityColumn = "id") val service: Service,
    @ColumnInfo(ExpenseCol.AMOUNT) val amount: Double,
    @ColumnInfo(DateRecurrenceCol.START_DATE) val startDate: LocalDate,
    @ColumnInfo(DateRecurrenceCol.RECURRENCE) val recurrence: RecurrenceType,
    @ColumnInfo(DateRecurrenceCol.UPDATED_TIME) val updatedTime: Long = System.currentTimeMillis(),
    @ColumnInfo(ExpenseCol.SERVICE_ID) private val serviceId: Long = 0,
    val id: Long = -1L
)