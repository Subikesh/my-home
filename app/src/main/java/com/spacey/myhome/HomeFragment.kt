package com.spacey.myhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.spacey.myhome.date.DateFormFragment
import com.spacey.myhome.date.DateScreen
import com.spacey.myhome.date.DateViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var homeLayout: LinearLayout
    private lateinit var homeCalendarView: CalendarView
    private lateinit var todayButton: TextView
    private lateinit var homeProgress: ProgressBar
    private lateinit var addFieldButton: FloatingActionButton
    private lateinit var formBackButton: FloatingActionButton

    private val dateViewModel: DateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        with(rootView) {
            homeLayout = findViewById(R.id.home_layout)
            homeCalendarView = findViewById(R.id.home_calendar)
            todayButton = findViewById(R.id.today_button)
            homeProgress = findViewById(R.id.home_progress)
            addFieldButton = findViewById(R.id.add_field)
            formBackButton = findViewById(R.id.back_button)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = requireActivity().findViewById(R.id.home_toolbar)
        toolbar.title = getString(R.string.app_name)

        setFormVisibility(false)
        dateViewModel.setDate(homeCalendarView.date)
        setListeners()
        dateViewModel.initialDownload()
    }

    private fun setListeners() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dateViewModel.initialDownloadComplete.collect { isDownloaded ->
                    if (isDownloaded) {
                        homeProgress.visibility = View.GONE
                        homeLayout.visibility = View.VISIBLE

//                        val dateFragment = DateFragment()
                        val dateFragment = DateScreen()
                        childFragmentManager.beginTransaction()
                            .add(R.id.date_fragment_container, dateFragment)
                            .addToBackStack(null)
                            .commit()

                    } else {
                        homeProgress.visibility = View.VISIBLE
                        homeLayout.visibility = View.GONE
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dateViewModel.formOpened.collect { isFormOpened ->
                    setFormVisibility(isFormOpened)
                }
            }
        }

        todayButton.setOnClickListener {
            val currentMillis = System.currentTimeMillis()
            homeCalendarView.setDate(currentMillis, true, true)
            dateViewModel.setDate(currentMillis)
        }

        homeCalendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            dateViewModel.setDate(year, month, dayOfMonth)
        }

        addFieldButton.setOnClickListener {
            val fieldFormFragment = DateFormFragment()
            childFragmentManager.beginTransaction().replace(R.id.date_fragment_container, fieldFormFragment).addToBackStack("FormFragment").commit()
            dateViewModel.toggleDateForm()
        }
        formBackButton.setOnClickListener {
            val dateFragment = DateScreen()
//            val dateFragment = DateFragment()
            childFragmentManager.beginTransaction().replace(R.id.date_fragment_container, dateFragment).commit()
            dateViewModel.toggleDateForm()
        }
    }

    private fun setFormVisibility(isVisible: Boolean) {
        addFieldButton.isVisible = !isVisible
        formBackButton.isVisible = isVisible
    }

    companion object {
        const val SAMPLE_STRING_KEY = "sample_string"
    }
}