package com.spacey.myhome.date

import com.spacey.myhome.data.expense.FieldEntity
import java.util.Calendar

sealed class DateUIState(open val date: Calendar) {

    data class LOADING(override val date: Calendar) : DateUIState(date)

    data class SUCCESS(override val date: Calendar, val fields: List<FieldEntity>) : DateUIState(date)

    data class ERROR(override val date: Calendar, val error: Throwable?) : DateUIState(date)
}
