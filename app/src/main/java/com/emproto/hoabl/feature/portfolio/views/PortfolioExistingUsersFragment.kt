package com.emproto.hoabl.feature.portfolio.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentFinancialSummaryBinding
import com.emproto.hoabl.feature.portfolio.adapters.ExistingUsersAdapter
import com.emproto.hoabl.model.RecyclerViewItem


class PortfolioExistingUsersFragment : BaseFragment() {

    private lateinit var binding: FragmentFinancialSummaryBinding
    private lateinit var adapter: ExistingUsersAdapter

    val onItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.tv_manage_projects -> {
                    val portfolioSpecificProjectView = PortfolioSpecificProjectView()
                    (requireActivity() as HomeActivity).replaceFragment(
                        portfolioSpecificProjectView.javaClass,
                        "",
                        true,
                        null,
                        null,
                        0,
                        false
                    )
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFinancialSummaryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpRecyclerView()
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE
    }

    private fun setUpRecyclerView() {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(ExistingUsersAdapter.TYPE_HEADER))
        list.add(RecyclerViewItem(ExistingUsersAdapter.TYPE_SUMMARY_COMPLETED))
        list.add(RecyclerViewItem(ExistingUsersAdapter.TYPE_SUMMARY_ONGOING))
        list.add(RecyclerViewItem(ExistingUsersAdapter.TYPE_COMPLETED_INVESTMENT))
        list.add(RecyclerViewItem(ExistingUsersAdapter.TYPE_ONGOING_INVESTMENT))
        list.add(RecyclerViewItem(ExistingUsersAdapter.TYPE_NUDGE_CARD))
        list.add(RecyclerViewItem(ExistingUsersAdapter.TYPE_WATCHLIST))
        list.add(RecyclerViewItem(ExistingUsersAdapter.TYPE_REFER))
        adapter = ExistingUsersAdapter(this, requireContext(), list)
        binding.financialRecycler.adapter = adapter
        adapter.setItemClickListener(onItemClickListener)
    }

}