package com.emproto.hoabl.feature.login

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.toColor
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentSigninIssueBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.AuthViewmodel
import com.emproto.hoabl.viewmodels.factory.AuthFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.request.login.TroubleSigningRequest
import com.emproto.networklayer.response.enums.Status
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class SigninIssueFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentSigninIssueBinding

    @Inject
    lateinit var authFactory: AuthFactory
    lateinit var authViewModel: AuthViewmodel

    @Inject
    lateinit var appPreference: AppPreference

    var charSequence1: Editable? = null
    var charSequence2: Editable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        authViewModel = ViewModelProvider(requireActivity(), authFactory)[AuthViewmodel::class.java]
        binding = FragmentSigninIssueBinding.inflate(layoutInflater)

        initClickListner()
        return binding.root

    }

    private fun issueChecked(): String {

        return when {
            binding.issueOne.isChecked -> {
                "2001"
            }
            binding.issueTwo.isChecked -> {
                "2002"
            }
            binding.issueThree.isChecked -> {
                "2003"
            }

            binding.issueFour.isChecked -> {
                "2004"
            }

            binding.issueFive.isChecked -> {
                "2005"
            }
            binding.issueSix.isChecked -> {
                "2006"
            }
            binding.issueSeven.isChecked -> {
                "2007"
            }

            else -> {
                " "
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initClickListner() {
        binding.mobileNumInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                binding.numLayout.error = ""
                charSequence1 = p0
                charSequence2 = null
                if (p0.toString().isNullOrEmpty() || charSequence2.toString().isNullOrEmpty()) {
                    binding.submitBtn.isEnabled = false
                    binding.submitBtn.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.submitBtn.background =
                            AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.unselect_button_bg
                            )
                    }
                } else {
                    binding.submitBtn.isEnabled = true
                    binding.submitBtn.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.submitBtn.background =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.button_bg)
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
                binding.emailLayout.error = ""
                charSequence2 = p0
                charSequence1 = null
                if (charSequence1.toString().isNullOrEmpty() || p0.toString().isNullOrEmpty()) {
                    binding.submitBtn.isEnabled = false
                    binding.submitBtn.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.submitBtn.background =
                            AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.unselect_button_bg
                            )
                    }
                } else {
                    binding.submitBtn.isEnabled = true
                    binding.submitBtn.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.submitBtn.background =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.button_bg)


                    }
                }
            }

        })

        binding.submitBtn.setOnClickListener(View.OnClickListener {

            if (binding.mobileNumInput.text.length != 10) {
                binding.numLayout.error = "Please Enter Valid Phone No."
                return@OnClickListener
            }
            if (!binding.emailInput.text.isValidEmail()) {
                binding.emailLayout.error = "Please Enter Valid Email"
                return@OnClickListener
            }

            val troubleSigningRequest = TroubleSigningRequest(
                "1001",
                "91",
                binding.editIssues.text.toString(),
                binding.emailInput.text.toString(),
                issueChecked(),
                binding.mobileNumInput.text.toString()
            )

            authViewModel.submitTroubleCase(troubleSigningRequest).observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Status.SUCCESS -> {

                            binding.progressBar.visibility = View.GONE
                            binding.submitBtn.visibility = View.VISIBLE
                            dismiss()
                            val dialog = IssueSubmittedConfirmationFragment()
                            dialog.isCancelable = true
                            dialog.show(parentFragmentManager, "Submit Card")
                        }
                        Status.ERROR -> {
                            dismiss()
                            binding.submitBtn.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.INVISIBLE
                            (requireActivity() as AuthActivity).showErrorToast(
                                it.message!!
                            )
                        }
                        Status.LOADING -> {
                            binding.submitBtn.visibility = View.INVISIBLE
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }

                })

        })

    }

    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


}








