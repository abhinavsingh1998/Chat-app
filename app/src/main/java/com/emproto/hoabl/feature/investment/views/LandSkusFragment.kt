package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentLandSkusBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.investment.adapters.LandSkusAdapter
import com.emproto.hoabl.feature.investment.dialogs.ApplicationSubmitDialog
import com.emproto.hoabl.feature.investment.dialogs.ConfirmationDialog
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.request.investment.AddInventoryBody
import com.emproto.networklayer.request.investment.VideoCallBody
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.Inventory
import javax.inject.Inject
import kotlin.collections.ArrayList

class LandSkusFragment:BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    private lateinit var binding: FragmentLandSkusBinding
    private lateinit var landSkusAdapter: LandSkusAdapter

    private var projectId = 0
    private var appliedList = ArrayList<Inventory>()
    private var notAppliedList = ArrayList<Inventory>()

    val onLandSkusItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.btn_apply_now -> {
                    val confirmationDialog = ConfirmationDialog(investmentViewModel,itemClickListener)
                    confirmationDialog.show(this.parentFragmentManager,"ConfirmationDialog")
                }
                R.id.cl_not_convinced -> {
                    callVideoCallApi()
                }
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentLandSkusBinding.inflate(layoutInflater)
        arguments?.let {
            projectId = it.getInt("ProjectId", 0)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        callApi()
        setUpRecyclerview()
    }

    private fun callApi() {
        investmentViewModel.getAllInventories(projectId).observe(viewLifecycleOwner,Observer{
            when (it.status) {
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    binding.clOuterLayout.visibility = View.VISIBLE
                    it.data?.data?.let { data ->
                        Log.d("hdhdhd",data.data.toString())
                        appliedList.clear()
                        notAppliedList.clear()
                        for(item in data.data){
                            when(item.isApplied){
                                true -> appliedList.add(item)
                                false -> notAppliedList.add(item)
                            }
                        }
                        setUpRecyclerview()
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

    private fun setUpUI() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory).get(InvestmentViewModel::class.java)
        (activity as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.imageBack.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).hideBottomNavigation()
        (requireActivity() as HomeActivity).showHeader()
    }

    private fun setUpRecyclerview() {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(1))
        when (appliedList.size) {
            0 -> {}
            else -> list.add(RecyclerViewItem(2))
        }
        list.add(RecyclerViewItem(3))
        landSkusAdapter =
            LandSkusAdapter(this, list, appliedList, notAppliedList, itemClickListener)
        binding.rvLandSkus.adapter = landSkusAdapter
        landSkusAdapter.setItemClickListener(onLandSkusItemClickListener)
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            when(item){
                "Yes" -> {
                    investmentViewModel.addInventory(AddInventoryBody(
                        inventoryBucketId = position,
                        launchPhaseId = projectId
                    )).observe(viewLifecycleOwner,Observer{
                        when(it.status){
                            Status.LOADING -> {
                                (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                            }
                            Status.SUCCESS -> {
                                (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                                it.data?.let { data ->
                                    val applicationSubmitDialog = ApplicationSubmitDialog("Thank you for your interest!","Our Project Manager will reach out to you in 24 hours!")
                                    applicationSubmitDialog.show(parentFragmentManager,"ApplicationSubmitDialog")
                                    callApi()
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
            }
            when(view.id){
                R.id.btn_apply_now -> {
                    openDialog()
                }
            }
        }
    }

    private fun openDialog() {
        val confirmationDialog = ConfirmationDialog(investmentViewModel,itemClickListener)
        confirmationDialog.show(this.parentFragmentManager,"ConfirmationDialog")
    }

    private fun callVideoCallApi() {
        investmentViewModel.scheduleVideoCall(
            VideoCallBody(caseType = "1003",
            description = "",
            issueType = "Schedule a video call",
            projectId= projectId)
        ).observe(viewLifecycleOwner,Observer{
            when (it.status) {
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let { data ->
                        val applicationSubmitDialog = ApplicationSubmitDialog(
                            "Video Call request sent successfully.",
                            "Our Project Manager will reach out to you soon!",
                            false
                        )
                        applicationSubmitDialog.show(parentFragmentManager, "ApplicationSubmitDialog")
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
}