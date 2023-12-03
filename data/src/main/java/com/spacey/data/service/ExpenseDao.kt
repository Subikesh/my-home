package com.spacey.data.service

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDate

@Dao
abstract class ExpenseDao {
    // TODO check if between is inclusive
    @Transaction
    @Query("SELECT * FROM Expense WHERE (to_date IS NULL AND from_date <= :date) OR (to_date IS NOT NULL AND :date BETWEEN from_date AND to_date)")
    protected abstract fun getExpenses(date: LocalDate): Flow<List<ExpenseEntity>>

    fun getDistinctExpenses(date: LocalDate) = getExpenses(date).distinctUntilChanged()

    @Transaction
    open fun insert(expense: ExpenseEntity): Long {
        val serviceId = insert(expense.service)
        return insert(Expense(serviceId, expense.amount, expense.dateRecurrence))
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(expense: Expense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(service: Service): Long
}

data class ExpenseEntity(
    @Relation(parentColumn = ExpenseCol.SERVICE_ID, entityColumn = "id") val service: Service,
    @ColumnInfo(ExpenseCol.AMOUNT) val amount: Double,
    @Embedded val dateRecurrence: DateRecurrence,
    @ColumnInfo(ExpenseCol.SERVICE_ID) private val serviceId: Long = 0
)