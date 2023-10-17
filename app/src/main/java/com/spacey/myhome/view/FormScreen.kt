package com.spacey.myhome.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FormScreen(
    fieldsList: List<Field>,
    modifier: Modifier = Modifier,
    onSubmit: (List<Field>) -> Unit
) {
    Column(modifier) {
        LazyColumn(modifier = Modifier
            .weight(1f)
            .padding(bottom = 16.dp)) {
            val rowModifier = Modifier.padding(vertical = 8.dp)
            items(fieldsList) { field ->
                when (field) {
                    is Field.Picklist -> {
                        PickList(
                            label = field.label,
                            options = field.options,
                            modifier = rowModifier
                        ) {
                            field.value = it
                        }
                    }

                    is Field.Date -> {
                        var date by remember { mutableIntStateOf(10) }
                        var month by remember { mutableIntStateOf(10) }
                        var year by remember { mutableIntStateOf(2000) }
                        Row(
                            rowModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = field.label, Modifier.weight(2f))
                            val dateModifier = Modifier.padding(end = 4.dp)
                            NumberField(
                                value = date,
                                label = { Text("Date") },
                                digits = 4,
                                onValueChange = { if (it != null) date = it }, modifier = dateModifier
                            )
                            NumberField(
                                value = month,
                                label = { Text("Month") },
                                digits = 4,
                                onValueChange = { if (it != null) month = it }, modifier = dateModifier
                            )
                            NumberField(
                                value = year,
                                label = { Text("Year") },
                                digits = 6,
                                onValueChange = { if (it != null) year = it }, modifier = dateModifier
                            )
                        }
                        // TODO: set field.date on value change
                    }

                    is Field.Counter -> {
                        Counter(label = field.label, default = field.value) {
                            field.value = it
                        }
                    }

                    is Field.Text -> {
                        var fieldValue by remember { mutableStateOf(field.value) }
                        TextField(
                            label = { Text(field.label) },
                            value = fieldValue,
                            onValueChange = {
                                fieldValue = it
                                field.value = it
                            },
                            modifier = rowModifier.fillMaxWidth()
                        )
                    }

                    // TODO: Check if field height is equal
                    is Field.CheckBox -> {
                        var isChecked by remember { mutableStateOf(field.value) }
                        Row(modifier = modifier.fillMaxWidth().clickable { isChecked = !isChecked }, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = field.label, Modifier.weight(1f))
                            Checkbox(checked = isChecked, {
                                isChecked = it
                                field.value = it
                            })
                        }
                    }
                }
            }
        }
        Button(onClick = {
            onSubmit(fieldsList)
        }) {
            Text("Submit")
        }
    }

}

@Preview
@Composable
fun TestThis() {
    val formList = remember {
        listOf(
            Field.Picklist("Type", listOf("Daily", "Monthly"), "Daily"),
            Field.Date("Date", System.currentTimeMillis()),
            Field.Counter("Amount", 5),
            Field.Text("Sample text"),
            Field.CheckBox("Sample", true)
        ).toMutableStateList()
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        FormScreen(formList, validate = { field ->
            Log.d("Form", "Validation: ${field.label}")
            when (field) {
                is Field.Picklist -> {
                    formList.firstOrNull { it is Field.Date }?.isVisible = field.value == "Daily"
                }
                else -> {}
            }
        }) {
            Log.d("Form", "Form submitted: $it")
        }
    }
}