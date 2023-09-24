package com.spacey.myhome.date

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment

class DateFormFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            val formList = listOf(
                DateField.Picklist("Type", listOf("Daily", "Monthly")),
                DateField.Date("Date"),
                DateField.Decimal("Amount")
            )

            setContent {
                FormScreen(formList)
            }
        }
    }

    @Composable
    fun FormScreen(fieldsList: List<DateField>) {
        val rowModifier = Modifier.padding(16.dp, 8.dp)
        Column {
            fieldsList.forEach { field ->
                Row(modifier = rowModifier.fillMaxWidth()) {
                    Text(text = field.text)
                    val inputModifier = Modifier.padding(start = 30.dp)
                    when (field) {
                        is DateField.Picklist -> Text("Picklist", inputModifier)
                        is DateField.Date -> Text("Date", inputModifier)
                        is DateField.Decimal -> Text("Decimal", inputModifier)
                        is DateField.Text -> Text("Text", inputModifier)
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun PreviewForm() {
        FormScreen(listOf(
            DateField.Picklist("Type", listOf("Daily", "Monthly")),
            DateField.Date("Date"),
            DateField.Decimal("Amount")
        ))
    }
}

sealed class DateField(open val text: String) {
    data class Picklist(override val text: String, val inputs: List<String>) : DateField(text)
    data class Date(override val text: String) : DateField(text)
    data class Text(override val text: String) : DateField(text)
    data class Decimal(override val text: String) : DateField(text)
}