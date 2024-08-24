package com.spacey.myhome.dateservices

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.R
import com.spacey.myhome.databinding.DateHolderBinding
import com.spacey.myhome.util.resolveAttribute
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class DatePickerRecyclerAdapter(
    private val dates: List<DateHomeItem>,
    defaultDate: LocalDate = LocalDate.now(),
    private val onClick: (LocalDate) -> Unit
) : ListAdapter<DateHomeItem, DatePickerRecyclerAdapter.DateHolder>(DiffCallback()) {

    private var selectedDate: LocalDate = defaultDate

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DateHolderBinding.inflate(inflator, parent, false)
        return DateHolder(binding)
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: DateHolder, position: Int) {
        val newDate = dates[holder.adapterPosition]
        holder.bind(newDate, newDate.date == selectedDate)
        holder.itemView.setOnClickListener {
            if (selectedDate != newDate.date) {
                val lastSelected = selectedDate
                selectedDate = newDate.date
                notifyItemChanged(dates.indexOfFirst { it.date == lastSelected })
                notifyItemChanged(dates.indexOfFirst { it.date == newDate.date })
            }
            onClick(newDate.date)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DateHomeItem>() {
        override fun areItemsTheSame(oldItem: DateHomeItem, newItem: DateHomeItem): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: DateHomeItem, newItem: DateHomeItem): Boolean {
            return oldItem == newItem
        }
    }

    class DateHolder(private val binding: DateHolderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(date: DateHomeItem, isSelected: Boolean) {
            binding.date.text = date.date.dayOfMonth.toString()
            binding.dayOfWeek.text = date.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ROOT)
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

data class DateHomeItem(val date: LocalDate, var isSelected: Boolean)