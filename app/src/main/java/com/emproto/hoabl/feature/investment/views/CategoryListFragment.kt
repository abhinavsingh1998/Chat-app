package com.emproto.hoabl.feature.investment.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentCategoryListBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.investment.adapters.CategoryListAdapter
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.investment.*
import com.emproto.networklayer.response.portfolio.ivdetails.SimilarInvestment
import javax.inject.Inject

class CategoryListFragment : BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel

    private lateinit var binding: FragmentCategoryListBinding
    private lateinit var categoryListAdapter: CategoryListAdapter
    private var type: String? = ""
    private var projectName: String? = ""


    @Inject
    lateinit var appPreference: AppPreference

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
        type = arguments?.getString("Category")
        projectName=arguments?.getString("ProjectName","")
        setUpViewModel()
        initObserver()
        (requireActivity() as HomeActivity).showHeader()

        eventTrackingSimilarInvestmentApplyNow()
    }

    private fun eventTrackingSimilarInvestmentApplyNow() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.SIMILARINVESTMENTSCARDAPPLYNOW)
    }

    private fun setUpViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(
                requireActivity(),
                investmentFactory
            )[InvestmentViewModel::class.java]
        (requireActivity() as HomeActivity).hideBottomNavigation()
        (requireActivity() as HomeActivity).showBackArrow()
    }

    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        when (type) {
            "LastFewPLots" -> {
               binding.tvCategoryHeading.text = projectName

                val data =
                    arguments?.getSerializable("LastFewPLotsData") as List<PageManagementsOrCollectionOneModel>
                setUpCategoryAdapter(data, 1)
            }
            "TrendingProjects" -> {
                binding.tvCategoryHeading.text = projectName
                val data =
                    arguments?.getSerializable("TrendingProjectsData") as List<PageManagementsOrCollectionTwoModel>
                setUpCategoryAdapter(data, 2)
            }
            "NewLaunches" -> {
                binding.tvCategoryHeading.text = resources.getString(R.string.new_launches)
                val data =
                    arguments?.getSerializable("NewLaunchesData") as List<PageManagementsOrNewInvestment>
                setUpCategoryAdapter(data, 0)
            }
            "AllInvestments" -> {
                binding.tvCategoryHeading.text = resources.getString(R.string.all_investments)
                val data =
                    arguments?.getSerializable("AllInvestmentsData") as List<ApData>
                setUpCategoryAdapter(data, 3)
            }
            "Watchlist" -> {
                binding.tvCategoryHeading.text = "My Watchlist"
                val data =
                    arguments?.getSerializable("WatchlistData") as List<Data>
                setUpCategoryAdapter(data, 4)
            }
            "Similar Investment" -> {
                binding.tvCategoryHeading.text = projectName
                val data =
                    arguments?.getSerializable("SimilarData") as List<SimilarInvestment>
                setUpCategoryAdapter(data, -1)
            }
            Constants.HOME -> {
                binding.tvCategoryHeading.text = "All Investments"
                val data =
                    arguments?.getSerializable("DiscoverAll") as List<com.emproto.networklayer.response.home.PageManagementsOrNewInvestment>
                setUpCategoryAdapter(data, 5)
            }
            "SimilarInvestments" -> {
                binding.tvCategoryHeading.text = projectName
                val data =
                    arguments?.getSerializable("SimilarInvestmentsData") as List<com.emproto.networklayer.response.investment.SimilarInvestment>
                setUpCategoryAdapter(data, 6)
            }

        }
    }

    private fun setUpCategoryAdapter(list: List<Any>, type: Int) {
        if (!list.isNullOrEmpty()) {
            categoryListAdapter =
                CategoryListAdapter(this.requireContext(), list, itemClickListener, type)
            binding.rvCategoryList.adapter = categoryListAdapter
        }
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(
            view: View,
            position: Int,
            item: String) {
            when (position) {
                0 -> {
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, item.toInt())
                    val fragment = ProjectDetailFragment()
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment, true
                    )
                }
                1 -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, item.toInt())
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
            }

        }
    }
}