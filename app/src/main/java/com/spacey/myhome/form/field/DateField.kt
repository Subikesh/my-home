package com.spacey.myhome.form.field

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.spacey.myhome.utils.HomeDatePickerRow
import java.time.LocalDate

@Composable
fun DateField(label: String, selectedDate: LocalDate, modifier: Modifier = Modifier, onDateChanged: (LocalDate) -> Unit) {
    HomeDatePickerRow(
        initialDate = selectedDate,
        onDateChanged = {
            onDateChanged(it)
        }, modifier = modifier
    ) {
        Card {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(label, Modifier.weight(1f))
                Text(selectedDate.toString(), Modifier.width(150.dp), textAlign = TextAlign.Center)
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}