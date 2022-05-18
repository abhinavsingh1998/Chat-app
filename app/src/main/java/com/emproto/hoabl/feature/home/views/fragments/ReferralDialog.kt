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
import com.emproto.core.customedittext.OnValueChangedListener
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ReferralDialogBinding


class ReferralDialog : DialogFragment(), View.OnClickListener {


    lateinit var mBinding: ReferralDialogBinding
    var mobileNo = ""
    var name= ""
    var hCountryCode= ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = ReferralDialogBinding.inflate(inflater, container, false)

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
                name= p0.toString()

                if (name.isNullOrEmpty() || mobileNo.isNullOrEmpty()){

                    unselected_state()
                } else{
                    selected_state()
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                name=p0.toString()
            }

        })

        mBinding.inputMobile.onValueChangeListner(object :OnValueChangedListener{
            override fun onValueChanged(value: String?, countryCode: String?) {
                mobileNo= value.toString()
                hCountryCode= countryCode.toString()
                if (mobileNo.isNullOrEmpty() || name.isNullOrEmpty()){
                    unselected_state()

                } else{
                    selected_state()
                }


            }
            override fun afterValueChanges(value1: String?) {
                mobileNo= value1!!
            }
        })

        mBinding.closeBtn.setOnClickListener{
            unselected_state()
            this.dismiss()
        }


    }

    fun unselected_state(){
        mBinding.referBtn.isClickable= false
        mBinding.referBtn.isEnabled= false
        mBinding.referBtn.background= resources.getDrawable(R.drawable.unselect_button_bg)
    }

    fun selected_state(){
        mBinding.referBtn.isClickable= true
        mBinding.referBtn.isEnabled= true
        mBinding.referBtn.background= resources.getDrawable(R.drawable.button_bg)
    }
}