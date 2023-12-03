package com.spacey.data

import android.content.Context
import com.spacey.data.base.AppDatabase
import com.spacey.data.service.ExpenseRepository

object AppComponent {

    private lateinit var appDatabase: AppDatabase
    private val expenseDao = appDatabase.expenseDao()

    val expenseRepository = ExpenseRepository(expenseDao)

    fun initiate(appContext: Context) {
        appDatabase = AppDatabase.getInstance(appContext)
    }
}