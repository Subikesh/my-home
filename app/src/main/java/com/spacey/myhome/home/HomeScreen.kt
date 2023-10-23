package com.spacey.myhome.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spacey.myhome.utils.toLocalDate
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedDate: LocalDate by remember { mutableStateOf(LocalDate.now()) }
    val cardList = buildList {
        val services = listOf("Milk","Water can","Gas","Maid")
        for (i in 1..25) {
            addAll(services.map { CardItem(it) })
        }
    }
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalItemSpacing = 24.dp
    ) {
        item(span = StaggeredGridItemSpan.Companion.FullLine) {
            HomeDatePicker(date = selectedDate) {
                selectedDate = it
            }
        }

        itemsIndexed(cardList) { i, card ->
            Card(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.size((((i % 3) + 1) * 100).dp)
            ) {
                Text(
                    text = card.label,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeDatePicker(date: LocalDate, onDateChanged: (LocalDate) -> Unit) {
    val haptics = LocalHapticFeedback.current
    var showDateDialog by remember { mutableStateOf(false) }
    Row(
        Modifier.clickable { showDateDialog = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "${date.dayOfMonth} ${
                date.month.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                )
            } ${date.year},\n${date.dayOfWeek}",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.displayMedium
        )
        Icon(
            Icons.Default.Edit,
            contentDescription = "Date edit",
            modifier = Modifier.padding(16.dp)
        )
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

data class CardItem(val label: String)

@Preview
@Composable
fun Preview() {
    Surface(Modifier.background(Color.White)) {
        HomeScreen()
    }
}