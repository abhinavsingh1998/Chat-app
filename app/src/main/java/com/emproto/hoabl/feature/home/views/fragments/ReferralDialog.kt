package com.emproto.hoabl.feature.home.views.fragments

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.customedittext.OnValueChangedListener
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ReferralDialogBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.request.refernow.ReferalRequest
import com.emproto.networklayer.response.enums.Status
import javax.inject.Inject


class ReferralDialog : DialogFragment(), View.OnClickListener {


    lateinit var mBinding: ReferralDialogBinding
    var mobileNo = ""
    var name = ""
    var hCountryCode = ""

    @Inject
    lateinit var factory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = ReferralDialogBinding.inflate(inflater, container, false)

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        initClickListner()
        return mBinding.root
    }

    override fun onClick(p0: View?) {


    }

    private fun initClickListner() {
        mBinding.referralName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                name = p0.toString()

                if (name.isNullOrEmpty() || mobileNo.isNullOrEmpty()) {
                    unselected_state()
                } else {
                    selected_state()
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                name = p0.toString()
            }

        })

        mBinding.inputMobile.onValueChangeListner(object : OnValueChangedListener {
            override fun onValueChanged(value: String?, countryCode: String?) {
                mobileNo = value.toString()
                hCountryCode = countryCode.toString()
                if (mobileNo.isNullOrEmpty() || name.isNullOrEmpty()) {
                    unselected_state()

                } else {
                    selected_state()
                }


            }

            override fun afterValueChanges(value1: String?) {
                mobileNo = value1!!
            }
        })

        mBinding.closeBtn.setOnClickListener {
            unselected_state()
            this.dismiss()
        }

        mBinding.referBtn.setOnClickListener {
            val referRequest = ReferalRequest(name, mobileNo)
            homeViewModel.getReferNow(referRequest).observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismiss()
                    }
                }
            })
        }


    }

    fun unselected_state() {
        mBinding.referBtn.isClickable = false
        mBinding.referBtn.isEnabled = false
        mBinding.referBtn.background = resources.getDrawable(R.drawable.unselect_button_bg)
    }

    fun selected_state() {
        mBinding.referBtn.isClickable = true
        mBinding.referBtn.isEnabled = true
        mBinding.referBtn.background = resources.getDrawable(R.drawable.button_bg)
    }
}