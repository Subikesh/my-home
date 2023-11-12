package com.spacey.myhome.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spacey.myhome.home.Field
import java.time.LocalDate

@Composable
fun MyHomeFormScreen(currentDate: LocalDate, defaultTab: FormTab = FormTab.Daily, onSubmit: (List<Field>) -> Unit) {
    Scaffold(floatingActionButtonPosition = FabPosition.End, floatingActionButton = {
        LargeFloatingActionButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Done, "Submit")
        }
    }) { padding ->
        var selectedTab: FormTab by remember { mutableStateOf(defaultTab) }
        Column(Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab.ordinal) {
                FormTab.values().forEachIndexed { i, formTab ->
                    Tab(
                        selected = selectedTab.ordinal == i,
                        onClick = { selectedTab = formTab },
                        text = { Text(formTab.name) }
                    )
                }
            }
            selectedTab.fieldList.forEach {
                it.InputView(Modifier.padding(8.dp), onChange = {

                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Field.InputView(modifier: Modifier = Modifier, onChange: (Field) -> Unit) {
    when (this) {
        is Field.Date -> {
            Card(modifier) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                    Text(text = this@InputView.label,
                        Modifier.weight(1f))
                    Text(
                        text = this@InputView.value.toString(),
                        modifier = Modifier
                            .width(150.dp),
                        textAlign = TextAlign.Center
                    )
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        }

        is Field.Amount -> {
            Card(modifier) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = this@InputView.label, modifier = Modifier.weight(1f))
                    TextField(
                        value = this@InputView.value,
                        onValueChange = {
                            this@InputView.value = it
                            onChange(this@InputView)
                        },
                        modifier = Modifier.width(150.dp)
                    )
                }
            }
        }
        is Field.Picklist -> {
            Card(modifier) {
                Column {
                    Text(text = "Type", modifier = Modifier.padding(16.dp))
                    var selectedValue: String by remember { mutableStateOf(this@InputView.value) }
                    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                        items(this@InputView.options) {
                            FilterChip(
                                selected = selectedValue == it,
                                onClick = { selectedValue = it },
                                label = { Text(it) },
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
        else -> TODO()
    }
}

enum class FormTab(val fieldList: List<Field>) {
    Daily(listOf(
        Field.Date("From date", LocalDate.now()),
        Field.Picklist("Type", listOf("Counter", "Checklist", "Amount"), "Counter"),
        Field.Amount("Amount", "0")
    )),
    Monthly(listOf(
        Field.Date("From month", LocalDate.now()),
        Field.Amount("Amount", "0"))
    )
}

@Preview
@Composable
fun Preview() {
    MyHomeFormScreen(LocalDate.now(), FormTab.Daily) {

    }
}