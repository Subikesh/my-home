package com.spacey.data

import android.content.Context
import com.spacey.data.base.AppDatabase
import com.spacey.data.service.ExpenseRepository

internal lateinit var appComponent: AppComponent

class AppComponent(context: Context) {
    init {
        appComponent = this
    }

    private val appDatabase = AppDatabase.getInstance(context)
    private val expenseDao = appDatabase.expenseDao()

    val expenseRepository = ExpenseRepository(expenseDao)
}