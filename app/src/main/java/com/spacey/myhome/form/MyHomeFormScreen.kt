package com.spacey.myhome.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.spacey.data.base.InputType
import com.spacey.data.service.DateRecurrence
import com.spacey.data.service.ExpenseEntity
import com.spacey.data.service.RecurrenceType
import com.spacey.data.service.Service
import com.spacey.myhome.ui.component.Field
import com.spacey.myhome.ui.component.FormInputView
import java.time.DayOfWeek
import java.time.LocalDate

// TODO: set onSubmit lambda here for submit
@Composable
fun MyHomeFormScreen(currentDate: LocalDate, onSubmit: (ExpenseEntity) -> Unit) {
    var selectedTabIndex: Int by remember { mutableIntStateOf(0) }
    val tabList = listOf(FormTab.Daily(currentDate), FormTab.Monthly(currentDate))
    Column(Modifier.fillMaxSize()) {
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
        Button(onClick = {
            selectedTab.fieldList
            onSubmit(selectedTab.getExpenseEntity())
        }, modifier = outerPadding) {
            Text(text = "Submit")
        }
    }
}

sealed class FormTab(val name: String, val fieldList: List<Field>) {
    class Daily(selectedDate: LocalDate) : FormTab(
        "Daily",
        listOf(
            Field.Text("Name", KeyboardType.Text),
            Field.Date("From date", selectedDate),
            Field.WeekDayPicker("Week Days", DayOfWeek.values().toList()),
            Field.Picklist("Type", InputType.values().toList(), InputType.values().indexOf(InputType.COUNTER)),
            Field.Text("Amount", KeyboardType.Decimal, "0")
        )
    )
    class Monthly(selectedDate: LocalDate) : FormTab(
        "Monthly",
        listOf(
            Field.Text("Name", KeyboardType.Text),
            Field.Date("From month", selectedDate),
            Field.Text("Amount", KeyboardType.Decimal, "0")
        )
    )
}

fun FormTab.getExpenseEntity(): ExpenseEntity {
    return when (this) {
        is FormTab.Daily -> {
            val picklistField = fieldList[3] as Field.Picklist<*>
            val picklistValue = picklistField.options[picklistField.selectedIndex] as InputType
            val amount = (fieldList[4] as Field.Text).value.toDouble()
            ExpenseEntity(
                service = Service(
                    name = (fieldList[0] as Field.Text).value,
                    type = picklistValue,
                    amount = amount
                ),
                amount = amount,
                dateRecurrence = DateRecurrence(
                    fromDate = (fieldList[1] as Field.Date).value,
                    toDate = null,
                    recurrence = RecurrenceType.Weekly((fieldList[2] as Field.WeekDayPicker).value)
                )
            )
        }
        is FormTab.Monthly -> {
            val amount = (fieldList[2] as Field.Text).value.toDouble()
            ExpenseEntity(
                service = Service(
                    name = (fieldList[0] as Field.Text).value,
                    type = InputType.AMOUNT,
                    amount = amount
                ),
                amount = amount,
                dateRecurrence = DateRecurrence(
                    fromDate = (fieldList[1] as Field.Date).value,
                    toDate = null,
                    recurrence = RecurrenceType.EveryMonth
                )
            )
        }
    }
}

//@Preview
//@Composable
//fun Preview() {
//    MyHomeFormScreen(LocalDate.now())
//}