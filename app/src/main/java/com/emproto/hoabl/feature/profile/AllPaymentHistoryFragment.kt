package com.emproto.hoabl.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.FragmentPaymentHistoryBinding
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.profile.adapter.accounts.AccountsPaymentListAdapter
import com.emproto.networklayer.response.profile.AccountsResponse

class AllPaymentHistoryFragment : Fragment() ,AccountsPaymentListAdapter.OnPaymentItemClickListener{
    lateinit var binding: FragmentPaymentHistoryBinding
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

        binding.rvAllPaymentHistory.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        binding.rvAllPaymentHistory.adapter = AccountsPaymentListAdapter(context,
          accountsPaymentList, this)
    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
    }

    override fun onAccountsPaymentItemClick(
        accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
        view: View,
        position: Int
    ) {

    }


}