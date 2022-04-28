package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentInvestmentLayoutBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.investment.adapters.NewInvestmentAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.response.enums.Status
import javax.inject.Inject

class InvestmentFragment : BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDatas()
        setUpUI()
        callApi()
        setUpRecyclerView()
    }

    private fun setUpDatas() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory).get(InvestmentViewModel::class.java)
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility=View.VISIBLE
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.VISIBLE
    }

    private fun callApi() {
        investmentViewModel.getInvestments(5002).observe(viewLifecycleOwner, Observer {
            Log.d("Investment","${it.data?.data.toString()}")
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                }
                Status.ERROR -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                }
            }
        })
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

}
