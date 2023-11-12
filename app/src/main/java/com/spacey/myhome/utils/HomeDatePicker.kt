package com.spacey.myhome.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDatePickerRow(modifier: Modifier = Modifier, initialDate: LocalDate = LocalDate.now(), onDateChanged: (LocalDate) -> Unit, content: @Composable RowScope.() -> Unit) {
    val haptics = LocalHapticFeedback.current
    var showDateDialog by remember { mutableStateOf(false) }
    Row(
        modifier.clickable { showDateDialog = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
    if (showDateDialog) {
        val dateState = rememberDatePickerState(initialSelectedDateMillis = initialDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())

        if (dateState.selectedDateMillis != null) {
            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }

        DatePickerDialog(
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                Button(onClick = {
                    dateState.selectedDateMillis?.let { onDateChanged(it.toLocalDate()) }
                    showDateDialog = false
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                }) {
                    Text("Confirm")
                }
            }) {
            DatePicker(state = dateState)
        }
    }
}

