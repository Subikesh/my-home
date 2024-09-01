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
    private val getSelectedDate: () -> LocalDate,
    private val onClick: (LocalDate) -> Unit
) : PagingDataAdapter<DateEntity, DatePickerRecyclerAdapter.DateHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DateHolderBinding.inflate(inflator, parent, false)
        return DateHolder(binding)
    }

    override fun onBindViewHolder(holder: DateHolder, position: Int) {
        val newDate = getItem(position)!!
        val selectedDate = getSelectedDate()

        holder.bind(newDate.date, newDate.date == selectedDate)
        holder.itemView.setOnClickListener {
            if (selectedDate != newDate.date) {
                onClick(newDate.date)
//                notifyItemChanged(pos)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DateEntity>() {
        override fun areItemsTheSame(oldItem: DateEntity, newItem: DateEntity): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: DateEntity, newItem: DateEntity): Boolean {
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