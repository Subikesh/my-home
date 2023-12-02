package com.spacey.data.service

import com.spacey.data.base.InputType
import com.spacey.data.base.ioScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    suspend fun getExpenses(date: LocalDate): Flow<List<ExpenseEntity>> {
        return ioScope {
            expenseDao.getDistinctExpenses(date).map { expense ->
                expense.map {
                    ExpenseEntity(
                        it.service.type,
                        it.expense.amount,
                        it.expense.dateRecurrence
                    )
                }
            }
        }
    }
}

data class ExpenseEntity(val serviceType: InputType, val amount: Float, val dateRecurrence: DateRecurrence)