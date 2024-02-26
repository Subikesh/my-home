package com.spacey.data.service

import android.util.Log
import com.spacey.data.base.ioScope
import java.time.DayOfWeek
import java.time.LocalDate

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    suspend fun getExpenses(date: LocalDate): List<NewExpenseEntity> {
        return ioScope {
            expenseDao.getExpenses(date)
        }
    }

    suspend fun insertExpense(expense: NewExpenseEntity, isDaily: Boolean = false) {
        ioScope {
            expenseDao.insert(expense, isDaily)
        }
    }

    suspend fun deleteExpense(expenseEntity: ExpenseEntity) {
        return ioScope {
            expenseDao.delete(expenseEntity.id)
            Log.d("Db", "Deleted successfully")
        }
    }
}

data class NewExpenseEntity(
    val startDate: LocalDate,
    val service: Service,
    val serviceAmount: Double,
    val amount: Double,
    val weekDays: List<DayOfWeek>
)