package com.spacey.myhome.dateservices

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.spacey.myhome.HomeActivity
import com.spacey.myhome.R
import com.spacey.myhome.util.CommonConstants
import com.spacey.myhome.databinding.FragmentDateServicesBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateServicesFragment : Fragment() {

    private var _binding: FragmentDateServicesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DateServicesViewModel by viewModels()

    private val dates = (1..100).map {
        val date = LocalDate.ofYearDay(2022, it)
        DateHomeItem(date, it == 5)
    }

    private val dateAdapter = DatePickerRecyclerAdapter(dates) {
        viewModel.selectDate(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateServicesBinding.inflate(inflater, container, false)

        binding.dateRecycler.run {
            adapter = dateAdapter
            layoutManager = LinearLayoutManager(this@DateServicesFragment.context, LinearLayoutManager.HORIZONTAL, false)
            scrollToPosition(50)
        }

        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            (activity as HomeActivity).setToolbarTitle(if (date == LocalDate.now()) {
                "Today"
            } else {
                date.format(DateTimeFormatter.ofPattern(CommonConstants.HOME_DATE_PATTERN))
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_services)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}