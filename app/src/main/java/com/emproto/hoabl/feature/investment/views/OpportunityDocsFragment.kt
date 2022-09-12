package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentOpportunityDocsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.OpportunityDocsAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import javax.inject.Inject

class OpportunityDocsFragment:BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding:FragmentOpportunityDocsBinding
    private lateinit var opportunityDocsAdapter: OpportunityDocsAdapter
    private var title = ""
    private var projectId = 0
    private var isFromProjectAmenities = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOpportunityDocsBinding.inflate(layoutInflater)
        arguments.let {
            title = it?.getString(Constants.PROJECT_NAME).toString()
            projectId = it?.getInt(Constants.PROJECT_ID, 0)!!
            isFromProjectAmenities = it.getBoolean(Constants.IS_PROJECT_AMENITIES_CLICKED,false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpUI()
        setUpRecyclerView()
    }

    private fun initViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory)[InvestmentViewModel::class.java]
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).hideBottomNavigation()
        (requireActivity() as HomeActivity).showBackArrow()
    }

    private fun setUpRecyclerView() {
        binding.progressBar.visibility = View.VISIBLE
        investmentViewModel.getOpportunityDoc().observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            val list = ArrayList<RecyclerViewItem>()
            when (it.isBannerImageActive) {
                true -> {
                    list.add(RecyclerViewItem(1))
                }
                else -> {}
            }
            when (it.isEscalationGraphActive) {
                true -> {
                    list.add(RecyclerViewItem(2))
                }
                else -> {}
            }
            when (it.isCurrentInfraStoryActive) {
                true -> {
                    list.add(RecyclerViewItem(3))
                }
                else -> {}
            }
            when (it.isUpcomingInfraStoryActive) {
                true -> {
                    list.add(RecyclerViewItem(4))
                }
                else -> {}
            }
            when (it.isTourismAroundActive) {
                true -> {
                    list.add(RecyclerViewItem(5))
                }
                else -> {}
            }
            when (it.isAboutProjectsActive) {
                true -> {
                    list.add(RecyclerViewItem(6))
                }
                else -> {}
            }
            when (it.isProjectAminitiesActive) {
                true -> {
                    list.add(RecyclerViewItem(7))
                }
                else -> {}
            }
            list.add(RecyclerViewItem(8))

            opportunityDocsAdapter = OpportunityDocsAdapter(
                this.requireContext(),
                list,
                it,
                isFromProjectAmenities
            )
            binding.rvOppDocs.adapter = opportunityDocsAdapter
            opportunityDocsAdapter.setItemClickListener(onItemClickListener)
            when (isFromProjectAmenities) {
                true -> {
                    binding.rvOppDocs.scrollToPosition(list.size - 1)
                }
                else -> {}
            }
        }
    }

    val onItemClickListener =
        View.OnClickListener { view ->
            when(view.id){
                R.id.tv_apply_now -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, projectId)
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
            }
        }

}