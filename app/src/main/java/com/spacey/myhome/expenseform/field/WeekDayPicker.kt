package com.spacey.myhome.expenseform.field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class)
@Composable
fun WeekDayPicker(
    label: String,
    weekDays: List<DayOfWeek>,
    modifier: Modifier = Modifier,
    onSelectWeekDay: (DayOfWeek) -> Unit,
    onUnselectWeekDay: (DayOfWeek) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    Card(modifier.fillMaxWidth()) {
        Column {
            Text(text = label, modifier = Modifier.padding(16.dp))
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), content = {
                items(DayOfWeek.entries) {
                    FilterChip(
                        selected = it in weekDays,
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            if (it in weekDays) {
                                onUnselectWeekDay(it)
                            } else {
                                onSelectWeekDay(it)
                            }
                        },
                        label = { Text(it.name.first().uppercase()) },
                        shape = RoundedCornerShape(percent = 50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            })
        }
    }
}