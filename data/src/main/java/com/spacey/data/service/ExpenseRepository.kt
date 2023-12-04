package com.spacey.data.service

import android.util.Log
import com.spacey.data.base.ioScope
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    suspend fun getExpenses(date: LocalDate): Flow<List<ExpenseEntity>> {
        return ioScope {
            expenseDao.getDateExpenses(date)
        }
    }

    suspend fun insertExpense(expenseEntity: ExpenseEntity) {
        return ioScope {
            expenseDao.insert(expenseEntity)
            Log.d("Db", "Inserted successfully")
        }
    }
}