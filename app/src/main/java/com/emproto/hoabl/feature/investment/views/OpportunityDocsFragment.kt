package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentOpportunityDocsBinding
import com.emproto.hoabl.feature.investment.adapters.OpportunityDocsAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.networklayer.response.investment.OpprotunityDoc

class OpportunityDocsFragment:BaseFragment() {

    lateinit var binding:FragmentOpportunityDocsBinding
    lateinit var opportunityDocsAdapter: OpportunityDocsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOpportunityDocsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getSerializable("OppDocData") as List<OpprotunityDoc>
        setUpUI()
        setUpRecyclerView(data)
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
    }

    private fun setUpRecyclerView(data: List<OpprotunityDoc>) {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(1))
        list.add(RecyclerViewItem(2))
        list.add(RecyclerViewItem(3))
        list.add(RecyclerViewItem(4))
        list.add(RecyclerViewItem(5))
        list.add(RecyclerViewItem(6))
        list.add(RecyclerViewItem(7))
        list.add(RecyclerViewItem(8))

        opportunityDocsAdapter = OpportunityDocsAdapter(this.requireContext(),list,data)
        binding.rvOppDocs.adapter = opportunityDocsAdapter
    }


}