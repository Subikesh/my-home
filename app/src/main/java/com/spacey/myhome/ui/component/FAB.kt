package com.spacey.myhome.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.navigation.NavController
import com.spacey.myhome.navigation.NavRoute
import com.spacey.myhome.navigation.navigateTo
import java.time.LocalDate

@Composable
fun AddServiceFormFab(currentDate: LocalDate, navController: NavController) {
    val haptics = LocalHapticFeedback.current
    FloatingActionButton(onClick = {
        navController.navigateTo(NavRoute.Form(currentDate))
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
    }) {
        Icon(Icons.Default.Add, contentDescription = "Form")
    }
}

@Composable
fun SubmitFormFab(onSubmit: () -> Unit) {
    val haptics = LocalHapticFeedback.current
    FloatingActionButton(onClick = {
        onSubmit()
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
    }) {
        Icon(Icons.Default.Done, contentDescription = "Form")
    }
}