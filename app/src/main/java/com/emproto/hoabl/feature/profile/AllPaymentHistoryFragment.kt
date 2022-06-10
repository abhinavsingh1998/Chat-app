package com.emproto.hoabl.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPaymentHistoryBinding
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.profile.adapter.accounts.AccountsPaymentListAdapter
import com.emproto.hoabl.feature.profile.data.AccountDetailsData
import com.emproto.networklayer.response.profile.AccountsResponse

class AllPaymentHistoryFragment : Fragment() {
    lateinit var binding: FragmentPaymentHistoryBinding
    lateinit var accountsPaymentListAdapter: AccountsPaymentListAdapter
    lateinit var accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>
    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)

        initView()

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true

        initClickListener()

        return binding.root
    }
    private fun initView() {
        binding.rvAllPaymentHistory.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL, false
        )
        binding.rvAllPaymentHistory.adapter = accountsPaymentListAdapter
    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
    }


}