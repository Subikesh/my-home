package com.spacey.myhome.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spacey.myhome.utils.toLocalDate
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen() {
    Column(Modifier.fillMaxWidth()) {
        var selectedDate: LocalDate by remember { mutableStateOf(LocalDate.now()) }
        HomeDatePicker(date = selectedDate) {
            selectedDate = it
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeDatePicker(date: LocalDate, onDateChanged: (LocalDate) -> Unit) {
    val haptics = LocalHapticFeedback.current
    var showDateDialog by remember { mutableStateOf(false) }
    Row(Modifier.clickable { showDateDialog = true }, verticalAlignment = Alignment.CenterVertically) {
        Text(
            "${date.dayOfMonth} ${date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} ${date.year},\n${date.dayOfWeek}",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.displayMedium
        )
        Icon(Icons.Default.Edit, contentDescription = "Date edit", modifier = Modifier.padding(16.dp))
    }
    if (showDateDialog) {
        val dateState = rememberDatePickerState()

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

@Preview
@Composable
fun Preview() {
    Surface(Modifier.background(Color.White)) {
        HomeScreen()
    }
}