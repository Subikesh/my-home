package com.spacey.myhome.date

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.spacey.myhome.R

class DateFragment : Fragment() {

    private lateinit var typeSpinner: Spinner

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_date, container, false)

        with(rootView) {
            typeSpinner = findViewById(R.id.item_type)
        }
        return rootView
    }

}