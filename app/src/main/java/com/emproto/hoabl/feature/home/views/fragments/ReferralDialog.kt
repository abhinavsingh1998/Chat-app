package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.customedittext.OnValueChangedListener
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ReferralDialogBinding
import com.emproto.hoabl.databinding.ReferralSuccessDialogBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.portfolio.views.CustomDialog
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.request.refernow.ReferralRequest
import com.emproto.networklayer.response.enums.Status
import java.util.regex.Pattern
import javax.inject.Inject


class ReferralDialog : DialogFragment(), View.OnClickListener {


    lateinit var mBinding: ReferralDialogBinding
    var mobileNo = ""
    var name = ""
    var hCountryCode = ""

    private val num_patterns = Pattern.compile("^(0|[1-9][0-9]*)\$")
    val list = ArrayList<String>()


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
        val window = dialog!!.getWindow()
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        window.setDimAmount(0.9F)
        list.add("+91")
        mBinding.inputMobile.addDropDownValues(list)

        initClickListner()
        unselectedState()

        return mBinding.root
    }

    private fun initClickListner() {

        nameInpurt()
        numInpurt()
        closDialoge()
        referalClick()
    }


    private fun nameInpurt() {
        mBinding.referralName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                name = p0.toString()

                if (name.isNullOrEmpty() || mobileNo.isNullOrEmpty()) {
                    unselectedState()
                } else {
                    selectedState()
                }


            }

            override fun afterTextChanged(p0: Editable?) {
                name = p0.toString()
            }

        })

    }

    private fun numInpurt() {
        mBinding.inputMobile.onValueChangeListner(object : OnValueChangedListener {
            override fun onValueChanged(value: String?, countryCode: String?) {
                mobileNo = value.toString()
                hCountryCode = countryCode.toString()
                if (mobileNo.isNullOrEmpty() || name.isNullOrEmpty()) {
                    unselectedState()


                } else {
                    selectedState()
                }
                mBinding.errorTxt.isVisible = false

            }

            override fun afterValueChanges(value1: String?) {
                mobileNo = value1!!
            }
        })
    }

    private fun closDialoge() {
        mBinding.closeBtn.setOnClickListener {
            unselectedState()
            this.dismiss()
        }
    }

    private fun referalClick() {
        mBinding.referBtn.setOnClickListener {

            val referRequest = ReferralRequest(name, mobileNo)

            if (mobileNo.length != 10 || !mobileNo.ValidNO()) {
                mBinding.inputMobile.showError()
                mBinding.errorTxt.isVisible = true
                return@setOnClickListener
            }

            homeViewModel.getReferNow(referRequest).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {

                        mBinding.referBtn.isVisible = true
                        mBinding.progressBar.isVisible = false
                        val referralDialoglayout =
                            ReferralSuccessDialogBinding.inflate(layoutInflater)
                        val referralDialog = CustomDialog(requireContext())
                        referralDialog.setCancelable(false)
                        referralDialog.setContentView(referralDialoglayout.root)

                        referralDialog.show()
                        referralDialoglayout.okayBtn.setOnClickListener(View.OnClickListener {
                            referralDialog.dismiss()

                        })
                        dismiss()
                    }
                    Status.LOADING -> {
                        mBinding.referBtn.isVisible = false
                        mBinding.progressBar.isVisible = true
                    }

                    Status.ERROR -> {
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                        dismiss()
                    }
                }
            }
        }
    }

    private fun CharSequence?.ValidNO() =
        num_patterns.matcher(this).matches()

    fun unselectedState() {
        mBinding.referBtn.isClickable = false
        mBinding.referBtn.isEnabled = false
        mBinding.referBtn.background = resources.getDrawable(R.drawable.unselect_button_bg)
    }

    fun selectedState() {
        mBinding.referBtn.isClickable = true
        mBinding.referBtn.isEnabled = true
        mBinding.referBtn.background = resources.getDrawable(R.drawable.button_bg)
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onClick(v: View?) {
    }
}