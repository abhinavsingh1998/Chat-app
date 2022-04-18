package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentFinancialSummaryBinding
import com.emproto.hoabl.feature.home.adapters.FinancialSumAdapter
import com.emproto.hoabl.model.RecyclerViewItem


class FinancialSummaryFragment : BaseFragment() {

    private lateinit var binding: FragmentFinancialSummaryBinding
    private lateinit var adapter: FinancialSumAdapter

    private val onItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.btn_manage_projects ->{
                    val portfolioSpecificViewFragment = PortfolioSpecificViewFragment()
                    (requireActivity() as HomeActivity).replaceFragment(portfolioSpecificViewFragment.javaClass, "", true, null, null, 0, false)
                }
            }
        }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        binding = FragmentFinancialSummaryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(1))
        list.add(RecyclerViewItem(2))
        list.add(RecyclerViewItem(3))
        list.add(RecyclerViewItem(4))
        adapter = FinancialSumAdapter(requireContext(), list)
        binding.financialRecycler.adapter = adapter
        adapter.setItemClickListener(onItemClickListener)
    }

}