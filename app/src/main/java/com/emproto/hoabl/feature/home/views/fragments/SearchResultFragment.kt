package com.emproto.hoabl.feature.home.views.fragments


import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.core.Database.TableModel.SearchModel
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentSearchResultBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.SearchResultAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
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

//    lateinit var faqAdapter: SearchFaqAdapter
//    lateinit var projectListAdapter: CategoryListAdapter
    lateinit var documentAdapter: DocumentsAdapter

    private var topText = ""
    val faqList = ArrayList<ProjectContentsAndFaq>()
    val projectList = ArrayList<Data>()

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
        fragmentSearchResultBinding.searchLayout.rotateText.text = " "
        fragmentSearchResultBinding.searchLayout.rotateText.isSelected = true
        arguments.let {
            if (it != null) {
                topText = it.getString("TopText").toString()
            }
        }
        initObserver()
        initView()
        initClickListeners()
        return fragmentSearchResultBinding.root
    }

    private fun initClickListeners() {
        fragmentSearchResultBinding.searchLayout.search.requestFocus()
        fragmentSearchResultBinding.searchLayout.imageBack.setOnClickListener{
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
//                if (p0.toString().isNotEmpty()) {
//                    Handler().postDelayed({
//                        callSearchApi(p0.toString())
//                    },2000)
//                } else {
//
//                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //get the fragment
                if (p0.toString().isNotEmpty()) {
                    if(p0.toString() != ""){
                        fragmentSearchResultBinding.searchLayout.ivCloseImage.visibility = View.VISIBLE
                    }
                    Handler().postDelayed({
                        callSearchApi(p0.toString())
                    },2000)
                } else {

                }
            }

        })


    }

    private fun callSearchApi(searchWord:String){
        homeViewModel.getSearchResult(searchWord).observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let { data ->
                        fragmentSearchResultBinding.nsvSearchInfo.visibility = View.VISIBLE
                        when{
                            data.faqData.isNotEmpty() -> {
                                fragmentSearchResultBinding.tvFaq.visibility = View.VISIBLE
                                faqList.clear()
                                for(item in data.faqData){
                                    val pjd = ProjectContentsAndFaq("",0,item,0,0,0,"")
                                    faqList.add(pjd)
                                }
                            }
                            data.faqData.isEmpty() -> {
                                fragmentSearchResultBinding.tvFaq.visibility = View.GONE
                            }
                            data.projectContentData.isEmpty() -> {
                                fragmentSearchResultBinding.tvProject.visibility = View.GONE
                            }
                            data.projectContentData.isNotEmpty() -> {

                            }
                            data.docsData.isNotEmpty() -> {
                                for(item in data.docsData){
                                    projectList.add(item)
                                }
                            }
                            data.docsData.isEmpty() -> {
                                fragmentSearchResultBinding.tvDocuments.visibility = View.GONE
                                fragmentSearchResultBinding.documentsList.visibility = View.GONE
                            }

                        }
//                        projectList.clear()
//                        projectList.add(Data("","Image",1,"","","",""))
//                        projectList.add(Data("","Image",1,"","","",""))
//                        projectList.add(Data("","Image",1,"","","",""))
//                        projectList.add(Data("","Image",1,"","","",""))
                        setUpAdapter(faqList,data.projectContentData,projectList)
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
        (requireActivity() as HomeActivity).hideHeader()
        val inputMethodManager = (requireActivity() as HomeActivity).getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(fragmentSearchResultBinding.searchLayout.search, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setUpAdapter(
        faqList: ArrayList<ProjectContentsAndFaq>,
        projectContentData: List<ApData>,
        list: ArrayList<Data>
    ) {
        fragmentSearchResultBinding.projectList.layoutManager =
            LinearLayoutManager(requireContext())
        val projectListAdapter = CategoryListAdapter(requireContext(), projectContentData, itemClickListener,3)
        fragmentSearchResultBinding.projectList.adapter = projectListAdapter

        fragmentSearchResultBinding.documentsList.layoutManager =
            LinearLayoutManager(requireContext())
        documentAdapter = DocumentsAdapter(list,false,ivinterface)
        fragmentSearchResultBinding.documentsList.adapter = documentAdapter

        when{
            faqList.isNotEmpty() -> {
                val faqAdapter = SearchFaqAdapter(requireContext(), faqList, investmentScreenInterface)
                fragmentSearchResultBinding.faqsList.layoutManager = LinearLayoutManager(requireContext())
                fragmentSearchResultBinding.faqsList.adapter = faqAdapter
            }
        }
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