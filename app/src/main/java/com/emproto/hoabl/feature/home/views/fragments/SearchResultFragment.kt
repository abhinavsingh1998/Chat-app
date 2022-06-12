package com.emproto.hoabl.feature.home.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.core.Database.TableModel.SearchModel

import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentSearchResultBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.SearchResultAdapter
import com.emproto.hoabl.feature.investment.adapters.CategoryListAdapter
import com.emproto.hoabl.feature.portfolio.adapters.DocumentInterface
import com.emproto.hoabl.feature.portfolio.adapters.DocumentsAdapter
import com.emproto.hoabl.feature.portfolio.adapters.PortfolioSpecificViewAdapter
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.documents.Data
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.ApData
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectContentsAndFaq
import javax.inject.Inject

class SearchResultFragment : BaseFragment() {

    lateinit var fragmentSearchResultBinding: FragmentSearchResultBinding

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    lateinit var searchResultAdapter: SearchResultAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    lateinit var faqAdapter: SearchFaqAdapter
    lateinit var projectListAdapter: CategoryListAdapter
    lateinit var documentAdapter: DocumentsAdapter

    companion object {
        fun newInstance(): SearchResultFragment {
            val fragment = SearchResultFragment()
            /*val bundle = Bundle()
            fragment.arguments = bundle*/
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentSearchResultBinding = FragmentSearchResultBinding.inflate(layoutInflater)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
        initObserver()
        initView()
        return fragmentSearchResultBinding.root
    }

    private fun initObserver() {
        homeViewModel.getSearchResult().observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let { data ->
                        val faqList = ArrayList<ProjectContentsAndFaq>()
                        for(item in data.faqData){
                            val pjd = ProjectContentsAndFaq("",0,item,0,0,0,"")
                            faqList.add(pjd)
                        }
                        val list = ArrayList<Data>()
                        list.add(Data("","Image",1,"","","",""))
                        list.add(Data("","Image",1,"","","",""))
                        list.add(Data("","Image",1,"","","",""))
                        list.add(Data("","Image",1,"","","",""))
                        setUpAdapter(faqList,data.projectContentData,list)
// for docs --> com.emproto.networklayer.response.documents

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

    private fun setrecentAdapter(t: List<SearchModel>) {

    }

    private fun initView() {
        (requireActivity() as HomeActivity).hideBottomNavigation()
    }

    private fun setUpAdapter(
        faqList: ArrayList<ProjectContentsAndFaq>,
        projectContentData: List<ApData>,
        list: ArrayList<Data>
    ) {
        fragmentSearchResultBinding.projectList.layoutManager =
            LinearLayoutManager(requireContext())
        projectListAdapter = CategoryListAdapter(requireContext(), projectContentData, itemClickListener,3)
        fragmentSearchResultBinding.projectList.adapter = projectListAdapter

        fragmentSearchResultBinding.documentsList.layoutManager =
            LinearLayoutManager(requireContext())
        documentAdapter = DocumentsAdapter(list,false,ivinterface)
        fragmentSearchResultBinding.documentsList.adapter = documentAdapter

        fragmentSearchResultBinding.faqsList.layoutManager = LinearLayoutManager(requireContext())
        faqAdapter = SearchFaqAdapter(requireContext(), faqList, investmentScreenInterface)
        fragmentSearchResultBinding.faqsList.adapter = faqAdapter
    }

    val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {

        }
    }

    val ivinterface = object:DocumentInterface{
        override fun onclickDocument(position: Int) {

        }
    }

    val investmentScreenInterface = object:PortfolioSpecificViewAdapter.InvestmentScreenInterface{
        override fun onClickFacilityCard() {

        }

        override fun seeAllCard() {

        }

        override fun seeProjectTimeline(id: Int) {
        }

        override fun seeBookingJourney(id: Int) {
        }

        override fun referNow() {
        }

        override fun seeAllSimilarInvestment() {
        }

        override fun onClickSimilarInvestment(project: Int) {
        }

        override fun onApplySinvestment(projectId: Int) {
        }

        override fun readAllFaq(position: Int, faqId: Int) {

        }

        override fun seePromisesDetails(position: Int) {
        }

        override fun moreAboutPromises() {
        }

        override fun seeProjectDetails(projectId: Int) {
        }

        override fun seeOnMap(latitude: String, longitude: String) {
        }

        override fun onClickImage(mediaViewItem: MediaViewItem, position: Int) {
        }

        override fun seeAllImages(imagesList: ArrayList<MediaViewItem>) {
        }

        override fun shareApp() {
        }

        override fun onClickAsk() {
        }

        override fun onDocumentView(position: Int) {
        }
    }

}