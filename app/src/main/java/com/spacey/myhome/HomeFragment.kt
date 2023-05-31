package com.spacey.myhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.spacey.myhome.date.DateFragment
import com.spacey.myhome.date.DateViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var homeCalendarView: CalendarView
    private lateinit var todayButton: TextView
    private lateinit var dateText: TextView

    private val dateViewModel: DateViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        with(rootView) {
            homeCalendarView = findViewById(R.id.home_calendar)
            todayButton = findViewById(R.id.today_button)
            dateText = findViewById(R.id.date_text)
        }

        val dateFragment = DateFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.date_fragment_container, dateFragment)
            .commit()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = requireActivity().findViewById(R.id.home_toolbar)
        toolbar.title = getString(R.string.app_name)

        setListeners()
    }

    private fun setListeners() {
        todayButton.setOnClickListener {
            val currentMillis = System.currentTimeMillis()
            homeCalendarView.setDate(currentMillis, true, true)
            dateViewModel.setDate(currentMillis)
        }

        homeCalendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            dateViewModel.setDate(year, month, dayOfMonth)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dateViewModel.dateState.collect { selectedDate ->
                    dateText.text =
                        "Current date: ${selectedDate.dayOfMonth}/${selectedDate.month}/${selectedDate.year}"
                }
            }
        }
    }

    companion object {
        const val SAMPLE_STRING_KEY = "sample_string"
    }
}