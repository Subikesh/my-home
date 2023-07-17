package com.spacey.myhome.date

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.spacey.myhome.R
import com.spacey.myhome.view.CounterView
import kotlinx.coroutines.launch
import java.util.Calendar

class DateFragment : Fragment() {

    private lateinit var typeSpinner: Spinner
    private lateinit var dateText: TextView

    private lateinit var fieldsContainer: LinearLayout

    private val dateViewModel: DateViewModel by viewModels({ requireParentFragment() })

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_date, container, false)
        with(rootView) {
            typeSpinner = findViewById(R.id.item_type)
            dateText = findViewById(R.id.date_text)
            fieldsContainer = findViewById(R.id.fields_layout)
        }
        return rootView
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateViewModel.getMetadata()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dateViewModel.dateState.collect { dateState ->
                    fieldsContainer.removeAllViewsInLayout()
                    when (dateState) {
                        is DateUIState.LOADING -> {
                            val loadingText = TextView(requireContext())
                            loadingText.text = "Loading current date"
                            fieldsContainer.addView(loadingText)
                        }
                        is DateUIState.SUCCESS -> {
                            for (field in dateState.fields) {
                                val inflator = layoutInflater.inflate(R.layout.daily_field_view, fieldsContainer, false)

                                with(inflator) {
                                    val fieldText = findViewById<TextView>(R.id.field_text)
                                    val counterView = findViewById<CounterView>(R.id.field_counter)
                                    fieldText.text = field.name
                                    counterView.setOnCountChangeListener { newCount ->
                                        Log.d("Count", "Count changed for ${field.name} to $newCount")
                                    }
                                }
                                fieldsContainer.addView(inflator)
                            }
                            Log.d("Fields", "Success fields: ${dateState.fields}")
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

}