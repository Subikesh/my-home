package com.spacey.myhome.data.base

import java.util.Calendar

fun Calendar.getDateString(): String {
    val getValue: (Int) -> String = {
        val date = when (it) {
            Calendar.MONTH -> get(it)+1
            else -> get(it)
        }
        if (date < 10) "0$date" else date.toString()
    }
    return "${getValue(Calendar.DATE)}/${getValue(Calendar.MONTH)}/${getValue(Calendar.YEAR)}"
}