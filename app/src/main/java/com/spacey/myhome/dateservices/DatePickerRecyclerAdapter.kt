package com.spacey.myhome.dateservices

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spacey.myhome.databinding.DateHolderBinding
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class DatePickerRecyclerAdapter(private val dates: List<DateHomeItem>) : ListAdapter<DateHomeItem, DatePickerRecyclerAdapter.DateHolder>(DiffCallback()) {

    private var onClickListener: (LocalDate) -> Unit = {}

    fun setOnClickListener(onClick: (LocalDate) -> Unit) {
        onClickListener = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DateHolderBinding.inflate(inflator, parent, false)
        return DateHolder(binding)
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: DateHolder, position: Int) {
        holder.bind(dates[position], onClickListener)
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

        fun bind(date: DateHomeItem, onClick: (LocalDate) -> Unit) {
            if (date.isSelected) {
                binding.root.setBackgroundColor(Color.CYAN)
            }
            binding.date.text = date.date.dayOfMonth.toString()
            binding.dayOfWeek.text = date.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ROOT)
            binding.root.setOnClickListener { onClick(date.date) }
        }
    }
}

data class DateHomeItem(val date: LocalDate, var isSelected: Boolean)