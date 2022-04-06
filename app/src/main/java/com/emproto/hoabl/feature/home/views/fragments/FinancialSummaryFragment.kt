package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseActivity
import com.emproto.core.BaseFragment
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.databinding.FragmentFinancialSummaryBinding
import com.emproto.hoabl.feature.home.adapters.PortfolioInvestmentCardAdapter
import com.emproto.hoabl.model.FinancialSummaryItems


class FinancialSummaryFragment : BaseFragment() {


    lateinit var binding: FragmentFinancialSummaryBinding
    lateinit var adapter: PortfolioInvestmentCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFinancialSummaryBinding.inflate(layoutInflater)


        binding.financialRecycler.layoutManager = LinearLayoutManager(requireContext())

        val detailAdapter = PortfolioInvestmentCardAdapter(requireContext(), initData())
        binding.financialRecycler.adapter = detailAdapter

        binding.btnManageProjects.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                (requireActivity()as HomeActivity).addFragment(PortfolioSpecificViewFragment.newInstance(),true)
            }

        })

        return binding.root
    }

    private fun initData(): ArrayList<FinancialSummaryItems> {
        val itemList: ArrayList<FinancialSummaryItems> = ArrayList<FinancialSummaryItems>()

        itemList.add(FinancialSummaryItems("No. of Products", "2",
            "Area in Sqft", "3600",
            "Amount Invested", "36,00,000",
            "Avg Estimated Appreciation", "+4%"))
        itemList.add(FinancialSummaryItems("No. of Products", "2",
            "Sqft Applied", "3600",
            "Amount Invested", "36,00,000",
            "Amount Pending", "36,00,000"))

        return itemList
    }


}