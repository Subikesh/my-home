package com.spacey.myhome.data.expense

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.spacey.myhome.data.expense.ExpenseConstants.DB

@Dao
interface ExpenseLocalDataSource {

    @Query("SELECT * FROM ${DB.TABLE} WHERE ${DB.DATE} = :dateString")
    fun getDateExpenses(dateString: String): List<ExpenseDBEntity>

    @Insert(ExpenseDBEntity::class)
    fun insertExpenses(expenses: List<ExpenseDBEntity>)
}