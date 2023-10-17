package com.spacey.myhome.date

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spacey.myhome.view.Field
import com.spacey.myhome.view.FormScreen

class DateScreen : Fragment() {

    private val viewModel: DateViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val dateState by viewModel.dateState.collectAsState()
                DateMainScreen(dateState)
            }
        }
    }

    @Composable
    fun DateMainScreen(dateUIState: DateUIState) {
        when (dateUIState) {
            is DateUIState.LOADING -> {
                Box {
                    // TODO: Place this in center and not full screen
                    CircularProgressIndicator()
                }
            }
            is DateUIState.SUCCESS -> {
                val fieldsList = dateUIState.expenses.map {
                    when (it.type) {
                        "COUNT" -> Field.Counter(it.name, it.value.toInt())
                        "CHECK" -> Field.CheckBox(it.name, it.value.toBoolean())
                        "AMOUNT" -> Field.Text(it.name, it.value)
                        else -> Field.Text(it.name, it.value)
                    }
                }
                FormScreen(fieldsList = fieldsList, Modifier.padding(horizontal = 16.dp), onSubmit = {

                })
//                for (expense in dateUIState.expenses) {
//                    val inflater = layoutInflater.inflate(R.layout.daily_field_view, fieldsContainer, false)
//
//                    with(inflater) {
//                        val fieldText = findViewById<TextView>(R.id.field_text)
//                        val counterView = findViewById<CounterView>(R.id.field_counter)
//                        fieldText.text = expense.name
//                        counterView.setOnCountChangeListener { newCount ->
//                            Log.d("Count", "Count changed for ${expense.name} to $newCount")
//                        }
//                    }
//                    fieldsContainer.addView(inflater)
//                }
//                Log.d("Fields", "Success fields: ${dateUIState.expenses}")
            }
            is DateUIState.ERROR -> {
//                val errorText = TextView(requireContext())
//                errorText.text = "Error fields: ${dateUIState.error}"
//                fieldsContainer.addView(errorText)
                Text("Error fields ${dateUIState.error}")
            }
        }
//        dateText.text =
//            "Current date: ${dateUIState.date[Calendar.DAY_OF_MONTH]}/${dateUIState.date[Calendar.MONTH]+1}/${dateUIState.date[Calendar.YEAR]}\n"
    }
}