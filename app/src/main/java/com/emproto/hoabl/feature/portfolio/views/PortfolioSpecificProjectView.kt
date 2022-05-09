package com.emproto.hoabl.feature.portfolio.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.DocumentsBottomSheetBinding
import com.emproto.hoabl.databinding.FragmentPortfolioSpecificViewBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.dialogs.ApplicationSubmitDialog
import com.emproto.hoabl.feature.portfolio.adapters.DocumentsAdapter
import com.emproto.hoabl.feature.portfolio.adapters.PortfolioSpecificViewAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.response.enums.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class PortfolioSpecificProjectView : BaseFragment() {

    lateinit var binding: FragmentPortfolioSpecificViewBinding
    lateinit var portfolioSpecificViewAdapter: PortfolioSpecificViewAdapter
    lateinit var documentBinding: DocumentsBottomSheetBinding
    lateinit var docsBottomSheet: BottomSheetDialog

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioviewmodel: PortfolioViewModel
    val list = ArrayList<RecyclerViewItem>()

    private val onPortfolioSpecificItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.btn_apply_now -> {
                    val applyDialog = ApplicationSubmitDialog(
                        "Request Sent",
                        "A relationship manager will get back to you to discuss more about it."
                    )
                    applyDialog.show(this.parentFragmentManager, "ApplicationThankingDialog")
                }
                R.id.tv_documents_see_all -> {

                    docsBottomSheet.show()
                }
                R.id.tv_view_timeline -> {
                    (requireActivity() as HomeActivity).addFragment(
                        ProjectTimelineFragment.newInstance(
                            "",
                            ""
                        ), false
                    )

                }
                R.id.tv_view_booking_journey -> {
                    (requireActivity() as HomeActivity).addFragment(
                        BookingjourneyFragment.newInstance(
                            "",
                            ""
                        ), false
                    )
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        binding = FragmentPortfolioSpecificViewBinding.inflate(layoutInflater)

        portfolioviewmodel = ViewModelProvider(
            requireActivity(),
            portfolioFactory
        )[PortfolioViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setUpRecyclerView()
        initView()
        initObserver()
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

    private fun initObserver() {
        portfolioviewmodel.getInvestmentDetails(1).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.loader.show()
                    binding.rvPortfolioSpecificView.hide()
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    binding.rvPortfolioSpecificView.show()
                    setUpRecyclerView()
                    fetchDocuments()
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    binding.rvPortfolioSpecificView.show()
                    setUpRecyclerView()
                    fetchDocuments()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }

            }
        })

    }

    private fun fetchDocuments() {
        portfolioviewmodel.getDocumentList(1).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    list.removeAt(3)
                    list.add(
                        3,
                        RecyclerViewItem(PortfolioSpecificViewAdapter.PORTFOLIO_DOCUMENTS, it.data!!.data)
                    )
                    portfolioSpecificViewAdapter.notifyItemChanged(3)
                    it.data?.let {
                        val adapter = DocumentsAdapter(it.data, true)
                        documentBinding.rvDocsItemRecycler.adapter = adapter
                    }

                }
                Status.ERROR -> {

                }

            }
        })
    }

    private fun setUpRecyclerView() {

        list.add(RecyclerViewItem(PortfolioSpecificViewAdapter.PORTFOLIO_SPECIFIC_VIEW_TYPE_ONE))
        list.add(RecyclerViewItem(PortfolioSpecificViewAdapter.PORTFOLIO_SPECIFIC_VIEW_TYPE_TWO))
        list.add(RecyclerViewItem(PortfolioSpecificViewAdapter.PORTFOLIO_SPECIFIC_VIEW_TYPE_THREE))
        list.add(RecyclerViewItem(PortfolioSpecificViewAdapter.PORTFOLIO_DOCUMENTS))
        list.add(RecyclerViewItem(PortfolioSpecificViewAdapter.PORTFOLIO_SPECIFIC_VIEW_TYPE_FIVE))
        list.add(RecyclerViewItem(PortfolioSpecificViewAdapter.PORTFOLIO_SPECIFIC_VIEW_TYPE_SIX))
        list.add(RecyclerViewItem(PortfolioSpecificViewAdapter.PORTFOLIO_SPECIFIC_VIEW_TYPE_SEVEN))
        list.add(RecyclerViewItem(PortfolioSpecificViewAdapter.PORTFOLIO_SPECIFIC_VIEW_TYPE_EIGHT))
        list.add(RecyclerViewItem(PortfolioSpecificViewAdapter.PORTFOLIO_SPECIFIC_VIEW_TYPE_NINE))
        list.add(RecyclerViewItem(PortfolioSpecificViewAdapter.PORTFOLIO_SPECIFIC_VIEW_TYPE_TEN))
        portfolioSpecificViewAdapter = PortfolioSpecificViewAdapter(this.requireContext(), list)
        binding.rvPortfolioSpecificView.adapter = portfolioSpecificViewAdapter
        portfolioSpecificViewAdapter.setItemClickListener(onPortfolioSpecificItemClickListener)
    }
}