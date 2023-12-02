package com.spacey.data.service

import com.spacey.data.base.ioScope
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    suspend fun getExpenses(date: LocalDate): Flow<List<Expense>> {
        return ioScope { expenseDao.getDistinctExpenses(date) }
    }
}