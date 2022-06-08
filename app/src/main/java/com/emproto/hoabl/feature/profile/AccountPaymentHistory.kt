package com.emproto.hoabl.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentAccountDetailsBinding
import com.emproto.hoabl.databinding.FragmentPaymentHistoryBinding
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.profile.adapter.AccountDetailsFragmentAdapter
import com.emproto.hoabl.feature.profile.data.AccountDetailsData

class AccountPaymentHistory : Fragment() {
    lateinit var binding: FragmentPaymentHistoryBinding
    lateinit var adapter: AccountDetailsFragmentAdapter
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

        binding.listPaymentHistory.layoutManager = LinearLayoutManager(requireContext())
        val detailAdapter = AccountDetailsFragmentAdapter(initData())
        binding.listPaymentHistory.adapter = detailAdapter
    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
    }

    private fun initData(): ArrayList<AccountDetailsData> {
        val accountdetailsList: ArrayList<AccountDetailsData> = ArrayList<AccountDetailsData>()
        accountdetailsList.add(
            AccountDetailsData(
                "Isle of Bliss", " 11th March 2022",
                "See receipt", R.drawable.rupee_icon, "Payment Milestone 2:", "₹20,000"
            )
        )
        accountdetailsList.add(
            AccountDetailsData(
                "Yellow Meadow",
                "10th March 2022",
                "See receipt",
                R.drawable.rupee_icon,
                "Payment Milestone 4:",
                "₹80,000"
            )
        )
        accountdetailsList.add(
            AccountDetailsData(
                "Yellow Meadow",
                "10th March 2022",
                "See receipt",
                R.drawable.rupee_icon,
                "Payment Milestone 4:",
                "₹80,000"
            )
        )
        accountdetailsList.add(
            AccountDetailsData(
                "Yellow Meadow",
                "10th March 2022",
                "See receipt",
                R.drawable.rupee_icon,
                "Payment Milestone 4:",
                "₹80,000"
            )
        )

        return accountdetailsList
    }


}