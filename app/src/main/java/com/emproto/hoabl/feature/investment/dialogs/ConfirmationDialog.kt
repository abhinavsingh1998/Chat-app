package com.emproto.hoabl.feature.investment.dialogs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ApplyConfirmationDialogBinding
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.google.android.material.textview.MaterialTextView

class ConfirmationDialog(private val investmentViewModel: InvestmentViewModel) :DialogFragment(),View.OnClickListener {

    lateinit var binding: ApplyConfirmationDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ApplyConfirmationDialogBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.all_corner_radius_white_bg)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
        setUpUI()
    }

    private fun setUpUI() {
        investmentViewModel.getSku().observe(viewLifecycleOwner, Observer {
            it.let { data ->
                binding.apply {
                    tvItemLandSkusName.text = data.name
                    tvItemLandSkusArea.text = "${data.areaRange.from} - ${data.areaRange.to} ft"
                    tvItemLandSkusPrice.text = "${data.priceRange.from} - ${data.priceRange.to}"
                    tvItemLandSkusDescription.text = data.shortDescription
                }
            }
        })
    }

    private fun clickListeners() {
        binding.tvYesText.setOnClickListener(this)
        binding.tvNoText.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onClick(v:View) {
        when(v.id){
            R.id.tv_yes_text -> {
                dialog?.dismiss()
                val applicationSubmitDialog = ApplicationSubmitDialog("Thank you for your interest!","Our Project Manager will reach out to you in 24 hours!")
                applicationSubmitDialog.show(parentFragmentManager,"ApplicationSubmitDialog")
            }
            R.id.tv_no_text -> {
                dialog?.dismiss()
            }
        }
    }

}