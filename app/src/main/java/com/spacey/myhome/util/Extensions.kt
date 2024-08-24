package com.spacey.myhome.util

import android.util.TypedValue
import android.view.View

fun View.resolveAttribute(resId: Int): Int {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}