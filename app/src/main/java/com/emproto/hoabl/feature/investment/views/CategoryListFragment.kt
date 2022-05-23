package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentCategoryListBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.investment.adapters.CategoryListAdapter
import com.emproto.hoabl.model.AllProjectsModel
import com.emproto.hoabl.model.NewLaunchModel
import com.emproto.hoabl.model.SmartDealsModel
import com.emproto.hoabl.model.TrendingModel
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.response.investment.ApData
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionOneModel
import com.emproto.networklayer.response.investment.PageManagementsOrCollectionTwoModel
import com.emproto.networklayer.response.investment.PageManagementsOrNewInvestment
import javax.inject.Inject

class CategoryListFragment() : BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    private lateinit var binding: FragmentCategoryListBinding
    private lateinit var categoryListAdapter: CategoryListAdapter
    private lateinit var sDList: List<PageManagementsOrCollectionOneModel>
    private lateinit var tPList: List<PageManagementsOrCollectionTwoModel>
    private lateinit var nLList: List<PageManagementsOrNewInvestment>
    private lateinit var aPList: List<ApData>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        initObserver()
    }

    private fun setUpViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory).get(InvestmentViewModel::class.java)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE
    }

    private fun initObserver() {
        investmentViewModel.getSmartDealsList().observe(viewLifecycleOwner, Observer {
            when(investmentViewModel.getSd().value){
                true -> {
                    binding.tvCategoryHeading.text = resources.getString(R.string.last_few_plots)
                    setUpAdapter("LastPlots", it)
                }
            }
        })
        investmentViewModel.getTrendingList().observe(viewLifecycleOwner, Observer {
            when(investmentViewModel.getTp().value){
                true -> {
                    binding.tvCategoryHeading.text = resources.getString(R.string.trending_projects)
                    setUpAdapter("TrendingProjects", it)
                }
            }
        })
        investmentViewModel.getNewInvestments().observe(viewLifecycleOwner, Observer {
            when(investmentViewModel.getNl().value){
                true -> {
                    binding.tvCategoryHeading.text = resources.getString(R.string.new_launches)
                    setUpAdapter("NewLaunches", it)
                }
            }
        })
        investmentViewModel.getAllInvestments().observe(viewLifecycleOwner, Observer {
            when(investmentViewModel.getAp().value){
                true -> {
                    binding.tvCategoryHeading.text = resources.getString(R.string.all_investments)
                    setUpAdapter("AllInvestments", it)
                }
            }
        })
    }

    private fun setUpAdapter(type:String,list: List<Any>){
        when (type) {
            "NewLaunches" -> {
                setUpCategoryAdapter(list, 0)
            }
            "LastPlots" -> {
                setUpCategoryAdapter(list, 1)
            }
            "TrendingProjects" -> {
                setUpCategoryAdapter(list, 2)
            }
            "AllInvestments" -> {
                setUpCategoryAdapter(list, 3)
            }
            else -> {
                setUpCategoryAdapter(list, 4)
            }
        }
    }

    private fun setUpCategoryAdapter(list: List<Any>, type: Int) {
        categoryListAdapter = CategoryListAdapter(this.requireContext(), list, itemClickListener,type)
        binding.rvCategoryList.adapter = categoryListAdapter
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            //investmentViewModel.setProjectId(item.toInt())
            //(requireActivity() as HomeActivity).addFragment(ProjectDetailFragment(),false)
            val bundle = Bundle()
            bundle.putInt("ProjectId", item.toInt())
            val fragment = ProjectDetailFragment()
            fragment.arguments = bundle
            (requireActivity() as HomeActivity).addFragment(
                fragment, false
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        investmentViewModel.setSd(false)
        investmentViewModel.setTp(false)
        investmentViewModel.setNl(false)
        investmentViewModel.setAp(false)
    }


}