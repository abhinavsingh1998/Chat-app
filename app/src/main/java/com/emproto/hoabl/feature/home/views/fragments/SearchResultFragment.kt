package com.emproto.hoabl.feature.home.views.fragments


import android.Manifest
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentSearchResultBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.adapters.AllInsightsAdapter
import com.emproto.hoabl.feature.home.adapters.AllLatestUpdatesAdapter
import com.emproto.hoabl.feature.home.data.LatesUpdatesPosition
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.investment.adapters.CategoryListAdapter
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.feature.investment.views.ProjectDetailFragment
import com.emproto.hoabl.feature.portfolio.adapters.DocumentInterface
import com.emproto.hoabl.feature.portfolio.adapters.DocumentsAdapter
import com.emproto.hoabl.feature.portfolio.adapters.PortfolioSpecificViewAdapter
import com.emproto.hoabl.feature.portfolio.views.DocViewerFragment
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.utils.Extensions.hideKeyboard
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.preferences.AppPreference
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

    lateinit var documentAdapter: DocumentsAdapter

    private var isReadPermissonGranted: Boolean = false
    private var isWritePermissonGranted: Boolean = false
    private var topText = ""
    val faqList = ArrayList<ProjectContentsAndFaq>()
    val docList = ArrayList<Data>()
    private var base64Data = ""
    private val permissionRequest: MutableList<String> = ArrayList()
    lateinit var handler: Handler
    private var runnable: Runnable? = null
    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentSearchResultBinding = FragmentSearchResultBinding.inflate(layoutInflater)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
        handler = Handler(Looper.getMainLooper())

        arguments.let {
            if (it != null) {
                topText = it.getString(Constants.TOP_TEXT).toString()
            }
        }
        fragmentSearchResultBinding.searchLayout.search.setText("")
        return fragmentSearchResultBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
        initClickListeners()
        callSearchApi("", false)
    }

    private fun checkSearchedText() {
        val searchString = homeViewModel.getSearchedText().value
        when (searchString?.trim()) {
            "" -> {
                callSearchApi("", false)
            }
            null -> {
                callSearchApi("", false)
            }
            else -> {
                callSearchApi(searchString.toString(), true)
            }
        }
    }

    private fun initClickListeners() {
        fragmentSearchResultBinding.searchLayout.search.requestFocus()
        fragmentSearchResultBinding.searchLayout.imageBack.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }
        fragmentSearchResultBinding.searchLayout.ivCloseImage.setOnClickListener {
            fragmentSearchResultBinding.searchLayout.search.setText("")
        }
        fragmentSearchResultBinding.searchLayout.rotateText.text = topText

        fragmentSearchResultBinding.searchLayout.search.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val p0 = fragmentSearchResultBinding.searchLayout.search.text.toString()
                    MixpanelSearch();
                    if (p0.toString() != "" && p0.toString().length > 1) {
                        fragmentSearchResultBinding.searchLayout.ivCloseImage.visibility =
                            View.VISIBLE
                        runnable = Runnable {
                            callSearchApi(p0.toString(), true)
                        }
                        runnable?.let { it1 -> handler.postDelayed(it1, 1000) }

                    } else
                        return true
                }
                return false
            }

        })
    }

    private fun MixpanelSearch() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(),"Search")
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
//                fragmentSearchResultBinding.searchLayout.rotateText.text = showHTMLText(
//                    "$totalAmtLandSold    $totalLandsold    $grossWeight    $num_User"
//                )

            }
        }

        fragmentSearchResultBinding.searchLayout.search.addTextChangedListener(object :
            TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (p0.toString().isEmpty() || p0.toString().isBlank()) {
//                    fragmentSearchResultBinding.searchLayout.ivCloseImage.visibility = View.GONE
//                }
                if (p0.toString().isEmpty()) {
                    fragmentSearchResultBinding.searchLayout.ivCloseImage.visibility = View.GONE
                    runnable = Runnable {
                        callSearchApi("", false)
                    }
                    runnable?.let { it1 -> handler.postDelayed(it1, 2000) }
                } else if (p0.toString() != "" && p0.toString().length > 1) {
                    fragmentSearchResultBinding.searchLayout.ivCloseImage.visibility = View.VISIBLE
                    runnable = Runnable {
                        callSearchApi(p0.toString().trim(), true)
                    }
                    runnable?.let { it1 -> handler.postDelayed(it1, 2000) }

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun callSearchApi(searchWord: String, searchStringPresent: Boolean) {
        homeViewModel.getSearchResult(searchWord.trim()).observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    fragmentSearchResultBinding.progressBar.show()
                }
                Status.SUCCESS -> {
                    fragmentSearchResultBinding.progressBar.hide()
                    it.data?.data?.let { data ->
                        fragmentSearchResultBinding.nsvSearchInfo.visibility = View.VISIBLE
                        fragmentSearchResultBinding.clOuterLayout.visibility = View.VISIBLE

                        //Projects
                        when (data.projectContentData.size) {
                            0 -> {
                                fragmentSearchResultBinding.tvProject.visibility = View.GONE
                                fragmentSearchResultBinding.projectList.visibility = View.GONE
                            }
                            else -> {
                                fragmentSearchResultBinding.tvProject.visibility = View.VISIBLE
                                fragmentSearchResultBinding.projectList.visibility = View.VISIBLE
                                val allProjectList = data.projectContentData
                                when {
                                    allProjectList.size > 3 -> {
                                        when (searchStringPresent) {
                                            true -> {
                                                setUpProjectAdapter(data.projectContentData)
                                            }
                                            false -> {
                                                val projectShowList = ArrayList<ApData>()
                                                for (i in 0..2) {
                                                    projectShowList.add(allProjectList[i])
                                                }
                                                setUpProjectAdapter(projectShowList)
                                            }
                                        }
                                    }
                                    else -> {
                                        setUpProjectAdapter(data.projectContentData)
                                    }
                                }
                            }
                        }

                        //Latest Updates
                        homeViewModel.setLatestUpdatesData(data.marketingUpdateData)
                        val allLatestUpdateList = data.marketingUpdateData
                        when (allLatestUpdateList.size) {
                            0 -> {
                                fragmentSearchResultBinding.tvLatestUpdates.visibility = View.GONE
                                fragmentSearchResultBinding.latestUpdatesList.visibility = View.GONE
                            }
                            else -> {
                                fragmentSearchResultBinding.tvLatestUpdates.visibility =
                                    View.VISIBLE
                                fragmentSearchResultBinding.latestUpdatesList.visibility =
                                    View.VISIBLE
                                when {
                                    allLatestUpdateList.size > 3 -> {
                                        when (searchStringPresent) {
                                            true -> {
                                                setUpLatestUpdateAdapter(allLatestUpdateList)
                                            }
                                            false -> {
                                                val luShowList =
                                                    ArrayList<com.emproto.networklayer.response.marketingUpdates.Data>()
                                                for (i in 0..2) {
                                                    luShowList.add(allLatestUpdateList[i])
                                                }
                                                setUpLatestUpdateAdapter(luShowList)
                                            }
                                        }
                                    }
                                    else -> {
                                        setUpLatestUpdateAdapter(allLatestUpdateList)
                                    }
                                }
                            }
                        }

                        //Insights
                        val allInsightsList = data.insightsData
                        when (allInsightsList.size) {
                            0 -> {
                                fragmentSearchResultBinding.tvInsights.visibility = View.GONE
                                fragmentSearchResultBinding.insightsList.visibility = View.GONE
                            }
                            else -> {
                                fragmentSearchResultBinding.tvInsights.visibility = View.VISIBLE
                                fragmentSearchResultBinding.insightsList.visibility = View.VISIBLE
                                when {
                                    allInsightsList.size > 3 -> {
                                        when (searchStringPresent) {
                                            true -> {
                                                setUpInsightsAdapter(allInsightsList)
                                            }
                                            false -> {
                                                val showInsightsList =
                                                    ArrayList<com.emproto.networklayer.response.insights.Data>()
                                                for (i in 0..2) {
                                                    showInsightsList.add(allInsightsList[i])
                                                }
                                                setUpInsightsAdapter(showInsightsList)
                                            }
                                        }
                                    }
                                    else -> {
                                        setUpInsightsAdapter(allInsightsList)
                                    }
                                }
                            }
                        }

                        //Faq
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
                                when {
                                    faqList.size > 3 -> {
                                        when (searchStringPresent) {
                                            true -> {
                                                setUpFaqAdapter(faqList)
                                            }
                                            false -> {
                                                val filteredFaqList = faqList.filter {
                                                    it.frequentlyAskedQuestion.typeOfFAQ == "3001" //General faq
                                                }
//                                                Log.d("DDDD", filteredFaqList.toString())
                                                val showFaqList = ArrayList<ProjectContentsAndFaq>()
                                                when {
                                                    filteredFaqList.size > 3 -> {
                                                        for (i in 0..2) {
                                                            showFaqList.add(filteredFaqList[i])
                                                        }
                                                        setUpFaqAdapter(showFaqList)
                                                    }
                                                    else -> {
                                                        setUpFaqAdapter(filteredFaqList)
                                                    }
                                                }

                                            }
                                        }
                                    }
                                    else -> {
                                        val filteredFaqList = faqList.filter {
                                            it.frequentlyAskedQuestion.typeOfFAQ == "3001"
                                        }
                                        if (filteredFaqList.isEmpty()) {
                                            fragmentSearchResultBinding.tvFaq.visibility = View.GONE
                                            fragmentSearchResultBinding.faqsList.visibility =
                                                View.GONE
                                        }
                                        setUpFaqAdapter(filteredFaqList)
                                    }
                                }
                            }
                        }

                        //Documents
                        callDocsApi(
                            searchWord,
                            data.projectContentData,
                            data.faqData,
                            data.marketingUpdateData,
                            data.insightsData,
                            searchStringPresent
                        )

//                        if (data.projectContentData.isEmpty() && data.faqData.isEmpty() && data.marketingUpdateData.isEmpty() && data.insightsData.isEmpty()) {
//                            fragmentSearchResultBinding.tvNoData.visibility = View.VISIBLE
//                        } else {
//                            fragmentSearchResultBinding.tvNoData.visibility = View.GONE
//                        }
                    }
                }
                Status.ERROR -> {
                    fragmentSearchResultBinding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        })
    }

    private fun setUpProjectAdapter(projectContentData: List<ApData>) {
        val projectListAdapter = CategoryListAdapter(
            requireContext(),
            projectContentData,
            itemClickListener,
            3
        )
        fragmentSearchResultBinding.projectList.adapter = projectListAdapter
    }

    private fun setUpFaqAdapter(showFaqList: List<ProjectContentsAndFaq>) {
        val faqAdapter = SearchFaqAdapter(
            requireContext(),
            showFaqList,
            investmentScreenInterface
        )
        fragmentSearchResultBinding.faqsList.adapter = faqAdapter
    }

    private fun setUpInsightsAdapter(showInsightsList: List<com.emproto.networklayer.response.insights.Data>) {
        val insightsAdapter = AllInsightsAdapter(requireActivity(),
            showInsightsList.size,
            showInsightsList,
            object : AllInsightsAdapter.InsightsItemsInterface {
                override fun onClickItem(position: Int) {
                    homeViewModel.setSeLectedInsights(showInsightsList[position])
                    homeViewModel.setSearchedText(fragmentSearchResultBinding.searchLayout.search.text.toString())
                    fragmentSearchResultBinding.searchLayout.search.setText("")
                    (requireActivity() as HomeActivity).addFragment(
                        InsightsDetailsFragment(),
                        true
                    )
                }
            }
        )
        fragmentSearchResultBinding.insightsList.adapter = insightsAdapter
    }

    private fun setUpLatestUpdateAdapter(luList: List<com.emproto.networklayer.response.marketingUpdates.Data>) {
        val allLatestUpdatesAdapter = AllLatestUpdatesAdapter(requireContext(), luList, luList.size,
            object : AllLatestUpdatesAdapter.UpdatesItemsInterface {
                override fun onClickItem(position: Int) {
//                    Log.d("eee", position.toString())
                    homeViewModel.setSeLectedLatestUpdates(luList[position])
                    homeViewModel.setSelectedPosition(
                        LatesUpdatesPosition(
                            position,
                            luList.size
                        )
                    )
                    homeViewModel.setSearchedText(fragmentSearchResultBinding.searchLayout.search.text.toString())
                    fragmentSearchResultBinding.searchLayout.search.setText("")
                    (requireActivity() as HomeActivity).addFragment(
                        LatestUpdatesDetailsFragment(),
                        true
                    )
                }

            })
        fragmentSearchResultBinding.latestUpdatesList.adapter = allLatestUpdatesAdapter
    }

    private fun callDocsApi(
        searchWord: String,
        projectContentData: List<ApData>,
        faqData: List<FrequentlyAskedQuestion>,
        marketingUpdateData: List<com.emproto.networklayer.response.marketingUpdates.Data>,
        insightsData: List<com.emproto.networklayer.response.insights.Data>,
        searchStringPresent: Boolean
    ) {
        homeViewModel.getSearchDocResult(searchWord.trim()).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    fragmentSearchResultBinding.progressBar.show()
                }
                Status.SUCCESS -> {
                    fragmentSearchResultBinding.progressBar.hide()
                    it.data?.data?.let { data ->
                        if (!data.isNullOrEmpty()) {
                            when (data.size) {
                                0 -> {
                                    fragmentSearchResultBinding.tvDocuments.visibility = View.GONE
                                    fragmentSearchResultBinding.documentsList.visibility = View.GONE
                                }
                                else -> {
//                                    Log.d("getget", data.toString())
                                    fragmentSearchResultBinding.tvDocuments.visibility =
                                        View.VISIBLE
                                    fragmentSearchResultBinding.documentsList.visibility =
                                        View.VISIBLE
                                    fragmentSearchResultBinding.tvNoData.visibility = View.GONE
                                    docList.clear()
                                    for (item in data) {
                                        docList.add(item)
                                    }
                                    when {
                                        docList.size > 3 -> {
                                            when (searchStringPresent) {
                                                true -> {
                                                    setupDocAdapter(docList)
                                                }
                                                false -> {
                                                    val showDocList = ArrayList<Data>()
                                                    for (i in 0..2) {
                                                        showDocList.add(docList[i])
                                                    }
                                                    setupDocAdapter(showDocList)
                                                }
                                            }
                                        }
                                        else -> {
                                            setupDocAdapter(docList)
                                        }
                                    }
                                }
                            }
                        } else {
                            fragmentSearchResultBinding.tvDocuments.visibility = View.GONE
                            fragmentSearchResultBinding.documentsList.visibility = View.GONE
                            if (projectContentData.isEmpty() && faqData.isEmpty() && marketingUpdateData.isEmpty() && insightsData.isEmpty()) {
                                hideKeyboard()
                                (requireActivity() as HomeActivity).navigate(R.id.navigation_investment)
                            } else {
                                fragmentSearchResultBinding.tvNoData.visibility = View.GONE
                            }
                        }

                    }
                }
                Status.ERROR -> {
                    fragmentSearchResultBinding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        })
    }

    private fun setupDocAdapter(showDocList: List<Data>) {
        documentAdapter = DocumentsAdapter(showDocList, false, ivinterface)
        fragmentSearchResultBinding.documentsList.adapter =
            documentAdapter
    }

    private fun initView() {
        (requireActivity() as HomeActivity).hideBottomNavigation()
        (requireActivity() as HomeActivity).hideHeader()
//        fragmentSearchResultBinding.searchLayout.rotateText.text = " "
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
            when (position) {
                0 -> {
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, item.toInt())
                    val fragment = ProjectDetailFragment()
                    fragment.arguments = bundle
                    homeViewModel.setSearchedText(fragmentSearchResultBinding.searchLayout.search.text.toString())
                    fragmentSearchResultBinding.searchLayout.search.setText("")
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
                1 -> {
                    val fragment = LandSkusFragment()
                    val bundle = Bundle()
                    bundle.putInt(Constants.PROJECT_ID, item.toInt())
                    fragment.arguments = bundle
                    homeViewModel.setSearchedText(fragmentSearchResultBinding.searchLayout.search.text.toString())
                    fragmentSearchResultBinding.searchLayout.search.setText("")
                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                }
            }
        }
    }

    val ivinterface = object : DocumentInterface {
        override fun onclickDocument(name: String, path: String) {
            when (path) {
                "" -> {
                    Toast.makeText(
                        requireContext(),
                        Constants.NO_DATA_AVAILABLE,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    when {
                        name.contains(Constants.JPG_SMALL, true) -> {
                            //open image loading screen
                            openDocument(name, path)
                        }
                        name.contains(Constants.PNG_SMALL, true) -> {
                            //open image loading screen
                            openDocument(name, path)
                        }
                        name.contains(Constants.PDF, false) -> {
                            getDocumentData(path)
                        }
                        else -> {
                            Toast.makeText(context, Constants.INVALID_FORMAT, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }

        }
    }

    fun getDocumentData(path: String) {
        homeViewModel.downloadDocument(path)
            .observe(viewLifecycleOwner,
                androidx.lifecycle.Observer {
                    when (it.status) {
                        Status.LOADING -> {
                            fragmentSearchResultBinding.progressBar.show()
                        }
                        Status.SUCCESS -> {
                            fragmentSearchResultBinding.progressBar.hide()
                            requestPermisson(it.data!!.data)
                        }
                        Status.ERROR -> {
                            fragmentSearchResultBinding.progressBar.hide()
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                        }
                    }
                })
    }

    private fun openDocument(name: String, path: String) {
        homeViewModel.setSearchedText(fragmentSearchResultBinding.searchLayout.search.text.toString())
        fragmentSearchResultBinding.searchLayout.search.setText("")
        (requireActivity() as HomeActivity).addFragment(
            DocViewerFragment.newInstance(true, name, path),
            true
        )
    }

    private fun requestPermisson(base64: String) {
        isReadPermissonGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isWritePermissonGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (!isReadPermissonGranted || !isWritePermissonGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            openPdf(base64)
        }
        if (permissionRequest.isNotEmpty()) {
            base64Data = base64
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }

    val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            isReadPermissonGranted =
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                    ?: isReadPermissonGranted
            isWritePermissonGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                ?: isWritePermissonGranted

            if (isReadPermissonGranted && isWritePermissonGranted) {
                openPdf(base64Data)
            }
        }

    private fun openPdf(stringBase64: String) {
        val file = Utility.writeResponseBodyToDisk(stringBase64)
        if (file != null) {
            val path = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + Constants.DOT_PROVIDER,
                file
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(path, Constants.APPLICATION_PDF)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(Constants.ERROR_OPEN_PDF, e.localizedMessage)
            }
        } else {
            (requireActivity() as HomeActivity).showErrorToast(Constants.SOMETHING_WENT_WRONG)
        }
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

            override fun onApplyInvestment(projectId: Int) {
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

            override fun onDocumentView(name: String, path: String) {
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        runnable?.let { handler.removeCallbacks(it) }
    }
}