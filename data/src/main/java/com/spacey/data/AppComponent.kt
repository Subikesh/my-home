package com.spacey.data

import android.content.Context
import com.spacey.data.base.AppDatabase
import com.spacey.data.service.ExpenseRepository

object AppComponent {

    private lateinit var appDatabase: AppDatabase
    private val expenseDao by lazy { appDatabase.expenseDao() }

    val expenseRepository by lazy { ExpenseRepository(expenseDao) }

    fun initiate(appContext: Context) {
        appDatabase = AppDatabase.getInstance(appContext)
    }
}