package com.spacey.myhome.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> PickList(
    label: String,
    options: List<T>,
    modifier: Modifier = Modifier,
    getDisplayValue: (T) -> String = { it.toString() },
    onSelected: (T) -> Unit
) {
    var selectedOption by remember { mutableStateOf(options.first()) }
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = getDisplayValue(selectedOption),
            label = { Text(label) },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(text = { Text(getDisplayValue(it)) }, onClick = {
                    selectedOption = it
                    onSelected(it)
                    expanded = false
                })
            }
        }

    }
}

// TODO: Max is only 100
@Composable
fun Counter(
    label: String,
    default: Int,
    modifier: Modifier = Modifier,
    maxValue: Int = 100,
    onCountChanged: (Int) -> Unit
) {
    var countValue by remember { mutableIntStateOf(default) }
    // TODO minus is always disabled
    var isMinusEnabled by remember { mutableStateOf(countValue > 0) }
    var isPlusEnabled by remember { mutableStateOf(countValue < maxValue) }
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, Modifier.weight(1f))
        IconButton(onClick = {
            onCountChanged(--countValue)
            // TODO: Optimise??
            isMinusEnabled = countValue > 0
            isPlusEnabled = countValue < maxValue
        }, enabled = isMinusEnabled) {
            Icon(Icons.Outlined.Remove, "Reduce count")
        }
        NumberField(
            value = countValue,
            readOnly = true,
            onValueChange = {},
            textAlign = TextAlign.Center,
            digits = 2
        )
        IconButton(onClick = {
            onCountChanged(++countValue)
            isMinusEnabled = countValue > 0
            isPlusEnabled = countValue < maxValue
        }, enabled = isPlusEnabled) {
            Icon(Icons.Filled.Add, "Add count")
        }
    }
}

/**
 * @param digits specifies the width based on maximum digits in the textField
 */
@Composable
fun NumberField(
    value: Int,
    onValueChange: (Int?) -> Unit,
    digits: Int,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    isError: Boolean = false,
    readOnly: Boolean = false,
    textAlign: TextAlign = TextAlign.Left,
) {
    TextField(
        value = value.toString(),
        onValueChange = { onValueChange(it.toIntOrNull()) },
        label = label,
        singleLine = singleLine,
        isError = isError,
        readOnly = readOnly,
        textStyle = TextStyle(textAlign = textAlign),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier.width((40 + if (digits > 0) 10 * (digits - 1) else 0).dp)
    )
}

sealed class Field(open val label: String) {
    // TODO: Give enum class type as param instead of List<String>?
    class Picklist(override val label: String, val options: List<String>, var value: String) : Field(label)
    class Date(override val label: String, var value: Long) : Field(label)
    class Text(override val label: String, var value: String = "") : Field(label)
    class Counter(override val label: String, var value: Int = 0) : Field(label)
}