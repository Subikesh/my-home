package com.spacey.myhome.dateservices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.spacey.myhome.HomeActivity
import com.spacey.myhome.R
import com.spacey.myhome.databinding.FragmentDateServicesBinding
import com.spacey.myhome.dateservices.datelist.DatePickerRecyclerAdapter
import com.spacey.myhome.util.CommonConstants
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateServicesFragment : Fragment() {

    private var _binding: FragmentDateServicesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DateServicesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateServicesBinding.inflate(inflater, container, false)

        val dateAdapter = DatePickerRecyclerAdapter(getSelectedDate = { viewModel.getSelectedDate() }) {
            viewModel.selectDate(it)
        }

        with(binding.dateRecycler) {
            adapter = dateAdapter
            layoutManager = LinearLayoutManager(this@DateServicesFragment.context, LinearLayoutManager.HORIZONTAL, false)
        }

        viewModel.datePager.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                dateAdapter.submitData(it)
            }
        }
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            (activity as HomeActivity).setToolbarTitle(if (date == LocalDate.now()) {
                "Today"
            } else {
                date.format(DateTimeFormatter.ofPattern(CommonConstants.HOME_DATE_PATTERN))
            })
            dateAdapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_services)
        }
//        binding.dateRecycler.scrollToPosition(dates.indexOfFirst { it == viewModel.selectedDate.value })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}