package com.spacey.myhome.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun AddServiceFormFab(onClick: () -> Unit) {
    LocalHapticFeedback.current
    FloatingActionButton(onClick = { onClick() }) {
        Icon(Icons.Default.Add, contentDescription = "Form")
    }
}

@Composable
fun SubmitFormFab(onSubmit: () -> Unit) {
    FloatingActionButton(onClick = {
        onSubmit()
    }) {
        Icon(Icons.Default.Done, contentDescription = "Form")
    }
}