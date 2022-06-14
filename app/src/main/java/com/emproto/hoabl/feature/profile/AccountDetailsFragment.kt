package com.emproto.hoabl.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.FragmentAccountDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.profile.adapter.accounts.AccountsPaymentListAdapter
import com.emproto.hoabl.feature.profile.adapter.accounts.AccountsDocumentListAdapter
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.AccountsResponse
import javax.inject.Inject

class AccountDetailsFragment : Fragment(), AccountsDocumentListAdapter.OnKycItemClickListener,
    AccountsPaymentListAdapter.OnPaymentItemClickListener {

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentAccountDetailsBinding


    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible = true

        initClickListener()
        (requireActivity() as HomeActivity).hideBottomNavigation()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getAccountsList().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data?.data!!.documents != null && it.data!!.data.documents is List<AccountsResponse.Data.Document>) {

                        binding.rvKyc.layoutManager =
                            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                        binding.rvKyc.adapter = AccountsDocumentListAdapter(context,
                            it.data!!.data.documents as ArrayList<AccountsResponse.Data.Document>, this)


                    }

                    if (it.data?.data!!.paymentHistory != null && it.data!!.data.paymentHistory is List<AccountsResponse.Data.PaymentHistory>) {
                        binding.rvPaymentHistory.layoutManager =
                            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                        binding.rvPaymentHistory.adapter = AccountsPaymentListAdapter(context,
                            it.data!!.data.paymentHistory as ArrayList<AccountsResponse.Data.PaymentHistory>, this)

                    }
                }
                Status.ERROR -> {
                    binding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        })

    }



    private fun initClickListener() {
        binding.backAction.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }

        binding.tvSeeAllPaymentHistory.setOnClickListener {
            val allPaymentHistory = AllPaymentHistoryFragment()
            (requireActivity() as HomeActivity).addFragment(allPaymentHistory, false)
        }
    }

    override fun onAccountsKycItemClick(
        accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
        view: View,
        position: Int
    ) {

    }

    override fun onAccountsPaymentItemClick(
        accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
        view: View,
        position: Int
    ) {


    }


}