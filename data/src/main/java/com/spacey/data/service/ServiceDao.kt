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
    @Transaction
    @Query("SELECT * FROM Expense WHERE :date BETWEEN from_date AND to_date")
    protected abstract fun getExpenses(date: LocalDate): Flow<List<ExpenseAndService>>

    fun getDistinctExpenses(date: LocalDate) = getExpenses(date).distinctUntilChanged()
}

@Dao
abstract class ServiceDao : BaseDao<Service>