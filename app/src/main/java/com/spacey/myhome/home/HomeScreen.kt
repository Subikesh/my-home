package com.spacey.myhome.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.spacey.data.service.Service
import com.spacey.myhome.ScaffoldViewState
import com.spacey.myhome.navigation.NavRoute
import com.spacey.myhome.navigation.navigateTo
import com.spacey.myhome.ui.component.CardView
import com.spacey.myhome.ui.component.Field
import com.spacey.myhome.utils.HomeDatePickerRow
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel(), setScaffoldState: (ScaffoldViewState) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    UI(
        selectedDate = uiState.selectedDate,
        expenseList = uiState.expenseList,
        nonSubscribedExpenses = uiState.notSubscribedExpenses,
        servicesList = uiState.servicesList,
        setScaffoldState = setScaffoldState,
        navigateToForm = {
            navController.navigateTo(NavRoute.Form(uiState.selectedDate, it.name))
        }
    ) {
        viewModel.onEvent(HomeEvent.SetDate(it))
    }
}

@Composable
private fun UI(
    selectedDate: LocalDate,
    expenseList: List<Field<*>>,
    nonSubscribedExpenses: List<Field<*>>,
    servicesList: List<Service>,
    setScaffoldState: (ScaffoldViewState) -> Unit,
    navigateToForm: (Service) -> Unit,
    onDateChanged: (LocalDate) -> Unit
) {
    var date: LocalDate by remember { mutableStateOf(selectedDate) }

    var isBottomSheetOpen by remember {
        mutableStateOf(false)
    }
    val haptics = LocalHapticFeedback.current

    setScaffoldState(ScaffoldViewState(fabIcon = {
        Icon(Icons.Default.Add, contentDescription = "Form")
    }, onFabClick = {
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        isBottomSheetOpen = true
    }))

    HomeBottomSheet(isBottomSheetOpen, nonSubscribedExpenses, servicesList, navigateToForm) {
        isBottomSheetOpen = false
    }

    LaunchedEffect(date) {
        onDateChanged(date)
    }
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalItemSpacing = 24.dp
    ) {
        item(span = StaggeredGridItemSpan.Companion.FullLine) {
            HomeDatePickerRow(initialDate = date,
                onDateChanged = { newDate ->
                    date = newDate
                    onDateChanged(newDate)
                }) {
                Text(
                    "${date.dayOfMonth} ${
                        date.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        )
                    } ${date.year},\n${date.dayOfWeek}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.displayMedium
                )
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Date edit",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        items(expenseList) { field ->
            field.CardView()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeBottomSheet(
    isSheetOpen: Boolean,
    nonSubscribedExpenses: List<Field<*>>,
    servicesList: List<Service>,
    navigateToForm: (Service) -> Unit,
    onSheetDismiss: () -> Unit
) {
    val haptics = LocalHapticFeedback.current
    if (isSheetOpen) {
        ModalBottomSheet(onDismissRequest = onSheetDismiss) {
            val bottomPadding = Modifier.padding(bottom = 16.dp)
            Column(bottomPadding.padding(horizontal = 16.dp)) {
                if (nonSubscribedExpenses.isNotEmpty()) {
                    Text(text = "Services not subscribed for today", bottomPadding)
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalItemSpacing = 24.dp,
                        modifier = bottomPadding
                    ) {
                        items(nonSubscribedExpenses) { field ->
                            field.CardView()
                        }
                    }
                }
                Text(text = "Subscribe to more services", bottomPadding)
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(servicesList) { service ->
                        Card(modifier = Modifier.padding(8.dp), onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onSheetDismiss()
                            navigateToForm(service)
                        }) {
                            Text(service.name, Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    Surface(Modifier.background(Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Field.Counter("Sample", 10).CardView()
            Field.CheckBox("True? False?").CardView()
        }
    }
}