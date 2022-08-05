package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FaqDetailFragmentBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.FaqDetailAdapter
import com.emproto.hoabl.model.RecyclerViewFaqItem
import com.emproto.hoabl.utils.Extensions.hideKeyboard
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
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

    private lateinit var adapter: FaqDetailAdapter
    private lateinit var allFaqList: List<CgData>
    private var isFromInvestment = true
    private var projectName = "FAQs"
    private var projectId = 0
    private var faqId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FaqDetailFragmentBinding.inflate(layoutInflater)
        arguments?.let {
            projectId = it.getInt("ProjectId")
            faqId = it.getInt("FaqId")
            isFromInvestment = it.getBoolean("isFromInvestment")
            projectName = it.getString("ProjectName").toString()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitialization()
        callApi()
    }

    private fun setUpInitialization() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(
                requireActivity(),
                investmentFactory
            ).get(InvestmentViewModel::class.java)
        profileViewModel =
            ViewModelProvider(requireActivity(), factory)[ProfileViewModel::class.java]
        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.imageBack.visibility =
            View.VISIBLE
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
        (activity as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility = View.GONE
        binding.blueHeader.visibility = View.VISIBLE
        //Getting general faqs
        profileViewModel.getGeneralFaqs(2001).observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    binding.slSwipeRefresh.visibility = View.VISIBLE
                    binding.slSwipeRefresh.isRefreshing = false
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
        })
    }

    private fun callProjectFaqApi() {
        //UI setup
        (activity as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility = View.VISIBLE
        binding.blueHeader.visibility = View.GONE
        //Getting project faqs
        investmentViewModel.getInvestmentsFaq(projectId).observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    binding.slSwipeRefresh.visibility = View.VISIBLE
                    binding.slSwipeRefresh.isRefreshing = false
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
        })
    }

    private fun setUpRecyclerView(data: List<CgData>, faqId: Int) {
        if(data.isNotEmpty()){  //If the api data is not empty
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
                fromInvestment = isFromInvestment
            )
            binding.rvFaq.adapter = adapter
        }else{
            Toast.makeText(requireContext(), "No Faqs exist", Toast.LENGTH_SHORT).show()
        }
    }

    val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when (view.id) {
                R.id.cv_category_name -> {
                    binding.rvFaq.smoothScrollToPosition(position + 1)
                }
                R.id.et_search -> {
                    sendFilteredData(item)
                }
                R.id.iv_faq_card_drop_down -> {
                    binding.rvFaq.smoothScrollToPosition(position)
                }
                R.id.imgArrow -> {
                    (activity as HomeActivity).onBackPressed()
                }
            }
        }
    }

    private fun sendFilteredData(item: String) {
        when {
            item.isEmpty() -> {
                hideKeyboard()
                setUpRecyclerView(allFaqList, faqId)
            }
            else -> {
                val list = ArrayList<RecyclerViewFaqItem>()
                list.add(RecyclerViewFaqItem(1, allFaqList[0]))
                val searchString = item.toString()
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
                    when(isFaqPresent){
                        true -> {
                            list.add(RecyclerViewFaqItem(2, item))
                            isFaqPresent = false
                        }
                    }
                }
                setAdapter(list, allFaqList, item)
            }
        }
    }

    private fun setAdapter(
        list: ArrayList<RecyclerViewFaqItem>,
        allFaqList: List<CgData>,
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
            isFromInvestment
        )
        binding.rvFaq.adapter = adapter
    }
}