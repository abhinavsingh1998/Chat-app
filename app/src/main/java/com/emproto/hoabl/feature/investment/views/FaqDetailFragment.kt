package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FaqDetailFragmentBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.investment.adapters.FaqDetailAdapter
import com.emproto.hoabl.model.RecyclerViewFaqItem
import com.emproto.hoabl.utils.Extensions.hideKeyboard
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.CgData
import javax.inject.Inject

class FaqDetailFragment : BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding: FaqDetailFragmentBinding

    @Inject
    lateinit var factory: ProfileFactory
    lateinit var profileViewModel: ProfileViewModel


    @Inject
    lateinit var appPreference: AppPreference

    private lateinit var adapter: FaqDetailAdapter
    private lateinit var allFaqList: List<CgData>
    private var isFromInvestment = true
    private var projectName = Constants.FAQS
    private var projectId = 0
    private var faqId = 0

    private lateinit var handler: Handler
    private var runnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FaqDetailFragmentBinding.inflate(layoutInflater)
        arguments?.let {
            projectId = it.getInt(Constants.PROJECT_ID)
            faqId = it.getInt(Constants.FAQ_ID)
            isFromInvestment = it.getBoolean(Constants.IS_FROM_INVESTMENT)
            projectName = it.getString(Constants.PROJECT_NAME).toString()
        }
        handler = Handler(Looper.getMainLooper())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitialization()
        callApi()
        eventTrackFaqDetail()
    }

    private fun eventTrackFaqDetail() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.FAQCARDDETAILEDPAGE
        )
    }

    private fun setUpInitialization() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(
                requireActivity(),
                investmentFactory
            )[InvestmentViewModel::class.java]
        profileViewModel =
            ViewModelProvider(requireActivity(), factory)[ProfileViewModel::class.java]
        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        binding.slSwipeRefresh.setOnRefreshListener {
            binding.slSwipeRefresh.isRefreshing = true
            callApi()
        }
    }

    private fun callApi() {
        when (isFromInvestment) {
            true -> {
                callProjectFaqApi()
            }
            false -> {
                callProfileFaqApi()
            }
        }
    }

    private fun callProfileFaqApi() {
        //UI setup
        (activity as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.GONE
        binding.blueHeader.visibility = View.VISIBLE
        //Getting general faqs
        profileViewModel.getGeneralFaqs(2001).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                    binding.errorText.visibility = View.GONE
                    binding.errorHeader.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.data?.let { data ->
                        allFaqList = data
                        setUpRecyclerView(data, faqId)
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        }
    }

    private fun callProjectFaqApi() {
        //UI setup
        (activity as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
        binding.blueHeader.visibility = View.GONE
        //Getting project faqs
        investmentViewModel.getInvestmentsFaq(projectId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    it.data?.data?.let { data ->
                        allFaqList = data
                        setUpRecyclerView(data, faqId)
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        }
    }

    private fun setUpRecyclerView(data: List<CgData>, faqId: Int) {
        if (data.isNotEmpty()) {  //If the api data is not empty
            binding.slSwipeRefresh.visibility = View.VISIBLE
            binding.slSwipeRefresh.isRefreshing = false
            val list = ArrayList<RecyclerViewFaqItem>()
            list.add(RecyclerViewFaqItem(1, data[0])) //Adding category
            for (item in data) {
                list.add(RecyclerViewFaqItem(2, item)) //Adding faq holder items
            }
            adapter = FaqDetailAdapter(
                this,
                list,
                data,
                faqId,
                itemClickListener,
                projectName = projectName,
                fromInvestment = isFromInvestment,
                handler = handler,
                runnable = runnable
            )
            binding.rvFaq.adapter = adapter
        } else {
            binding.errorText.visibility = View.VISIBLE
            binding.errorHeader.visibility = View.VISIBLE
            binding.imgArrow.setOnClickListener {
                (activity as HomeActivity).onBackPressed()
            }
            binding.tvFaqTitle.setOnClickListener {
                (activity as HomeActivity).onBackPressed()
            }
        }
    }

    val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(
            view: View,
            position: Int,
            item: String) {
            when (view.id) {
                R.id.cv_category_name -> {
                    binding.rvFaq.smoothScrollToPosition(position + 1)
                }
                R.id.et_search -> {
                    hideKeyboard()
                    sendFilteredData(item)
                }
                R.id.iv_faq_card_drop_down -> {
                    binding.rvFaq.smoothScrollToPosition(position)
                }
                R.id.imgArrow -> {
                    (activity as HomeActivity).onBackPressed()
                }
                R.id.tv_faq_title -> {
                    (activity as HomeActivity).onBackPressed()
                }
            }
        }
    }

    private fun sendFilteredData(item: String) {
        when {
            item == "" -> {
                hideKeyboard()
                setUpRecyclerView(allFaqList, faqId)
            }
            item.isEmpty() -> {
                Toast.makeText(
                    this.requireContext(),
                    "Search text cant be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                val list = ArrayList<RecyclerViewFaqItem>()
                list.add(RecyclerViewFaqItem(1, allFaqList[0]))
                val searchString = item
                var isFaqPresent = false
                for (item in allFaqList) { //Comparing search text with api data
                    for (element in item.faqs) {
                        if (element.faqQuestion.question.contains(
                                searchString.trim(),
                                true
                            ) || item.name.contains(searchString.trim(), true)
                        ) {
                            isFaqPresent = true
                        }
                    }
                    when (isFaqPresent) {
                        true -> {
                            list.add(RecyclerViewFaqItem(2, item))
                            isFaqPresent = false
                        }
                        else -> {}
                    }
                }
                when (list.size) {
                    1 -> {
                        Toast.makeText(requireContext(), "No faqs to show", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                setAdapter(list, item)
            }
        }
    }

    private fun setAdapter(
        list: ArrayList<RecyclerViewFaqItem>,
        item: String
    ) {
        var isItemsPresent = false
        for (item in list) { //Checking if the data is present or not
            if (item.viewType == 2) {
                isItemsPresent = true
            }
        }
        when (isItemsPresent) {
            false -> {
                hideKeyboard()
            }
            else -> {

            }
        }
        adapter = FaqDetailAdapter(
            this,
            list,
            this.allFaqList,
            faqId,
            itemClickListener,
            item,
            projectName,
            isFromInvestment, handler, runnable
        )
        binding.rvFaq.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        runnable?.let { handler.removeCallbacks(it) }
    }
}