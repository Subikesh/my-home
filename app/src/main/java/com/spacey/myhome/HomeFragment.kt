package com.spacey.myhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.spacey.myhome.utils.CalendarUtils

class HomeFragment : Fragment() {

    private lateinit var homeCalendarView: CalendarView
    private lateinit var todayButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        with(rootView) {
            homeCalendarView = findViewById(R.id.home_calendar)
            todayButton = findViewById(R.id.today_button)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshDateChanged()
        setListeners()
    }

    private fun setListeners() {
        todayButton.setOnClickListener {
            homeCalendarView.setDate(System.currentTimeMillis(), true, true)
            refreshDateChanged()
        }

        homeCalendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            onDateChangeListener(year, month, dayOfMonth)
        }
    }

    private fun refreshDateChanged(millis: Long = System.currentTimeMillis()) {
        val year = CalendarUtils.getYearFromMillis(millis)
        val month = CalendarUtils.getMonthFromMillis(millis)
        val dayOfMonth = CalendarUtils.getDayOfMonthFromMillis(millis)

        onDateChangeListener(year, month, dayOfMonth)
    }

    private fun onDateChangeListener(year: Int, month: Int, dayOfMonth: Int) {
        val dateString = "$dayOfMonth/$month/$year"
        val dateBundle = bundleOf(DateFragment.DATE_MILLIS to dateString)
        val dateFragment = DateFragment()
        dateFragment.arguments = dateBundle

        childFragmentManager.beginTransaction()
            .replace(R.id.date_fragment_container, dateFragment)
            .addToBackStack(dateString)
            .commit()
    }

    companion object {
        const val SAMPLE_STRING_KEY = "sample_string"
    }
}