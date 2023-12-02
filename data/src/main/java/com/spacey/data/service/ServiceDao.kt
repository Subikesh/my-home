package com.spacey.data.service

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.spacey.data.base.BaseDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDate

@Dao
abstract class ExpenseDao : BaseDao<Expense> {
    @Query("SELECT Expense.* FROM Expense LEFT JOIN DateRecurrence ON Expense.date_recurrence = DateRecurrence.id WHERE :date BETWEEN from_date AND to_date")
    protected abstract fun getExpenses(date: LocalDate): Flow<List<Expense>>

    fun getDistinctExpenses(date: LocalDate) = getExpenses(date).distinctUntilChanged()

    @Transaction
    override suspend fun insertAll(data: List<Expense>) {
        data.forEach { expense ->

        }
    }
}

@Dao
abstract class ServiceDao : BaseDao<Service>