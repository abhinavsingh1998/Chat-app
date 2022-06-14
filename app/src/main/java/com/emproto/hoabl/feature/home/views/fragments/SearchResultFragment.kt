package com.emproto.hoabl.feature.home.views.fragments


import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentSearchResultBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.SearchResultAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.CategoryListAdapter
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.feature.investment.views.ProjectDetailFragment
import com.emproto.hoabl.feature.portfolio.adapters.DocumentInterface
import com.emproto.hoabl.feature.portfolio.adapters.DocumentsAdapter
import com.emproto.hoabl.feature.portfolio.adapters.PortfolioSpecificViewAdapter
import com.emproto.hoabl.feature.portfolio.views.DocViewerFragment
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.documents.Data
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.ApData
import com.emproto.networklayer.response.portfolio.ivdetails.FrequentlyAskedQuestion
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectContentsAndFaq
import javax.inject.Inject


class SearchResultFragment : BaseFragment() {

    lateinit var fragmentSearchResultBinding: FragmentSearchResultBinding

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    lateinit var searchResultAdapter: SearchResultAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    //    lateinit var faqAdapter: SearchFaqAdapter
//    lateinit var projectListAdapter: CategoryListAdapter
    lateinit var documentAdapter: DocumentsAdapter

    private var topText = ""
    val faqList = ArrayList<ProjectContentsAndFaq>()
    val docList = ArrayList<Data>()

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

        arguments.let {
            if (it != null) {
                topText = it.getString("TopText").toString()
            }
        }
        return fragmentSearchResultBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
        initClickListeners()
        callSearchApi("")
    }

    private fun initClickListeners() {
        fragmentSearchResultBinding.searchLayout.search.requestFocus()
        fragmentSearchResultBinding.searchLayout.imageBack.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }
        fragmentSearchResultBinding.searchLayout.ivCloseImage.setOnClickListener {
            fragmentSearchResultBinding.searchLayout.search.setText("")
        }
    }

    private fun initObserver() {
        //for ticker
        homeViewModel.gethomeData().observe(viewLifecycleOwner) {
            it.let {
                val totalLandsold: String? = String.format(
                    getString(R.string.header),
                    it.data?.page?.mastheadSection?.totalSqftOfLandTransacted?.displayName,
                    it.data?.page?.mastheadSection?.totalSqftOfLandTransacted?.value + " Sqft"
                )
                //it.data?.page?.mastheadSection?.totalSqftOfLandTransacted?.displayName + " " + it.data?.page?.mastheadSection?.totalSqftOfLandTransacted?.value

                val totalAmtLandSold: String? = String.format(
                    getString(R.string.header),
                    it.data?.page?.mastheadSection?.totalAmoutOfLandTransacted?.displayName,
                    it.data?.page?.mastheadSection?.totalAmoutOfLandTransacted?.value
                )
                //it.data?.page?.mastheadSection?.totalAmoutOfLandTransacted?.displayName + " " + it.data?.page?.mastheadSection?.totalAmoutOfLandTransacted?.value
                val grossWeight: String? = String.format(
                    getString(R.string.header),
                    it.data?.page?.mastheadSection?.grossWeightedAvgAppreciation?.displayName,
                    it.data?.page?.mastheadSection?.grossWeightedAvgAppreciation?.value
                )
                //it.data?.page?.mastheadSection?.grossWeightedAvgAppreciation?.displayName + " " + it.data?.page?.mastheadSection?.grossWeightedAvgAppreciation?.value
                val num_User: String? = String.format(
                    getString(R.string.header),
                    it.data?.page?.mastheadSection?.totalNumberOfUsersWhoBoughtTheLand?.displayName,
                    it.data?.page?.mastheadSection?.totalNumberOfUsersWhoBoughtTheLand?.value
                )
                //it.data?.page?.mastheadSection?.totalNumberOfUsersWhoBoughtTheLand?.displayName + " " + it.data?.page?.mastheadSection?.totalNumberOfUsersWhoBoughtTheLand?.value
                fragmentSearchResultBinding.searchLayout.rotateText.text = showHTMLText(
                    "$totalAmtLandSold    $totalLandsold    $grossWeight    $num_User"
                )
            }
        }

        fragmentSearchResultBinding.searchLayout.search.addTextChangedListener(object :
            TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().isEmpty() || p0.toString().isBlank()){
                    fragmentSearchResultBinding.searchLayout.ivCloseImage.visibility = View.GONE
//                    callSearchApi("")
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString() != "" && p0.toString().length > 1) {
                    fragmentSearchResultBinding.searchLayout.ivCloseImage.visibility = View.VISIBLE
                    Handler().postDelayed({
                        callSearchApi(p0.toString())
                    }, 2000)
                } else if(p0.toString().isEmpty()) {
                    Handler().postDelayed({
                        callSearchApi("")
                    }, 2000)
                }
            }
        })
    }

    private fun callSearchApi(searchWord: String) {
        homeViewModel.getSearchResult(searchWord).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let { data ->
                        fragmentSearchResultBinding.nsvSearchInfo.visibility = View.VISIBLE
                        when (data.faqData.size) {
                            0 -> {
                                fragmentSearchResultBinding.tvFaq.visibility = View.GONE
                                fragmentSearchResultBinding.faqsList.visibility = View.GONE
                            }
                            else -> {
                                fragmentSearchResultBinding.tvFaq.visibility = View.VISIBLE
                                fragmentSearchResultBinding.faqsList.visibility = View.VISIBLE
                                faqList.clear()
                                for (item in data.faqData) {
                                    val pjd = ProjectContentsAndFaq("", 0, item, 0, 0, 0, "")
                                    faqList.add(pjd)
                                }
                                val faqAdapter = SearchFaqAdapter(
                                    requireContext(),
                                    faqList,
                                    investmentScreenInterface
                                )
                                fragmentSearchResultBinding.faqsList.adapter = faqAdapter
                            }
                        }
                        when (data.projectContentData.size) {
                            0 -> {
                                fragmentSearchResultBinding.tvProject.visibility = View.GONE
                                fragmentSearchResultBinding.projectList.visibility = View.GONE
                            }
                            else -> {
                                fragmentSearchResultBinding.tvProject.visibility = View.VISIBLE
                                fragmentSearchResultBinding.projectList.visibility = View.VISIBLE
                                val projectListAdapter = CategoryListAdapter(
                                    requireContext(),
                                    data.projectContentData,
                                    itemClickListener,
                                    3
                                )
                                fragmentSearchResultBinding.projectList.adapter = projectListAdapter
                            }
                        }

                        callDocsApi(searchWord,data.projectContentData,data.faqData)

                        if(data.projectContentData.isEmpty() && data.faqData.isEmpty()){
                            fragmentSearchResultBinding.tvNoData.visibility = View.VISIBLE
                        }else{
                            fragmentSearchResultBinding.tvNoData.visibility = View.GONE
                        }
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

    private fun callDocsApi(
        searchWord: String,
        projectContentData: List<ApData>,
        faqData: List<FrequentlyAskedQuestion>
    ){
        homeViewModel.getSearchDocResult(searchWord).observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let { data ->
                        if (!data.isNullOrEmpty()) {
                            when (data.size) {
                                0 -> {
                                    fragmentSearchResultBinding.tvDocuments.visibility = View.GONE
                                    fragmentSearchResultBinding.documentsList.visibility = View.GONE
                                }
                                else -> {
                                    Log.d("getget",data.toString())
                                    for (item in data) {
                                        docList.add(item)
                                    }
                                    documentAdapter = DocumentsAdapter(docList, false, ivinterface)
                                    fragmentSearchResultBinding.documentsList.adapter =
                                        documentAdapter
                                    when(docList.size){
                                        0 -> {
                                            fragmentSearchResultBinding.tvDocuments.visibility = View.GONE
                                            fragmentSearchResultBinding.documentsList.visibility = View.GONE
                                        }
                                    }
                                }
                            }
                        }else{
                            fragmentSearchResultBinding.tvDocuments.visibility = View.GONE
                            fragmentSearchResultBinding.documentsList.visibility = View.GONE
                            if(projectContentData.isEmpty() && faqData.isEmpty()){
                                fragmentSearchResultBinding.tvNoData.visibility = View.VISIBLE
                            }else{
                                fragmentSearchResultBinding.tvNoData.visibility = View.GONE
                            }
                        }
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

    private fun initView() {
        (requireActivity() as HomeActivity).hideBottomNavigation()
        (requireActivity() as HomeActivity).hideHeader()
        fragmentSearchResultBinding.searchLayout.rotateText.text = " "
        fragmentSearchResultBinding.searchLayout.rotateText.isSelected = true
        val inputMethodManager =
            (requireActivity() as HomeActivity).getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            fragmentSearchResultBinding.searchLayout.search,
            InputMethodManager.SHOW_IMPLICIT
        )
    }

    val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when(position){
                0 -> {
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", item.toInt())
                    val fragment = ProjectDetailFragment()
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(
                        fragment, false
                    )
                }
                1 -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt("ProjectId", item.toInt())
                    fragment.arguments = bundle
                    (requireActivity() as HomeActivity).addFragment(fragment, false)
                }
            }
        }
    }

    val ivinterface = object : DocumentInterface {
        override fun onclickDocument(position: Int) {
            openDocument(position)
        }
    }

    private fun openDocument(position: Int) {
        (requireActivity() as HomeActivity).addFragment(
            DocViewerFragment.newInstance("Doc Name", ""),
            false
        )
    }

    val investmentScreenInterface =
        object : PortfolioSpecificViewAdapter.InvestmentScreenInterface {
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