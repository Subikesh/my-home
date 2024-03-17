package com.spacey.myhome.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.spacey.data.base.InputType
import com.spacey.data.service.ServiceEntity
import com.spacey.myhome.form.field.DateField
import com.spacey.myhome.form.field.TextInputField
import com.spacey.myhome.form.field.WeekDayPicker
import com.spacey.myhome.ScaffoldViewState
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun MyHomeFormScreen(
    date: LocalDate,
    navController: NavController,
    service: String,
    amount: Double = 0.toDouble(),
    defaultCount: Int = 1,
    weekDays: List<DayOfWeek> = DayOfWeek.entries,
    defaultType: InputType = InputType.COUNTER,
    viewModel: MyHomeFormViewModel = viewModel(),
    setScaffoldState: (ScaffoldViewState) -> Unit
) {
    UI(
        currentDate = date,
        service = service,
        defaultAmount = amount,
        defaultWeekDays = weekDays,
        defaultType = defaultType,
        defaultCount = defaultCount,
        setScaffoldState = setScaffoldState
    ) { expense ->
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
    defaultCount: Int,
    defaultWeekDays: List<DayOfWeek>,
    defaultType: InputType,
    setScaffoldState: (ScaffoldViewState) -> Unit,
    onSubmit: (ServiceEntity) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    var startDate: LocalDate by remember { mutableStateOf(currentDate) }
    var amount: String by remember {
        mutableStateOf(defaultAmount.toString())
    }
    var count: String by remember {
        mutableStateOf(defaultCount.toString())
    }
//    var inputTypeIndex: Int by remember {
//        mutableIntStateOf(InputType.entries.indexOf(defaultType))
//    }
    val weekDays = remember {
        mutableStateListOf(*(defaultWeekDays.toTypedArray()))
    }

    setScaffoldState(ScaffoldViewState(fabIcon = {
        Icon(Icons.Default.Done, contentDescription = "Form")
    }, onFabClick = {
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        onSubmit(
            ServiceEntity(
                service = service,
                inputType = defaultType, // TODO: Only counter done for now. support others
                serviceAmount = amount.toDouble(),
                startDate = startDate,
                defaultAmount = amount.toDouble() * count.toInt(),
                weekDays = weekDays
            )
        )
    }))

    Column {
        val paddingModifier = Modifier.padding(top = 16.dp)

        Text(text = "Add expense for $service", modifier = paddingModifier, style = MaterialTheme.typography.displaySmall)

        TextInputField(label = "Service amount", textValue = amount, keyboardType = KeyboardType.Decimal, modifier = paddingModifier) {
            amount = it
        }
        TextInputField(label = "Default count", textValue = count, keyboardType = KeyboardType.Number, modifier = paddingModifier) {
            count = it
        }
        DateField(label = "Start date", modifier = paddingModifier, selectedDate = startDate) {
            startDate = it
        }
        WeekDayPicker(label = "Week Days", weekDays = weekDays, modifier = paddingModifier, onSelectWeekDay = {
            weekDays.add(it)
        }) {
            weekDays.remove(it)
        }
//        PicklistField(label = "Type", options = InputType.entries, modifier = paddingModifier, selectedIndex = inputTypeIndex) {
//            inputTypeIndex = it
//        }
    }
}