package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentInvestmentLayoutBinding
import com.emproto.hoabl.feature.investment.adapters.NewInvestmentAdapter
import com.emproto.hoabl.model.RecyclerViewItem

class InvestmentFragment : BaseFragment() {

    private lateinit var binding: FragmentInvestmentLayoutBinding
    private lateinit var newInvestmentAdapter: NewInvestmentAdapter

    private val onInvestmentItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.tv_smart_deals_see_all -> {
                    val categoryListFragment = CategoryListFragment()
                    (requireActivity() as HomeActivity).replaceFragment(categoryListFragment.javaClass, "", true, null, null, 0, false)
                }
                R.id.tv_trending_projects_see_all -> {
                    val categoryListFragment = CategoryListFragment()
                    (requireActivity() as HomeActivity).replaceFragment(categoryListFragment.javaClass, "", true, null, null, 0, false)
                }
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInvestmentLayoutBinding.inflate(layoutInflater)
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility=View.VISIBLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(NewInvestmentAdapter.INVESTMENT_VIEW_TYPE_ONE))
        list.add(RecyclerViewItem(NewInvestmentAdapter.INVESTMENT_VIEW_TYPE_TWO))
        list.add(RecyclerViewItem(NewInvestmentAdapter.INVESTMENT_VIEW_TYPE_THREE))
        list.add(RecyclerViewItem(NewInvestmentAdapter.INVESTMENT_VIEW_TYPE_FOUR))

        newInvestmentAdapter = NewInvestmentAdapter(this.requireContext(), list)
        binding.rvInvestmentPage.adapter= newInvestmentAdapter
        newInvestmentAdapter.setItemClickListener(onInvestmentItemClickListener)
    }

//    override fun onClick(v: View) {
//        when(v.id){
//            R.id.tv_smart_deal_see_all -> { (requireActivity() as HomeActivity).addFragment(CategoryListFragment(),false)}
//            R.id.tv_trending_projects_see_all -> { (requireActivity() as HomeActivity).addFragment(CategoryListFragment(),false) }
//        }
//    }

}
