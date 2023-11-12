package com.spacey.myhome.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spacey.myhome.utils.HomeDatePickerRow
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen() {
    var selectedDate: LocalDate by remember { mutableStateOf(LocalDate.now()) }
    val cardList = buildList {
        val services = listOf(
            Field.Counter("Milk", 1),
            Field.Counter("Water can", 0),
            Field.CheckBox("Gas"),
//            Field.Amount("Maid")
        )
        for (i in 1..10) {
            addAll(services)
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
            HomeDatePickerRow(initialDate = selectedDate,
                onDateChanged = { selectedDate = it }) {
                Text(
                    "${selectedDate.dayOfMonth} ${
                        selectedDate.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        )
                    } ${selectedDate.year},\n${selectedDate.dayOfWeek}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.displayMedium
                )
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Date edit",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        items(cardList) { field ->
            field.CardView()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Field.CardView(modifier: Modifier = Modifier) {
    if (!isVisible.value) return

    val haptic = LocalHapticFeedback.current
    when (this) {
        is Field.Counter -> {
            var count by remember { mutableIntStateOf(this.value) }
            ElevatedCard(
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                modifier = modifier
            ) {
                Text(
                    text = this@CardView.label,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        count--
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }, enabled = count > 0, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Remove, "Reduce count")
                    }
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = {
                        count++
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Add, "Increase count")
                    }
                }
            }
        }

        is Field.CheckBox -> {
            var checked by remember { mutableStateOf(false) }
            // TODO: secondary color wont work here. Set it as green
            val color =
                if (checked) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer
            ElevatedCard(
                colors = CardDefaults.cardColors(color),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                onClick = {
                    checked = !checked
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
            ) {
                Text(
                    text = this@CardView.label,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }

        else -> {}
    }
}

sealed class Field(open val label: String) {
    var isVisible = mutableStateOf(true)

    // TODO: Give enum class type as param instead of List<String>?
    class Picklist(override val label: String, val options: List<String>, var value: String) :
        Field(label)

    class Date(override val label: String, var value: LocalDate) : Field(label)
    class Amount(override val label: String, var value: String = "") : Field(label)
    class Counter(override val label: String, var value: Int = 0) : Field(label)
    class CheckBox(override val label: String, var value: Boolean = false) : Field(label)

    override fun toString(): String {
        return when (this) {
            is Amount -> "Field: $label is $value"
            is CheckBox -> "Field: $label is $value"
            is Counter -> "Field: $label is $value"
            is Date -> "Field: $label is $value"
            is Picklist -> "Field: $label is $value"
        }
    }
}

@Preview
@Composable
fun Preview() {
    Surface(Modifier.background(Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Field.Counter("Sample", 10).CardView()
            Field.CheckBox("True? False?").CardView()
        }
    }
}