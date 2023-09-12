package com.spacey.myhome.data.expense

import android.util.Log
import com.spacey.myhome.data.ConnectivityHelper
import com.spacey.myhome.data.base.Data
import com.spacey.myhome.data.base.getDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import java.util.Calendar

class ExpenseRepository(
    private val connectivityHelper: ConnectivityHelper,
    private val expenseLocalDataSource: ExpenseLocalDataSource,
    private val expenseRemoteDataSource: ExpenseRemoteDataSource,
    private val expensePreferences: ExpensePreferences
) {
    suspend fun getDateFields(date: Calendar, forceDownload: Boolean = false): Flow<Data<DateExpensesEntity>> {
        return flow {
            try {
                val dateString = date.getDateString()
                val dbExpense = getExpenseFromDB(dateString, false)
                emit(Data.Success(dbExpense))

                // TODO: Remove preference check after online support
                if ((!expensePreferences.isExpensesDownloaded || forceDownload) && connectivityHelper.isInternetConnected()) {
                    val expenses = expenseRemoteDataSource.getDateExpenses().expenses
                    expenseLocalDataSource.insertExpenses(expenses.flatMap { it.toDBEntity() })
                    Log.d("Date", "Date: $expenses")
                    expensePreferences.isExpensesDownloaded = true
                    emit(Data.Success(getExpenseFromDB(dateString, true)))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Data.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun getExpenseFromDB(date: String, isLatest: Boolean) = expenseLocalDataSource.getDateExpenses(date).toDomainEntity(date, isLatest)

    private fun List<ExpenseDBEntity>.toDomainEntity(date: String, isLatest: Boolean) = DateExpensesEntity(date, this.map {
        ExpenseEntity(it.name, it.fieldId, it.type, it.value)
    }).apply { this.isLatest = isLatest }

    private fun DateExpensesEntity.toDBEntity() = expenses.map {
        ExpenseDBEntity(date, it.name, it.fieldId, it.type, it.value)
    }
}