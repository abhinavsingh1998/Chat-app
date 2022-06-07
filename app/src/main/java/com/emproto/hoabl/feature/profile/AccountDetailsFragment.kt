package com.emproto.hoabl.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.emproto.hoabl.feature.home.views.HomeActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentAccountDetailsBinding
import com.emproto.hoabl.feature.profile.adapter.AccountDetailsFragmentAdapter
import com.emproto.hoabl.feature.profile.adapter.DocumentKycAdapter
import com.emproto.hoabl.feature.profile.data.AccountDetailsData

class AccountDetailsFragment : Fragment() {
    lateinit var binding: FragmentAccountDetailsBinding
    lateinit var adapter: AccountDetailsFragmentAdapter
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)

        initView()

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true

        initClickListener()
        (requireActivity() as HomeActivity).hideBottomNavigation()


        return binding.root
    }

    private fun initView() {
        val list = ArrayList<String>()
        list.add("Address proof")
        list.add("Pan Card")
        list.add("Cancelled Check")
        list.add("Bank Account Details")
        binding.listKyc.layoutManager = LinearLayoutManager(requireContext())
        binding.listKyc.adapter = DocumentKycAdapter(requireContext(), list)

        binding.listDocuments.layoutManager = LinearLayoutManager(requireContext())
        binding.listDocuments.adapter = DocumentKycAdapter(requireContext(), list)

        binding.listPaymenthistory.layoutManager = LinearLayoutManager(requireContext())
        val detailAdapter = AccountDetailsFragmentAdapter(initData())
        binding.listPaymenthistory.adapter = detailAdapter
    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }

        binding.tvSeeAllPaymentHistory.setOnClickListener {
            val myAcccount = AccountPaymentHistory()
            (requireActivity() as HomeActivity).addFragment(myAcccount, false)
        }
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