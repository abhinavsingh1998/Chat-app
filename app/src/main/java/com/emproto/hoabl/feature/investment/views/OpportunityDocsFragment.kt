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
import com.emproto.hoabl.databinding.FragmentOpportunityDocsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.investment.adapters.OpportunityDocsAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.response.investment.OpprotunityDoc
import javax.inject.Inject

class OpportunityDocsFragment:BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding:FragmentOpportunityDocsBinding
    lateinit var opportunityDocsAdapter: OpportunityDocsAdapter

    val onItemClickListener =
        View.OnClickListener { view ->
            when(view.id){
                R.id.tv_apply_now -> {

                }
            }

        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOpportunityDocsBinding.inflate(layoutInflater)
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
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
    }

    private fun setUpRecyclerView() {
        investmentViewModel.getOpportunityDoc().observe(viewLifecycleOwner, Observer {
            val list = ArrayList<RecyclerViewItem>()
            list.add(RecyclerViewItem(1))
            list.add(RecyclerViewItem(2))
            list.add(RecyclerViewItem(3))
            list.add(RecyclerViewItem(4))
            list.add(RecyclerViewItem(5))
            list.add(RecyclerViewItem(6))
            list.add(RecyclerViewItem(7))
            list.add(RecyclerViewItem(8))

            opportunityDocsAdapter = OpportunityDocsAdapter(this.requireContext(),list,it)
            binding.rvOppDocs.adapter = opportunityDocsAdapter
        })

    }


}