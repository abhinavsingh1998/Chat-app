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
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.Data
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel
import java.io.Serializable
import javax.inject.Inject

class InvestmentFragment : BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    private lateinit var binding: FragmentInvestmentLayoutBinding
    private lateinit var newInvestmentAdapter: NewInvestmentAdapter

    private lateinit var categoryList: List<PageManagementsOrCollectionOneModel>

    private val smartDealsListBundle = Bundle()
    private val trendingProjectsListBundle = Bundle()

    private val onInvestmentItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.tv_smart_deals_see_all -> {
                    val categoryListFragment = CategoryListFragment()
                    smartDealsListBundle.putString("Category","Smart Deals")
                    categoryListFragment.arguments = smartDealsListBundle
                    (requireActivity() as HomeActivity).replaceFragment(categoryListFragment.javaClass, "", true, smartDealsListBundle, null, 0, false)
                }
                R.id.tv_trending_projects_see_all -> {
                    val categoryListFragment = CategoryListFragment()
                    smartDealsListBundle.putString("Category","Trending Projects")
                    categoryListFragment.arguments = smartDealsListBundle
                    (requireActivity() as HomeActivity).replaceFragment(categoryListFragment.javaClass, "", true, smartDealsListBundle, null, 0, false)
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
            Log.d("Investment","${it.data?.toString()}")
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let {  data ->
                        setUpRecyclerView(data)
                        categoryList = data.pageManagementsOrCollectionOneModels
                        smartDealsListBundle.putSerializable("SmartDealsData",data.pageManagementsOrCollectionOneModels as Serializable)
                        smartDealsListBundle.putSerializable("TrendingProjectsData",data.pageManagementsOrCollectionTwoModels as Serializable)
                    }
                }
                Status.ERROR -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        })
    }

    private fun setUpRecyclerView(data: Data) {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(NewInvestmentAdapter.INVESTMENT_VIEW_TYPE_ONE))
        list.add(RecyclerViewItem(NewInvestmentAdapter.INVESTMENT_VIEW_TYPE_THREE))
        list.add(RecyclerViewItem(NewInvestmentAdapter.INVESTMENT_VIEW_TYPE_FOUR))

        newInvestmentAdapter = NewInvestmentAdapter((requireActivity() as HomeActivity),this.requireContext(), list, data)
        binding.rvInvestmentPage.adapter= newInvestmentAdapter
        newInvestmentAdapter.setItemClickListener(onInvestmentItemClickListener)
    }

}
