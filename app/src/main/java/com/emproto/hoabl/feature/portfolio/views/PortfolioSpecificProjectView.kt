package com.emproto.hoabl.feature.portfolio.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPortfolioSpecificViewBinding
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.ProjectDetailAdapter
import com.emproto.hoabl.feature.investment.dialogs.ApplicationSubmitDialog
import com.emproto.hoabl.feature.investment.dialogs.ConfirmationDialog
import com.emproto.hoabl.feature.investment.views.mediagallery.MediaViewFragment
import com.emproto.hoabl.feature.portfolio.adapters.DocumentsAdapter
import com.emproto.hoabl.feature.portfolio.adapters.PortfolioSpecificViewAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class PortfolioSpecificProjectView : BaseFragment() {

    lateinit var binding: FragmentPortfolioSpecificViewBinding
    lateinit var portfolioSpecificViewAdapter: PortfolioSpecificViewAdapter

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
                    val docsBottomSheet =
                        BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)
                    docsBottomSheet.setContentView(R.layout.documents_bottom_sheet)
                    val list = arrayListOf<String>(
                        "1",
                        "2",
                        "3",
                        "4",
                        "1",
                        "2",
                        "3",
                        "4",
                        "1",
                        "2",
                        "3",
                        "4",
                        "1",
                        "2",
                        "3",
                        "4"
                    )
                    val adapter = DocumentsAdapter(list, true)
                    docsBottomSheet.findViewById<RecyclerView>(R.id.rv_docs_item_recycler)?.adapter =
                        adapter
                    docsBottomSheet.findViewById<ImageView>(R.id.iv_docs_close)
                        ?.setOnClickListener {
                            docsBottomSheet.dismiss()
                        }
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
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPortfolioSpecificViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(1))
        list.add(RecyclerViewItem(2))
        list.add(RecyclerViewItem(3))
        list.add(RecyclerViewItem(4))
        list.add(RecyclerViewItem(5))
        list.add(RecyclerViewItem(6))
        list.add(RecyclerViewItem(7))
        list.add(RecyclerViewItem(8))
        list.add(RecyclerViewItem(9))
        list.add(RecyclerViewItem(10))
        portfolioSpecificViewAdapter = PortfolioSpecificViewAdapter(this.requireContext(), list)
        binding.rvPortfolioSpecificView.adapter = portfolioSpecificViewAdapter
        portfolioSpecificViewAdapter.setItemClickListener(onPortfolioSpecificItemClickListener)
    }
}