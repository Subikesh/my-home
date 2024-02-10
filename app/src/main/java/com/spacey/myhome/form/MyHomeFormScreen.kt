package com.spacey.myhome.form

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.spacey.data.base.InputType
import com.spacey.data.service.ExpenseEntity
import com.spacey.data.service.RecurrenceType
import com.spacey.data.service.Service
import com.spacey.myhome.ui.component.Field
import com.spacey.myhome.ui.component.FormInputView
import com.spacey.myhome.ui.component.SubmitFormFab
import com.spacey.myhome.utils.MyHomeScaffold
import java.time.DayOfWeek
import java.time.LocalDate

// TODO: set onSubmit lambda here for submit
@Composable
fun MyHomeFormScreen(date: LocalDate, viewModel: MyHomeFormViewModel, navController: NavController) {
    UI(currentDate = date, navController = navController) { expense ->
        viewModel.onEvent(MyHomeFormEvent.CreateExpense(expense))
        navController.popBackStack()
    }
}

@Composable
fun UI(currentDate: LocalDate, navController: NavController, onSubmit: (ExpenseEntity) -> Unit) {
    var selectedTabIndex: Int by remember { mutableIntStateOf(0) }
    val tabList = listOf(FormTab.Daily(currentDate), FormTab.Monthly(currentDate))
    val selectedTab = tabList[selectedTabIndex]

    MyHomeScaffold(navController = navController, fab = {
        SubmitFormFab {
            // TODO: Run validations
            onSubmit(selectedTab.getExpenseEntity())
        }
    }) {
        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {

            item {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabList.forEachIndexed { i, formTab ->
                        Tab(
                            selected = selectedTabIndex == i,
                            onClick = { selectedTabIndex = i },
                            text = { Text(formTab.name) }
                        )
                    }
                }
            }

            items(selectedTab.fieldList) { field ->
                field.FormInputView(Modifier.padding(top = 24.dp, start = 8.dp, end = 8.dp), haptics = LocalHapticFeedback.current)
            }
        }
    }
}

sealed class FormTab(val name: String, val fieldList: List<Field<*>>) {
    class Daily(selectedDate: LocalDate) : FormTab(
        "Daily",
        listOf(
            Field.Text("Name", KeyboardType.Text),
            Field.Date("From date", selectedDate),
            Field.WeekDayPicker("Week Days", DayOfWeek.entries),
            Field.Picklist("Type", InputType.entries, InputType.entries.indexOf(InputType.COUNTER)),
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
                startDate = (fieldList[1] as Field.Date).value,
                recurrence = RecurrenceType.Weekly((fieldList[2] as Field.WeekDayPicker).value.toSet())
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
                startDate = (fieldList[1] as Field.Date).value,
                recurrence = RecurrenceType.EveryMonth
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    MyHomeFormScreen(LocalDate.now(), MyHomeFormViewModel(), rememberNavController())
}