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
    var projectId = 0
    var faqId  = 0
    private lateinit var allFaqList : List<CgData>
    private var isFromInvestment = true
    private var projectName = "FAQs"

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
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.imageBack.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).hideBottomNavigation()
        binding.slSwipeRefresh.setOnRefreshListener {
            binding.slSwipeRefresh.isRefreshing = true
            callApi()
        }
    }

    private fun callApi() {
        when(isFromInvestment){
            true -> {
                callProjectFaqApi()
            }
            false -> {
                callProfileFaqApi()
            }
        }
    }

    private fun callProfileFaqApi() {
        profileViewModel.getGeneralFaqs(2001).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    binding.slSwipeRefresh.visibility = View.VISIBLE
                    binding.slSwipeRefresh.isRefreshing = false
                    it.data?.data?.let {  data ->
                        Log.d("generalfaq",data.toString())
                        allFaqList = data
                        setUpRecyclerView(data,faqId)
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
        investmentViewModel.getInvestmentsFaq(projectId).observe(viewLifecycleOwner, Observer {
            Log.d("Faq", it.data.toString())
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
                        setUpRecyclerView(data,faqId)
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
        val list = ArrayList<RecyclerViewFaqItem>()
        list.add(RecyclerViewFaqItem(1, data[0]),)
        for (item in data) {
            list.add(RecyclerViewFaqItem(2, item))
        }
        adapter = FaqDetailAdapter(
            requireActivity(),
            this.requireContext(),
            list,
            data,
            faqId,
            itemClickListener,
            projectName = projectName
        )
        binding.rvFaq.adapter = adapter
    }

    val itemClickListener = object:ItemClickListener{
        override fun onItemClicked(view: View, position: Int, item: String) {
            when(view.id){
                R.id.cv_category_name -> {
                    binding.rvFaq.smoothScrollToPosition(position+1)
                }
                R.id.et_search -> {
                    sendFilteredData(item)
                }
//                R.id.iv_faq_card_drop_down -> {
//                    binding.rvFaq.smoothScrollToPosition(position+2)
//                }
            }
        }
    }

    private fun sendFilteredData(item: String) {
        when{
            item.isEmpty() -> {
                hideKeyboard()
                setUpRecyclerView(allFaqList,faqId)
            }
            else -> {
                val list = ArrayList<RecyclerViewFaqItem>()
                list.add(RecyclerViewFaqItem(1, allFaqList[0]))
                val searchString = item.toString()
                for (item in allFaqList) {
                    for(element in item.faqs){
                        if(element.faqQuestion.question.contains(searchString.trim(),true) || item.name.contains(searchString.trim(),true)){
                            list.add(RecyclerViewFaqItem(2, item))
                        }
                    }
                }
                setAdapter(list,allFaqList,item)
            }
        }
    }

    private fun setAdapter(
        list: ArrayList<RecyclerViewFaqItem>,
        allFaqList: List<CgData>,
        item: String
    ) {
        var isItemsPresent = false
        for(item in list){
            if(item.viewType == 2){
                isItemsPresent = true
            }
        }
        when(isItemsPresent){
            false -> {
                Toast.makeText(requireContext(), "No faqs to show", Toast.LENGTH_SHORT).show()
                hideKeyboard()
            }
            else -> {

            }
        }
        adapter = FaqDetailAdapter(requireActivity(),requireContext(), list, this.allFaqList, faqId,itemClickListener,item,projectName)
        binding.rvFaq.adapter = adapter
    }
}