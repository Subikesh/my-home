package com.spacey.myhome.dateservices

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.spacey.myhome.R
import com.spacey.myhome.databinding.FragmentDateServicesBinding
import java.time.LocalDate

class DateServicesFragment : Fragment() {

    private var _binding: FragmentDateServicesBinding? = null
    private val binding get() = _binding!!

    // TODO: Move to VM
    private val _selectedDate: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.ofYearDay(2022, 5))
    val selectedDate: LiveData<LocalDate> = _selectedDate

    private val dates = (1..100).map {
        val date = LocalDate.ofYearDay(2022, it)
        DateHomeItem(date, it == 5)
    }

    private val dateAdapter = DatePickerRecyclerAdapter(dates)

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

        dateAdapter.setOnClickListener { selected ->
            for (date in dates) {
                if (selectedDate.value == date.date) {
                    date.isSelected = false
                } else if (date.date == selected) {
                    date.isSelected = true
                }
            }
            _selectedDate.value = selected
            dateAdapter.submitList(dates)
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