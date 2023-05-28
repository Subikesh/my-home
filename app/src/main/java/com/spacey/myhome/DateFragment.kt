package com.spacey.myhome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView

class DateFragment : Fragment() {

    private lateinit var dateString: TextView
    private lateinit var typeSpinner: Spinner

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

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateString.text = "Current date: ${arguments?.getString(DATE_MILLIS)}"
    }

    companion object {
        const val DATE_MILLIS = "date_millis"
    }
}