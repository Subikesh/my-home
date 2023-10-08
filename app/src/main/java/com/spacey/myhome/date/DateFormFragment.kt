package com.spacey.myhome.date

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.spacey.myhome.view.Composables

class DateFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val formList = listOf(
                    DateField.Picklist("Type", listOf("Daily", "Monthly")),
                    DateField.Date("Date"),
                    DateField.Counter("Amount"),
                    DateField.Text("Sample text")
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    FormScreen(formList)
                }
            }
        }
    }

    @Composable
    fun FormScreen(fieldsList: List<DateField>) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding()
        ) {
            val rowModifier = Modifier.padding(vertical = 8.dp)
            fieldsList.forEach { field ->
                when (field) {
                    is DateField.Picklist -> {
                        Composables.PickList(
                            label = field.text,
                            options = field.inputs,
                            rowModifier
                        )
                    }

                    is DateField.Date -> {
                        var date by remember { mutableIntStateOf(10) }
                        var month by remember { mutableIntStateOf(10) }
                        var year by remember { mutableIntStateOf(2000) }
                        Row(
                            rowModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = field.text, Modifier.weight(2f))
                            val dateModifier = Modifier.padding(end = 4.dp)
                            Composables.NumberField(
                                value = date,
                                label = { Text("Date") },
                                digits = 4,
                                onValueChange = { if (it != null) date = it }, modifier = dateModifier)
                            Composables.NumberField(
                                value = month,
                                label = { Text("Month") },
                                digits = 4,
                                onValueChange = { if (it != null) month = it }, modifier = dateModifier)
                            Composables.NumberField(
                                value = year,
                                label = { Text("Year") },
                                digits = 6,
                                onValueChange = { if (it != null) year = it }, modifier = dateModifier)
                        }
                    }

                    is DateField.Counter -> {
                        Composables.Counter(label = field.text, default = field.default)
                    }

                    is DateField.Text -> {
                        var fieldValue by remember { mutableStateOf("") }
                        TextField(
                            label = { Text(field.text) },
                            value = fieldValue,
                            onValueChange = { fieldValue = it },
                            modifier = rowModifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

}

sealed class DateField(open val text: String) {
    data class Picklist(override val text: String, val inputs: List<String>) : DateField(text)
    data class Date(override val text: String) : DateField(text)
    data class Text(override val text: String) : DateField(text)
    data class Counter(override val text: String, val default: Int = 0) : DateField(text)
}