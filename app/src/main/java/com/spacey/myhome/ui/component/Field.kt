package com.spacey.myhome.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spacey.myhome.utils.HomeDatePickerRow
import java.time.DayOfWeek
import java.time.LocalDate

sealed class Field(open val label: String) {
    var isVisible = mutableStateOf(true)

    // TODO: Give enum class type as param instead of List<String>?
    class Picklist<T>(override val label: String, val options: List<T>, var selectedIndex: Int) :
        Field(label)

    class Date(override val label: String, var value: LocalDate) : Field(label)
    class Text(override val label: String, val keyboardType: KeyboardType, var value: String = "") : Field(label)
    class Counter(override val label: String, var value: Int = 0) : Field(label)
    class CheckBox(override val label: String, var value: Boolean = false) : Field(label)
    class WeekDayPicker(override val label: String, value: List<DayOfWeek>) : Field(label) {
        val value: MutableSet<DayOfWeek> = value.toMutableSet()
    }

    override fun toString(): String {
        return when (this) {
            is Text -> "Field: $label is $value"
            is CheckBox -> "Field: $label is $value"
            is Counter -> "Field: $label is $value"
            is Date -> "Field: $label is $value"
            is Picklist<*> -> "Field: $label is ${options[selectedIndex]}"
            is WeekDayPicker -> "Field: $label is $value"
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

        else -> throw IllegalArgumentException("Field $label not configured for card view")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Field.FormInputView(modifier: Modifier = Modifier) {
    when (this) {
        is Field.Date -> {
            var selectedDate: LocalDate by remember { mutableStateOf(this@FormInputView.value) }
            HomeDatePickerRow(
                initialDate = selectedDate,
                onDateChanged = {
                    this@FormInputView.value = it
                    selectedDate = it
                }, modifier = modifier
            ) {
                Card {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = this@FormInputView.label,
                            Modifier.weight(1f)
                        )
                        Text(
                            text = selectedDate.toString(),
                            modifier = Modifier.width(150.dp),
                            textAlign = TextAlign.Center
                        )
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }
        }

        is Field.Text -> {
            Card(modifier) {
                var value: String by remember { mutableStateOf(this@FormInputView.value) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = this@FormInputView.label, modifier = Modifier.weight(1f))
                    TextField(
                        value = value,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = this@FormInputView.keyboardType, capitalization = KeyboardCapitalization.Words),
                        onValueChange = {
                            value = it
                            this@FormInputView.value = it
                        },
                        modifier = Modifier.width(150.dp)
                    )
                }
            }
        }

        is Field.Picklist<*> -> {
            Card(modifier) {
                Column {
                    Text(text = "Type", modifier = Modifier.padding(16.dp))
                    var selectedIndex: Int by remember { mutableIntStateOf(this@FormInputView.selectedIndex) }
//                    var selectedValue: String by remember { mutableStateOf(this@FormInputView.value) }
                    LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(horizontal = 16.dp)) {
                        items(this@FormInputView.options.size) {
                            FilterChip(
                                selected = selectedIndex == it,
                                onClick = {
                                    selectedIndex = it
                                    this@FormInputView.selectedIndex = it
                                },
                                label = { Text(this@FormInputView.options[it].toString()) },
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

        // TODO: Make the filterChip in this round?
        is Field.WeekDayPicker -> {
            Card(modifier) {
                val datesSelected = remember { mutableStateListOf(*(value.toTypedArray())) }
                Column {
                    Text(text = this@FormInputView.label, modifier = Modifier.padding(16.dp))
                    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), content = {
                        items(DayOfWeek.values().asList()) {
                            FilterChip(
                                selected = it in datesSelected,
                                onClick = {
                                    if (it in datesSelected) {
                                        datesSelected.remove(it)
                                        value.remove(it)
                                    } else {
                                        datesSelected.add(it)
                                        value.add(it)
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

        else -> throw IllegalArgumentException("Field $label not configured for form input view")
    }
}