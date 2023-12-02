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
    @Transaction
    @Query("SELECT * FROM Expense WHERE :date BETWEEN from_date AND to_date")
    protected abstract fun getExpenses(date: LocalDate): Flow<List<ExpenseEntity>>

    fun getDistinctExpenses(date: LocalDate) = getExpenses(date).distinctUntilChanged()

    @Transaction
    open fun insert(expense: ExpenseEntity): Long {
        val serviceId = insert(expense.serviceType)
        return insert(Expense(serviceId, expense.amount, expense.dateRecurrence))
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(expense: Expense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(service: Service): Long

}

data class ExpenseEntity(
    @ColumnInfo(ExpenseCol.SERVICE_ID) val serviceId: Long,
    @Relation(parentColumn = ExpenseCol.SERVICE_ID, entityColumn = "id") val serviceType: Service,
    @ColumnInfo(ExpenseCol.AMOUNT) val amount: Float,
    @Embedded val dateRecurrence: DateRecurrence
)