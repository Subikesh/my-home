package com.spacey.myhome.serviceform

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.spacey.data.base.InputType
import com.spacey.data.service.Service
import com.spacey.myhome.ScaffoldViewState
import com.spacey.myhome.expenseform.field.PicklistField
import com.spacey.myhome.expenseform.field.TextInputField

@Composable
fun ServiceFormScreen(service: Service?, navController: NavController, viewModel: ServiceViewModel = viewModel(), setScaffoldState: (ScaffoldViewState) -> Unit) {
    UI(service = service, setScaffoldState = setScaffoldState) {
        viewModel.onEvent(ServiceEvent.AddService(it))
        navController.popBackStack()
    }
}

@Composable
private fun UI(service: Service?, setScaffoldState: (ScaffoldViewState) -> Unit, onSubmit: (Service) -> Unit) {
    val haptics = LocalHapticFeedback.current
    var serviceName: String by remember {
        mutableStateOf(service?.name ?: "")
    }
    var inputTypeIndex: Int by remember {
        mutableIntStateOf(InputType.entries.indexOf(service?.type ?: InputType.COUNTER))
    }

    setScaffoldState(ScaffoldViewState(fabIcon = {
        Icon(Icons.Default.Done, contentDescription = "Form")
    }, onFabClick = {
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        onSubmit(Service(name = serviceName, type = InputType.entries[inputTypeIndex]))
    }))

    Column {
        val paddingModifier = Modifier.padding(top = 16.dp)

        Text(text = "Add a new service", modifier = paddingModifier, style = MaterialTheme.typography.displaySmall)

        TextInputField(label = "Service Name", textValue = serviceName, modifier = paddingModifier) {
            serviceName = it
        }
        PicklistField(label = "Type", options = InputType.entries, modifier = paddingModifier, selectedIndex = inputTypeIndex) {
            inputTypeIndex = it
        }
    }

}