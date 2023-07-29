package com.spacey.myhome.date

import com.spacey.myhome.data.expense.ExpenseEntity
import java.util.Calendar

sealed class DateUIState(open val date: Calendar) {

    data class LOADING(override val date: Calendar) : DateUIState(date)

    data class SUCCESS(override val date: Calendar, val expenses: List<ExpenseEntity>) : DateUIState(date)

    data class ERROR(override val date: Calendar, val error: Throwable?) : DateUIState(date)
}
