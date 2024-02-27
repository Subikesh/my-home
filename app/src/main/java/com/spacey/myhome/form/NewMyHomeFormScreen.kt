package com.spacey.myhome.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.spacey.data.base.InputType
import com.spacey.data.service.NewExpenseEntity
import com.spacey.data.service.Service
import com.spacey.myhome.form.field.DateField
import com.spacey.myhome.form.field.PicklistField
import com.spacey.myhome.form.field.TextInputField
import com.spacey.myhome.form.field.WeekDayPicker
import com.spacey.myhome.ui.component.SubmitFormFab
import com.spacey.myhome.utils.MyHomeScaffold
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun MyHomeFormScreen(
    date: LocalDate,
    navController: NavController,
    service: String,
    amount: Double = 0.0,
    weekDays: List<DayOfWeek> = DayOfWeek.entries,
    defaultType: InputType = InputType.COUNTER,
    viewModel: MyHomeFormViewModel = viewModel()
) {
    UI(currentDate = date, service = service, defaultAmount = amount, defaultWeekDays = weekDays, defaultType = defaultType, navController = navController) { expense ->
        viewModel.onEvent(MyHomeFormEvent.CreateExpense(expense))
        navController.popBackStack()
    }
}

//TODO handle for creating new expense and also editing existing ExpenseEntity
@Composable
private fun UI(
    currentDate: LocalDate,
    service: String,
    defaultAmount: Double,
    defaultWeekDays: List<DayOfWeek>,
    defaultType: InputType,
    navController: NavController,
    onSubmit: (NewExpenseEntity) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    var startDate: LocalDate by remember { mutableStateOf(currentDate) }
    var amount: String by remember {
        mutableStateOf(defaultAmount.toString())
    }
    var inputTypeIndex: Int by remember {
        mutableIntStateOf(InputType.entries.indexOf(defaultType))
    }
    val weekDays = remember {
        mutableStateListOf(*(defaultWeekDays.toTypedArray()))
    }

    MyHomeScaffold(navController = navController, fab = {
        SubmitFormFab {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            onSubmit(
                NewExpenseEntity(
                    startDate = startDate,
                    service = Service(service, InputType.entries[inputTypeIndex]),
                    amount = amount.toDouble(), // TODO: Get default expense entry
                    serviceAmount = amount.toDouble(),
                    weekDays = weekDays
                )
            )
        }
    }) {
        Column {
            val paddingModifier = Modifier.padding(top = 16.dp)

            Text(text = "Add expense for $service", modifier = paddingModifier, style = MaterialTheme.typography.displaySmall)

            TextInputField(label = "Amount", textValue = amount, keyboardType = KeyboardType.Decimal, modifier = paddingModifier) {
                amount = it
            }
            DateField(label = "Start date", modifier = paddingModifier, selectedDate = startDate) {
                startDate = it
            }
            WeekDayPicker(label = "Week Days", weekDays = weekDays, modifier = paddingModifier, onSelectWeekDay = {
                weekDays.add(it)
            }) {
                weekDays.remove(it)
            }
            PicklistField(label = "Type", options = InputType.entries, modifier = paddingModifier, selectedIndex = inputTypeIndex) {
                inputTypeIndex = it
            }
        }
    }
}

@Preview
@Composable
fun FormPreview() {
    MyHomeFormScreen(LocalDate.now(), rememberNavController(), "Milk")
}