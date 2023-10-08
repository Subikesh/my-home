package com.spacey.myhome.data.expense

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spacey.myhome.data.expense.ExpenseConstants.DB

@Dao
interface ExpenseLocalDataSource {

    @Query("SELECT * FROM ${DB.TABLE} WHERE ${DB.DATE} = :dateString")
    fun getDateExpenses(dateString: String): List<ExpenseDBEntity>

    @Insert(ExpenseDBEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertExpenses(expenses: List<ExpenseDBEntity>)

    @Query("DELETE FROM ${DB.TABLE} WHERE ${DB.DATE} = :dateString")
    fun resetExpenses(dateString: String)
}