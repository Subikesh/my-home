package com.spacey.myhome.form

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
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
import com.spacey.myhome.form.field.TextInputField
import com.spacey.myhome.form.field.WeekDayPicker
import com.spacey.myhome.ui.component.Field
import com.spacey.myhome.ui.component.SubmitFormFab
import com.spacey.myhome.utils.MyHomeScaffold
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun MyHomeFormScreen(date: LocalDate, navController: NavController, service: Service, viewModel: MyHomeFormViewModel = viewModel()) {
    val context = LocalContext.current
    UI(currentDate = date, service = service, navController = navController) { expense ->
        viewModel.onEvent(MyHomeFormEvent.CreateExpense(expense))
        navController.popBackStack()
    }
}

//TODO handle for creating new expense and also editing existing ExpenseEntity
@Composable
private fun UI(currentDate: LocalDate, service: Service, amount: Double,  navController: NavController, onSubmit: (NewExpenseEntity) -> Unit) {
    val haptics = LocalHapticFeedback.current
    var startDate: LocalDate by remember { mutableStateOf(currentDate) }
    var amount: Double by remember {
        mutableDoubleStateOf()
    }
    var type: InputType by remember {
        mutableStateOf(InputType.COUNTER)
    }

    MyHomeScaffold(navController = navController, fab = {
        SubmitFormFab {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            onSubmit(NewExpenseEntity(

            ))
        }
    }) {


        val paddingModifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
        Text(text = "Add expense for ${service.name}", modifier = paddingModifier)

        TextInputField(label = "Amount", textValue = "0", keyboardType = KeyboardType.Decimal, modifier = paddingModifier) {

        }

        WeekDayPicker(label = "Week Days", weekDays = DayOfWeek.entries, modifier = paddingModifier) {

        }

        Field.WeekDayPicker("Week Days", DayOfWeek.entries),
        Field.Picklist("Type", InputType.entries, InputType.entries.indexOf(InputType.COUNTER)),
        Field.Text("Amount", KeyboardType.Decimal, "0")

//        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
//            item {
//                TabRow(selectedTabIndex = selectedTabIndex) {
//                    tabList.forEachIndexed { i, formTab ->
//                        Tab(
//                            selected = selectedTabIndex == i,
//                            onClick = { selectedTabIndex = i },
//                            text = { Text(formTab.name) }
//                        )
//                    }
//                }
//            }

//            items(selectedTab.fieldList) { field ->
//                field.FormInputView(Modifier.padding(top = 24.dp, start = 8.dp, end = 8.dp), haptics = LocalHapticFeedback.current)
//            }
//        }
    }
}

@Preview
@Composable
fun FormPreview() {
    MyHomeFormScreen(LocalDate.now(), Service("Milk"), rememberNavController())
}