package com.spacey.myhome.dateservices

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.R
import com.spacey.myhome.databinding.DateHolderBinding
import com.spacey.myhome.util.resolveAttribute
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class DatePickerRecyclerAdapter(
    defaultDate: LocalDate,
    private val onClick: (LocalDate) -> Unit
) : PagingDataAdapter<LocalDate, DatePickerRecyclerAdapter.DateHolder>(DiffCallback()) {

    private var selectedDate: LocalDate = defaultDate

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DateHolderBinding.inflate(inflator, parent, false)
        return DateHolder(binding)
    }

    override fun onBindViewHolder(holder: DateHolder, position: Int) {
        val newDate = getItem(position)
        holder.bind(newDate!!, newDate == selectedDate)
        holder.itemView.setOnClickListener {
            if (selectedDate != newDate) {
                val lastSelected = selectedDate
                selectedDate = newDate
//                notifyItemChanged(dates.indexOfFirst { it == lastSelected })
//                notifyItemChanged(dates.indexOfFirst { it == newDate })
            }
            onClick(newDate)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<LocalDate>() {
        override fun areItemsTheSame(oldItem: LocalDate, newItem: LocalDate): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LocalDate, newItem: LocalDate): Boolean {
            return oldItem == newItem
        }
    }

    class DateHolder(private val binding: DateHolderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(date: LocalDate, isSelected: Boolean) {
            binding.date.text = date.dayOfMonth.toString()
            binding.dayOfWeek.text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ROOT)
            with(binding.root) {
                if (isSelected) {
                    setBackgroundColor(resolveAttribute(R.attr.colorSecondary))
                    setTextColor(resolveAttribute(R.attr.colorOnSecondary))
                } else {
                    setBackgroundColor(Color.TRANSPARENT)
                    setTextColor(resolveAttribute(R.attr.colorOnBackground))
                }
            }
        }

        private fun setTextColor(color: Int) {
            binding.date.setTextColor(color)
            binding.dayOfWeek.setTextColor(color)
        }
    }
}