package com.spacey.myhome.expenseform

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.spacey.data.base.InputType
import com.spacey.data.service.ServiceEntity
import com.spacey.myhome.ScaffoldViewState
import com.spacey.myhome.expenseform.field.DateField
import com.spacey.myhome.expenseform.field.TextInputField
import com.spacey.myhome.expenseform.field.WeekDayPicker
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun MyHomeFormScreen(
    date: LocalDate,
    navController: NavController,
    service: String,
    viewModel: MyHomeFormViewModel = viewModel(),
    setScaffoldState: (ScaffoldViewState) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(service, date) {
        viewModel.onEvent(MyHomeFormEvent.SetService(service, date))
    }

    when (uiState) {
        is MyHomeFormUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        else -> {
            UI(
                currentDate = date,
                service = service,
                serviceEntity = (uiState as? MyHomeFormUiState.Success)?.serviceEntity,
                setScaffoldState = setScaffoldState
            ) { expense ->
                viewModel.onEvent(MyHomeFormEvent.CreateExpense(expense))
                navController.popBackStack()
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun UI(
    currentDate: LocalDate,
    service: String,
    serviceEntity: ServiceEntity?,
    setScaffoldState: (ScaffoldViewState) -> Unit,
    onSubmit: (ServiceEntity) -> Unit
) {
    val haptics = LocalHapticFeedback.current

    var startDate: LocalDate by remember {
        mutableStateOf(serviceEntity?.startDate ?: currentDate)
    }
    var amount: String by remember {
        mutableStateOf((serviceEntity?.serviceAmount ?: 0.0).toString())
    }
    var count: String by remember {
        mutableStateOf((serviceEntity?.defaultCount ?: 1).toString())
    }
    val weekDays = remember {
        mutableStateListOf(*((serviceEntity?.weekDays ?: DayOfWeek.entries).toTypedArray()))
    }

    setScaffoldState(ScaffoldViewState(fabIcon = {
        Icon(Icons.Default.Done, contentDescription = "Form")
    }, onFabClick = {
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        onSubmit(
            ServiceEntity(
                service = service,
                inputType = InputType.COUNTER, // inputType decided with service. No need to pass here
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
    }
}