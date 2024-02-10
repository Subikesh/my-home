package com.spacey.myhome.form

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun MyHomeFormScreen(date: LocalDate, viewModel: MyHomeFormViewModel, navController: NavController) {
    val context = LocalContext.current
    UI(currentDate = date, navController = navController, onValidationFail = {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }) { expense ->
        viewModel.onEvent(MyHomeFormEvent.CreateExpense(expense))
        navController.popBackStack()
    }
}

@Composable
private fun UI(currentDate: LocalDate, navController: NavController, onValidationFail: (String) -> Unit, onSubmit: (ExpenseEntity) -> Unit) {
    var selectedTabIndex: Int by remember { mutableIntStateOf(0) }
    val tabList = listOf(FormTab.Daily(currentDate), FormTab.Monthly(currentDate))
    val selectedTab = tabList[selectedTabIndex]

    var validationMessage: String? = null
    MyHomeScaffold(navController = navController, fab = {
        SubmitFormFab {
            // Validations
            when (selectedTab) {
                is FormTab.Daily -> {
                    if (selectedTab.nameField.value.isBlank()) {
                        validationMessage = "Name cannot be empty!"
                    }
                }

                is FormTab.Monthly -> {
                    if (selectedTab.nameField.value.isBlank()) {
                        validationMessage = "Name cannot be empty!"
                    }
                }
            }
            if (validationMessage == null) {
                onSubmit(selectedTab.getExpenseEntity())
            } else {
                onValidationFail(validationMessage!!)
            }
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
            Field.Date("Start date", selectedDate),
            Field.WeekDayPicker("Week Days", DayOfWeek.entries),
            Field.Picklist("Type", InputType.entries, InputType.entries.indexOf(InputType.COUNTER)),
            Field.Text("Amount", KeyboardType.Decimal, "0")
        )
    ) { val nameField = getFieldByLabel("Name") as Field.Text
        val amountField = getFieldByLabel("Amount") as Field.Text
        val startDateField = getFieldByLabel("Start date") as Field.Date
        val typeField = getFieldByLabel("Type") as Field.Picklist<*>
        val weekDayField = getFieldByLabel("Week Days") as Field.WeekDayPicker
    }

    class Monthly(selectedDate: LocalDate) : FormTab(
        "Monthly",
        listOf(
            Field.Text("Name", KeyboardType.Text),
            Field.Date("From month", selectedDate),
            Field.Text("Amount", KeyboardType.Decimal, "0")
        )
    ) {
        val nameField = getFieldByLabel("Name") as Field.Text
        val fromMonthField = getFieldByLabel("From month") as Field.Date
        val amountField = getFieldByLabel("Amount") as Field.Text
    }
    protected fun getFieldByLabel(label: String) = fieldList.find { it.label == label }!!
}

fun FormTab.getExpenseEntity(): ExpenseEntity {
    return when (this) {
        is FormTab.Daily -> {
            val picklistValue = this.typeField.options[this.typeField.selectedIndex] as InputType
            val amount = this.amountField.value.toDouble()
            ExpenseEntity(
                service = Service(
                    name = this.nameField.value,
                    type = picklistValue,
                    amount = amount
                ),
                amount = amount,
                startDate = this.startDateField.value,
                recurrence = RecurrenceType.Weekly(this.weekDayField.value.toSet())
            )
        }

        is FormTab.Monthly -> {
            val amount = this.amountField.value.toDouble()
            ExpenseEntity(
                service = Service(
                    name = this.nameField.value,
                    type = InputType.AMOUNT,
                    amount = amount
                ),
                amount = amount,
                startDate = this.fromMonthField.value,
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