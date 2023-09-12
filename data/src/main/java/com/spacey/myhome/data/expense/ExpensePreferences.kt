package com.spacey.myhome.data.expense

import android.content.SharedPreferences

class ExpensePreferences(private val preference: SharedPreferences) {
    private val editor: SharedPreferences.Editor = preference.edit()

    var isExpensesDownloaded: Boolean
        get() = preference.getBoolean(EXPENSE_DOWNLOADED, false)
        set(value) { editor.putBoolean(EXPENSE_DOWNLOADED, value).commit() }

    companion object {
        private const val EXPENSE_DOWNLOADED = "EXPENSE_DOWNLOADED"
    }
}