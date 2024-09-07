package com.spacey.myhome.dateservices.dateserviceslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spacey.myhome.databinding.TodayServiceItemBinding
import com.spacey.myhome.domain.Subscription

class DateServicesAdapter : ListAdapter<Subscription, DateServicesAdapter.ViewHolder>(ItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TodayServiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ViewHolder(private val binding: TodayServiceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subscription: Subscription) {
            binding.serviceName.text = subscription.serviceRegistry.service.service.name
            binding.serviceAmount.text = subscription.default.toString()
        }
    }

    private class ItemDiff : ItemCallback<Subscription>() {

        override fun areItemsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
            return oldItem.serviceRegistry.service == newItem.serviceRegistry.service
        }

        override fun areContentsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
            return oldItem == newItem
        }
    }
}