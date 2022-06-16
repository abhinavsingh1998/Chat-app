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
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.DocumentsBottomSheetBinding
import com.emproto.hoabl.databinding.FragmentAccountDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.portfolio.adapters.DocumentInterface
import com.emproto.hoabl.feature.portfolio.adapters.DocumentsAdapter
import com.emproto.hoabl.feature.portfolio.views.DocViewerFragment
import com.emproto.hoabl.feature.profile.adapter.accounts.AccountsDocumentLabelListAdapter
import com.emproto.hoabl.feature.profile.adapter.accounts.AccountsPaymentListAdapter
import com.emproto.hoabl.feature.profile.adapter.accounts.AccountsKycListAdapter
import com.emproto.hoabl.feature.profile.adapter.accounts.AllDocumentAdapter
import com.emproto.hoabl.utils.Extensions.toData
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.documents.Data
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.AccountsResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class AccountDetailsFragment : Fragment(), AccountsKycListAdapter.OnKycItemClickListener,
    AccountsDocumentLabelListAdapter.OnDocumentLabelItemClickListener,
    AccountsPaymentListAdapter.OnPaymentItemClickListener,
    AllDocumentAdapter.OnDocumentLabelClickListener {

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentAccountDetailsBinding
    val bundle = Bundle()
    lateinit var allPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>
    lateinit var allKycList: ArrayList<AccountsResponse.Data.Document>

    lateinit var documentBinding: DocumentsBottomSheetBinding
    lateinit var docsBottomSheet: BottomSheetDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true

        initView()
        initClickListener()
        (requireActivity() as HomeActivity).hideBottomNavigation()


        return binding.root
    }

    private fun initView() {
        documentBinding = DocumentsBottomSheetBinding.inflate(layoutInflater)
        docsBottomSheet =
            BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)
        docsBottomSheet.setContentView(documentBinding.root)

        documentBinding.ivDocsClose.setOnClickListener {
            docsBottomSheet.dismiss()
        }
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
                        allKycList =
                            it.data!!.data.documents as ArrayList<AccountsResponse.Data.Document>
                        if (allKycList.isNullOrEmpty()) {
                            binding.rvKyc.visibility = View.GONE
                            binding.tvKyc.visibility = View.GONE
                            binding.rvDocuments.visibility = View.GONE
                            binding.tvSeeAllDocuments.visibility = View.GONE
                        } else {
                            binding.rvKyc.visibility = View.VISIBLE
                            binding.tvKyc.visibility = View.VISIBLE
                            binding.rvKyc.layoutManager =
                                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                            binding.rvKyc.adapter = AccountsKycListAdapter(
                                context,
                                allKycList,
                                this
                            )
                            binding.rvDocuments.visibility = View.VISIBLE
                            binding.tvSeeAllDocuments.visibility = View.VISIBLE
                            binding.rvDocuments.layoutManager =
                                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                            binding.rvDocuments.adapter = AccountsDocumentLabelListAdapter(
                                context,
                                it.data!!.data.documents as ArrayList<AccountsResponse.Data.Document>,
                                this
                            )
                        }

                    }

                    if (it.data?.data!!.paymentHistory != null && it.data!!.data.paymentHistory is List<AccountsResponse.Data.PaymentHistory>) {
                        allPaymentList =
                            it.data!!.data.paymentHistory as ArrayList<AccountsResponse.Data.PaymentHistory>
                        if (allPaymentList.isNullOrEmpty()) {
                            binding.tvPaymentHistory.visibility = View.GONE
                            binding.tvSeeAllPayment.visibility = View.GONE
                            binding.rvPaymentHistory.visibility = View.GONE
                        } else {
                            binding.tvPaymentHistory.visibility = View.VISIBLE
                            binding.tvSeeAllPayment.visibility = View.VISIBLE
                            binding.rvPaymentHistory.visibility = View.VISIBLE
                            binding.rvPaymentHistory.layoutManager =
                                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                            binding.rvPaymentHistory.adapter = AccountsPaymentListAdapter(
                                context,
                                it.data!!.data.paymentHistory as ArrayList<AccountsResponse.Data.PaymentHistory>,
                                this
                            )
                        }


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

        binding.tvSeeAllPayment.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                "accountResponse",
                allPaymentList
            )
            val allPaymentHistoryFragment = AllPaymentHistoryFragment()
            allPaymentHistoryFragment.arguments = bundle
            (requireActivity() as HomeActivity).replaceFragment(
                allPaymentHistoryFragment.javaClass,
                "",
                true,
                bundle,
                null,
                0,
                false
            )
        }
        binding.tvSeeAllDocuments.setOnClickListener {
            docsBottomSheet.show()
            documentBinding.rvDocsItemRecycler.layoutManager =
                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            documentBinding.rvDocsItemRecycler.adapter = AllDocumentAdapter(
                context,
                allKycList,
                this
            )


        }
    }


    override fun onAccountsKycItemClick(
        accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
        view: View,
        position: Int
    ) {
        openDocument(position)
    }

    override fun onAccountsPaymentItemClick(
        accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
        view: View,
        position: Int
    ) {


    }
//
//    if (it.data.documentList != null) {
//        val adapter =
//            DocumentsAdapter(it.data.documentList, true, object : DocumentInterface {
//                override fun onclickDocument(position: Int) {
//                    docsBottomSheet.dismiss()
//                    openDocument(position)
//                }
//
//            })
//        documentBinding.rvDocsItemRecycler.adapter = adapter
//    }

    override fun onAccountsDocumentLabelItemClick(
        accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
        view: View,
        position: Int
    ) {
        docsBottomSheet.dismiss()
        openDocument(position)


    }

    private fun openDocument(position: Int) {
        (requireActivity() as HomeActivity).addFragment(
            DocViewerFragment.newInstance(true, "Test.ong"),
            false
        )
    }

    override fun onAccountsDocumentLabelClick(
        accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
        view: View,
        position: Int
    ) {
        docsBottomSheet.dismiss()
        openDocument(position)
    }


}