package com.spacey.myhome.date

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.spacey.myhome.view.CView

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
                var fieldValue: String by remember { mutableStateOf("") }
                when (field) {
                    is DateField.Picklist -> {
                        CView.PickList(hint = field.text, options = field.inputs, rowModifier)
                    }

                    is DateField.Date -> {
                        Row(modifier = rowModifier) {
                            Text(text = field.text)
                            val inputModifier = Modifier.padding(start = 30.dp)
                            Text("Date", inputModifier)
                        }
                    }

                    is DateField.Counter -> {
                        Row(modifier = rowModifier) {
                            Text(text = field.text)
                            val inputModifier = Modifier.padding(start = 30.dp)
                            Text("Decimal", inputModifier)
                        }
                    }

                    is DateField.Text -> {
                        TextField(
                            label = { Text(field.text) },
                            value = fieldValue,
                            onValueChange = { fieldValue = it },
                            modifier = rowModifier
                        )
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun PreviewForm() {
        Surface(modifier = Modifier.background(Color.White)) {
            Column {
                FormScreen(
                    listOf(
                        DateField.Picklist("Type", listOf("Daily", "Monthly")),
                        DateField.Date("Date"),
                        DateField.Counter("Amount"),
                        DateField.Text("Sample text")
                    )
                )
            }
        }
    }
}

sealed class DateField(open val text: String) {
    data class Picklist(override val text: String, val inputs: List<String>) : DateField(text)
    data class Date(override val text: String) : DateField(text)
    data class Text(override val text: String) : DateField(text)
    data class Counter(override val text: String) : DateField(text)
}