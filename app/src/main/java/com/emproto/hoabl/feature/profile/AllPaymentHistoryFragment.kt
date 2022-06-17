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
import com.emproto.hoabl.feature.profile.adapter.accounts.AllPaymentHistoryAdapter
import com.emproto.networklayer.response.profile.AccountsResponse

class AllPaymentHistoryFragment : Fragment(){
    lateinit var binding: FragmentPaymentHistoryBinding
    val bundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible = true
        initClickListener()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var allPaymentList = arguments?.getSerializable("accountResponse") as ArrayList<AccountsResponse.Data.PaymentHistory>
        binding.rvAllPaymentHistory.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        binding.rvAllPaymentHistory.adapter = AllPaymentHistoryAdapter(context,
            allPaymentList)
    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
    }



}