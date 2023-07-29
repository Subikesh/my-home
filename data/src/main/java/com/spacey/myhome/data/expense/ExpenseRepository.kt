package com.spacey.myhome.data.expense

import android.util.Log
import com.spacey.myhome.data.ConnectivityHelper
import com.spacey.myhome.data.base.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import java.util.Calendar

class ExpenseRepository(
    private val connectivityHelper: ConnectivityHelper,
    private val expenseLocalDataSource: ExpenseLocalDataSource,
    private val expenseRemoteDataSource: ExpenseRemoteDataSource
) {
    suspend fun getDateFields(date: Calendar): Flow<Data<DateExpensesEntity>> {
        return flow {
            try {
                val dateString = date.getDateString()
                val dbExpense = getExpenseFromDB(dateString, false)
                emit(Data.Success(dbExpense))

                if (connectivityHelper.isInternetConnected()) {
                    val expenses = expenseRemoteDataSource.getDateExpenses().getExpense(dateString)
                    expenseLocalDataSource.insertExpenses(expenses.toDBEntity())
                    Log.d("Date", "Date: $expenses")
                    emit(Data.Success(getExpenseFromDB(dateString, true)))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Data.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun getExpenseFromDB(date: String, isLatest: Boolean) = expenseLocalDataSource.getDateExpenses(date).toDomainEntity(date, isLatest)

    private fun Calendar.getDateString(): String {
        return "${get(Calendar.DATE)}/${get(Calendar.MONTH)+1}/${get(Calendar.YEAR)}"
    }

    private fun List<ExpenseDBEntity>.toDomainEntity(date: String, isLatest: Boolean) = DateExpensesEntity(date, this.map {
        ExpenseEntity(it.name, it.fieldId, it.type, it.value)
    }).apply { this.isLatest = isLatest }

    private fun DateExpensesEntity.toDBEntity() = expenses.map {
        ExpenseDBEntity(date, it.name, it.fieldId, it.type, it.value)
    }
}