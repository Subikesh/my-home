package com.spacey.data

import android.content.Context
import com.spacey.data.base.AppDatabase
import com.spacey.data.service.ExpenseRepository
import com.spacey.data.service.ServiceRepository

internal lateinit var appComponent: AppComponent

class AppComponent(context: Context) {
    init {
        appComponent = this
    }

    private val appDatabase = AppDatabase.getInstance(context)
    private val serviceDao = appDatabase.serviceDao()
    private val expenseDao = appDatabase.expenseDao()

    val serviceRepository = ServiceRepository(serviceDao)
    val expenseRepository = ExpenseRepository(expenseDao)
}