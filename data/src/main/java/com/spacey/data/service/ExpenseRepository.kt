package com.spacey.data.service

import com.spacey.data.base.ioScope
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    suspend fun getExpenses(date: LocalDate): Flow<List<ExpenseEntity>> {
        return ioScope {
            expenseDao.getDistinctExpenses(date)
        }
    }

    suspend fun insertExpense(expenseEntity: ExpenseEntity) {
        return ioScope {
            expenseDao.insert(expenseEntity)
        }
    }
}