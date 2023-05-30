package com.spacey.myhome

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class DateFragment : Fragment() {

    private lateinit var dateString: TextView
    private lateinit var typeSpinner: Spinner

    private val dateViewModel: DateViewModel by viewModels({ parentFragment ?: this })

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_date, container, false)

        with(rootView) {
            dateString = findViewById(R.id.date_string)
            typeSpinner = findViewById(R.id.item_type)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dateViewModel.dateState.collect { selectedDate ->
                    dateString.text =
                        "Current date: ${selectedDate.dayOfMonth}/${selectedDate.month}/${selectedDate.year}"
                }
            }

        }

        return rootView
    }

}