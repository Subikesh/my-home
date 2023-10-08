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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

object Composables {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PickList(label: String, options: List<String>, modifier: Modifier = Modifier) {
        var selectedText by remember { mutableStateOf(options.first()) }
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedText,
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
                    DropdownMenuItem(text = { Text(it) }, onClick = {
                        selectedText = it
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
        maxValue: Int = 100
    ) {
        var countValue by remember { mutableIntStateOf(default) }
        var errorText: String? by remember { mutableStateOf(null) }
        // TODO minus is always disabled
        var isMinusEnabled by remember { mutableStateOf(countValue > 0) }
        var isPlusEnabled by remember { mutableStateOf(countValue < maxValue) }
        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = label, Modifier.weight(1f))
            IconButton(onClick = { countValue-- }, enabled = isMinusEnabled) {
                Icon(Icons.Outlined.Remove, "Reduce count")
            }
            NumberField(
                value = countValue,
                onValueChange = {
                    it?.let { value ->
                        isMinusEnabled = value > 0
                        isPlusEnabled = value < maxValue
                        if (value > maxValue) {
                            errorText = "Value must be less than $maxValue"
                        } else {
                            countValue = value
                        }
                    }
                },
                textAlign = TextAlign.Center,
                digits = 2,
                isError = errorText != null,
                supportingText = {
                    errorText?.let {
                        Text(it, color = Color.Red)
                    }
                }
            )
            IconButton(onClick = { countValue++ }, enabled = isPlusEnabled) {
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
        supportingText: @Composable (() -> Unit)? = null,
        textAlign: TextAlign = TextAlign.Left,
    ) {
        TextField(
            value = value.toString(),
            onValueChange = { onValueChange(it.toIntOrNull()) },
            label = label,
            singleLine = singleLine,
            supportingText = supportingText,
            isError = isError,
            textStyle = TextStyle(textAlign = textAlign),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier.width((40 + if (digits > 0) 10 * (digits - 1) else 0).dp)
        )
    }

}