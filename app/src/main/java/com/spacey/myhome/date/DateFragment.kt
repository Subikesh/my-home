package com.spacey.myhome.date

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.spacey.myhome.R
import com.spacey.myhome.view.CounterView
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import java.util.Calendar

class DateFragment : Fragment() {

    private lateinit var dateText: TextView
    private lateinit var fieldsContainer: LinearLayout
    private lateinit var dateLoader: ProgressBar

    private val dateViewModel: DateViewModel by viewModels({ requireParentFragment() })

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_date, container, false)
        with(rootView) {
            dateText = findViewById(R.id.date_text)
            fieldsContainer = findViewById(R.id.fields_layout)
            dateLoader = findViewById(R.id.date_loader)
        }
        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dateViewModel.dateState.collectIndexed { index, dateState ->
                    fieldsContainer.removeAllViewsInLayout()
                    setLoading(dateState is DateUIState.LOADING)
                    when (dateState) {
                        is DateUIState.LOADING -> {}
                        is DateUIState.SUCCESS -> {
                            for (expense in dateState.expenses) {
                                val inflater = layoutInflater.inflate(R.layout.daily_field_view, fieldsContainer, false)

                                with(inflater) {
                                    val fieldText = findViewById<TextView>(R.id.field_text)
                                    val counterView = findViewById<CounterView>(R.id.field_counter)
                                    fieldText.text = expense.name
                                    counterView.setOnCountChangeListener { newCount ->
                                        Log.d("Count", "Count changed for ${expense.name} to $newCount")
                                    }
                                }
                                fieldsContainer.addView(inflater)
                            }
                            Log.d("Fields", "Success fields: ${dateState.expenses}")
                        }
                        is DateUIState.ERROR -> {
                            val errorText = TextView(requireContext())
                            errorText.text = "Error fields: ${dateState.error}"
                            fieldsContainer.addView(errorText)
                        }
                    }
                    dateText.text =
                        "Current date: ${dateState.date[Calendar.DAY_OF_MONTH]}/${dateState.date[Calendar.MONTH]}/${dateState.date[Calendar.YEAR]}\n"
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        dateLoader.isVisible = isLoading
        fieldsContainer.isVisible = !isLoading
    }

}