package com.emproto.hoabl.feature.login

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentSigninIssueBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SigninIssueFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentSigninIssueBinding

    var charSequence1: Editable? = null
    var charSequence2: Editable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSigninIssueBinding.inflate(layoutInflater)

        initClickListner()
        return binding.root

    }

    private fun initClickListner() {
        binding.mobileNumInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                charSequence1 = p0
                charSequence2 = null
                if (p0.toString().isNullOrEmpty() || charSequence2.toString().isNullOrEmpty()) {
                    binding.submitBtn.isEnabled = false
                    binding.submitBtn.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.submitBtn.background =
                            AppCompatResources.getDrawable(requireContext(),
                                R.drawable.unselect_button_bg)
                    }
                } else {
                    binding.submitBtn.isEnabled = true
                    binding.submitBtn.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.submitBtn.background =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.button_bg)

                        binding.submitBtn.setOnClickListener(View.OnClickListener {
                            val dialog = IssueSubmittedConfirmationFragment()
                            dialog.show(parentFragmentManager, "Welcome Card")
                        })
                    }
                }
            }
        })

        binding.emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                charSequence2 = p0
                charSequence1 = null
                if (charSequence1.toString().isNullOrEmpty() || p0.toString().isNullOrEmpty()) {
                    binding.submitBtn.isEnabled = false
                    binding.submitBtn.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.submitBtn.background =
                            AppCompatResources.getDrawable(requireContext(),
                                R.drawable.unselect_button_bg)
                    }
                } else {
                    binding.submitBtn.isEnabled = true
                    binding.submitBtn.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.submitBtn.background =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.button_bg)

                        binding.submitBtn.setOnClickListener(View.OnClickListener {
                            val dialog = IssueSubmittedConfirmationFragment()
                            dialog.show(parentFragmentManager, "Welcome Card")
                        })

                    }
                }
            }

        })
    }


}








