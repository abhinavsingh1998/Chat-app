package com.emproto.hoabl.feature.portfolio.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentFinancialSummaryBinding
import com.emproto.hoabl.feature.portfolio.adapters.ExistingUsersAdapter
import com.emproto.hoabl.model.RecyclerViewItem


class PortfolioExistingUsersFragment : BaseFragment() {

    private lateinit var binding: FragmentFinancialSummaryBinding
    private lateinit var adapter: ExistingUsersAdapter

    private val onItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {

            }
        }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        binding = FragmentFinancialSummaryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpRecyclerView()
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
    }

    private fun setUpRecyclerView() {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(1))
        list.add(RecyclerViewItem(2))
        list.add(RecyclerViewItem(3))
        list.add(RecyclerViewItem(4))
        list.add(RecyclerViewItem(5))
        list.add(RecyclerViewItem(6))
        list.add(RecyclerViewItem(7))
        list.add(RecyclerViewItem(8))
        adapter = ExistingUsersAdapter(requireContext(), list)
        binding.financialRecycler.adapter = adapter
        adapter.setItemClickListener(onItemClickListener)
    }

}