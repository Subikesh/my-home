package com.spacey.myhome.dateservices.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spacey.myhome.databinding.FragmentSubscriptionDetailBinding
import com.spacey.myhome.dateservices.DateServicesViewModel

class SubscriptionDetailsFragment : Fragment() {

    private lateinit var binding: FragmentSubscriptionDetailBinding

    private val dateServicesViewModel: DateServicesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FragmentSubscriptionDetailBinding.inflate(inflater, container, false).apply {
            binding = this
            dateServicesViewModel.selectedSubscription.value?.let {
                subscriptionTitle.text = it.serviceRegistry.serviceJob.service.name
                delivererText.text = it.serviceRegistry.serviceJob.deliverer.user.userName
            }
        }

        return binding.root
    }
}