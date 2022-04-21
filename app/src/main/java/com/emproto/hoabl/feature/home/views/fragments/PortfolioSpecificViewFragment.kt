package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentPortfolioSpecificviewBinding
import com.emproto.hoabl.feature.home.adapters.PortfolioDocumentAdapter
import com.emproto.hoabl.feature.home.adapters.ProjectPromisesAdapter
import com.emproto.hoabl.feature.home.adapters.VideosAdapter
import com.emproto.hoabl.feature.profile.adapter.FaqAdapter
import com.emproto.hoabl.feature.investment.adapters.InvestmentAdapter

class PortfolioSpecificViewFragment : BaseFragment() {

    lateinit var binding: FragmentPortfolioSpecificviewBinding
    lateinit var adapter:PortfolioDocumentAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    lateinit var videosAdapter: VideosAdapter
    lateinit var projectPromiseAdapter:ProjectPromisesAdapter
    lateinit var faqAdapter: FaqAdapter
    lateinit var investmentAdapter: InvestmentAdapter


    companion object{
        fun newInstance(): PortfolioSpecificViewFragment {
            val fragment= PortfolioSpecificViewFragment();
         //   val bundle= Bundle();

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPortfolioSpecificviewBinding.inflate(layoutInflater)

        initView()
        initClickListeners()
        return binding.root
    }

    private fun initView() {
        val list:ArrayList<String> = ArrayList()
        list.add("document.pdf")
        list.add("documents.pdf")
        list.add("documents_1.pdf")
        list.add("documents_2.pdf")
        list.add("documents_2.pdf")
        list.add("documents_2.pdf")
        list.add("documents_2.pdf")

        adapter= PortfolioDocumentAdapter(requireContext(),list)
        linearLayoutManager= LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.documentsRecyclerview.layoutManager=linearLayoutManager
        binding.documentsRecyclerview.adapter=adapter

        videosAdapter= VideosAdapter(requireContext(),list)
        staggeredGridLayoutManager= StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.recyclerviewLatestVideos.layoutManager =staggeredGridLayoutManager
        binding.recyclerviewLatestVideos.setHasFixedSize(true)
        binding.recyclerviewLatestVideos.adapter=videosAdapter


        projectPromiseAdapter= ProjectPromisesAdapter(list)
        linearLayoutManager= LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.projectPromisesRecyclerview.layoutManager=linearLayoutManager
        binding.projectPromisesRecyclerview.adapter=projectPromiseAdapter

        faqAdapter= FaqAdapter(list)
        linearLayoutManager= LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        binding.recyclerviewFaq.layoutManager=linearLayoutManager
        binding.recyclerviewFaq.adapter=faqAdapter

        investmentAdapter= InvestmentAdapter(requireContext(),list)
        linearLayoutManager= LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        binding.smiliarInvestmentRecyclerview.layoutManager=linearLayoutManager
        binding.smiliarInvestmentRecyclerview.adapter=investmentAdapter
    }

    private fun initClickListeners() {

    }
}