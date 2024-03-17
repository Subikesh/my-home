package com.spacey.myhome.expenseform.field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> PicklistField(label: String, options: List<T>, selectedIndex: Int, modifier: Modifier = Modifier, onSelected: (Int) -> Unit) {
    val haptics = LocalHapticFeedback.current

    Card(modifier.fillMaxWidth()) {
        Column {
            Text(text = label, modifier = Modifier.padding(16.dp))

            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
//                    LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(options.size) {
                    FilterChip(
                        selected = selectedIndex == it,
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onSelected(it)
                        },
                        label = { Text(options[it].toString()) },
                        modifier = Modifier.padding(8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }
    }
}