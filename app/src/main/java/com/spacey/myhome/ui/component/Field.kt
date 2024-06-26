package com.spacey.myhome.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spacey.myhome.utils.HomeDatePickerRow
import java.time.DayOfWeek
import java.time.LocalDate

sealed class Field<T>(open val label: String, var value: T) {
    var isVisible = mutableStateOf(true)

    // TODO: Give enum class type as param instead of List<String>?
    class Picklist<R>(override val label: String, val options: List<R>, var selectedIndex: Int) :
        Field<R>(label, options[selectedIndex])

    class Date(override val label: String, value: LocalDate) : Field<LocalDate>(label, value)
    class Text(override val label: String, val keyboardType: KeyboardType, value: String = "") : Field<String>(label, value)
    class Counter(override val label: String, value: Int = 0) : Field<Int>(label, value)
    class CheckBox(override val label: String, value: Boolean = false) : Field<Boolean>(label, value)
    class WeekDayPicker(override val label: String, value: List<DayOfWeek>) : Field<MutableList<DayOfWeek>>(label, value.toMutableList())

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

    // TODO: As values are stored as int, using this.
    @Deprecated("Use values as some meaning type other than int")
    val intValue: Int
        get() = when (this) {
            is Counter -> value
            is CheckBox -> if (value) 1 else 0
            else -> 0
        }

    fun copy(newLabel: String = label, newValue: Int = intValue): Field<*> {
        if (newLabel == label && newValue == intValue) return this
        return when (this) {
            is CheckBox -> CheckBox(newLabel, newValue != 0)
            is Counter -> Counter(newLabel, newValue)
            else -> this
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Field<*>.CardView(modifier: Modifier = Modifier, onChange: (Int) -> Unit = {}) {
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
                        onChange(count)
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
                        onChange(count)
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
            val alpha = if (checked) 1f else 0.4f
            ElevatedCard(
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer.copy(alpha)),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                onClick = {
                    checked = !checked
                    onChange(if (checked) 1 else 0)
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }, modifier = modifier
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class)
@Composable
fun Field<*>.FormInputView(modifier: Modifier = Modifier, haptics: HapticFeedback? = null) {
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
            Card(modifier.fillMaxWidth()) {
                Column {
                    Text(text = "Type", modifier = Modifier.padding(16.dp))
                    var selectedIndex: Int by remember { mutableIntStateOf(this@FormInputView.selectedIndex) }

                    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
//                    LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(horizontal = 16.dp)) {
                        items(this@FormInputView.options.size) {
                            FilterChip(
                                selected = selectedIndex == it,
                                onClick = {
                                    haptics?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
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

        // TODO: Make the filterChip round?
        is Field.WeekDayPicker -> {
            Card(modifier.fillMaxWidth()) {
                val datesSelected = remember { mutableStateListOf(*(value.toTypedArray())) }
                Column {
                    Text(text = this@FormInputView.label, modifier = Modifier.padding(16.dp))
                    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), content = {
                        items(DayOfWeek.entries) {
                            FilterChip(
                                selected = it in datesSelected,
                                onClick = {
                                    haptics?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
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

@Preview
@Composable
fun Preview() {
    Field.Picklist("Sample", listOf("Hi", "Hello", "there"), 0).FormInputView()
}