package com.spacey.myhome.dateservices.todayserviceslist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spacey.myhome.databinding.TodayServiceItemBinding
import com.spacey.myhome.domain.Subscription

class TodayServicesAdapter : ListAdapter<Subscription, TodayServicesAdapter.ViewHolder>(ItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
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