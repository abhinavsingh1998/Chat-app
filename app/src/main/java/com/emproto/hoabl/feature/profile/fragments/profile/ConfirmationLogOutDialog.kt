package com.emproto.hoabl.feature.profile.fragments.profile

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
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.example.portfolioui.databinding.LogoutConfirmationBinding
import com.google.android.material.textview.MaterialTextView

class ConfirmationLogOutDialog(private val itemClickListener: ItemClickListener) :DialogFragment(),View.OnClickListener {

    lateinit var binding: LogoutConfirmationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LogoutConfirmationBinding.inflate(layoutInflater)
//        dialog?.window?.setBackgroundDrawableResource(R.drawable.all_corner_radius_white_bg)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        binding.apply {
            textView8.text = "Are you sure you want to logout from all devices?"
        }
        binding.actionYes.setOnClickListener { view ->
            dialog?.dismiss()
            itemClickListener.onItemClicked(view,0,"")
        }
        binding.actionNo.setOnClickListener(this)
        dialog?.setCancelable(false)
    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onClick(v:View) {
        when(v.id){
            R.id.tv_no_text -> {
                dialog?.dismiss()
            }
        }
    }

}