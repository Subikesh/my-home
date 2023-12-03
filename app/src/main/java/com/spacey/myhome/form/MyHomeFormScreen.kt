package com.spacey.myhome.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spacey.myhome.ui.component.Field
import com.spacey.myhome.ui.component.FormInputView
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun MyHomeFormScreen(
    currentDate: LocalDate,
    onSubmit: (List<Field>) -> Unit
) {
    var selectedTabIndex: Int by remember { mutableIntStateOf(0) }
    val tabList = listOf(FormTab.Daily(currentDate), FormTab.Monthly(currentDate))
    Column {
        val outerPadding = Modifier.padding(top = 24.dp, start = 8.dp, end = 8.dp)
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabList.forEachIndexed { i, formTab ->
                Tab(
                    selected = selectedTabIndex == i,
                    onClick = { selectedTabIndex = i },
                    text = { Text(formTab.name) }
                )
            }
        }
        val selectedTab = tabList[selectedTabIndex]
        selectedTab.fieldList.forEach { field ->
            field.FormInputView(outerPadding)
        }
        Button(onClick = { onSubmit(selectedTab.fieldList) }, modifier = outerPadding) {
            Text(text = "Submit")
        }
    }
}

sealed class FormTab(val name: String, val fieldList: List<Field>) {
    class Daily(selectedDate: LocalDate) : FormTab(
        "Daily",
        listOf(
            Field.Date("From date", selectedDate),
            Field.WeekDayPicker("Week Days", DayOfWeek.values().toList()),
            Field.Picklist("Type", listOf("Counter", "Amount", "Checkbox"), "Counter"),
            Field.Amount("Amount", "0")
        )
    )
    class Monthly(selectedDate: LocalDate) : FormTab(
        "Monthly",
        listOf(
            Field.Date("From month", selectedDate),
            Field.Amount("Amount", "0")
        )
    )
}

@Preview
@Composable
fun Preview() {
    MyHomeFormScreen(LocalDate.now()) {

    }
}