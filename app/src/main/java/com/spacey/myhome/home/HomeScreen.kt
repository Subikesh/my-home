package com.spacey.myhome.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
    var showDateDialog by remember { mutableStateOf(false) }
    val textModifier = Modifier.clickable {
        showDateDialog = true
    }
    Text(
        "${date.dayOfMonth} ${date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} ${date.year},\n${date.dayOfWeek}",
        modifier = textModifier,
        style = MaterialTheme.typography.displayMedium
    )
    if (showDateDialog) {
        val dateState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                Button(onClick = {
                    dateState.selectedDateMillis?.let { onDateChanged(it.toLocalDate()) }
                    showDateDialog = false
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