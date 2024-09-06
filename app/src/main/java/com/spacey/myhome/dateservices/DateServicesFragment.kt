package com.spacey.myhome.dateservices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.spacey.myhome.HomeActivity
import com.spacey.myhome.R
import com.spacey.myhome.databinding.DateBottomDialogBinding
import com.spacey.myhome.databinding.FragmentDateServicesBinding
import com.spacey.myhome.dateservices.datelist.DatePickerRecyclerAdapter
import com.spacey.myhome.util.CommonConstants
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DateServicesFragment : Fragment() {

    private var _binding: FragmentDateServicesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DateServicesViewModel by viewModels()

    private lateinit var dateAdapter: DatePickerRecyclerAdapter

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.date_service_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.today -> {
                    val today = LocalDate.now()
                    selectDate(today)
                }
                R.id.calendar -> {
                    val bottomSheet = BottomSheetDialog(requireContext())
                    var selectedDate: LocalDate = viewModel.getSelectedDate()
                    with(DateBottomDialogBinding.inflate(layoutInflater)) {
                        bottomSheet.setContentView(root)
                        bottomSheet.show()

                        datePicker.date = selectedDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                        datePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
                            selectedDate = LocalDate.of(year, month+1, dayOfMonth)
                        }
                        dateSelectButton.setOnClickListener {
                            selectDate(selectedDate)
                            bottomSheet.dismiss()
                        }
                    }
                }
                else -> return false
            }
            return true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateServicesBinding.inflate(inflater, container, false)

        dateAdapter = DatePickerRecyclerAdapter(getSelectedDate = { viewModel.getSelectedDate() }) {
            viewModel.selectDate(it)
        }

        activity?.addMenuProvider(menuProvider)

        setMenuVisibility(true)

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
//        binding.dateRecycler.scrollToPosition(dates.indexOfFirst { it == viewModel.selectedDate.value })
    }

    private fun selectDate(date: LocalDate) {
        viewModel.selectDate(date)
        dateAdapter.snapshot().indexOfFirst { it == date }.takeIf { it != -1 }?.let {
            binding.dateRecycler.scrollToPosition(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}