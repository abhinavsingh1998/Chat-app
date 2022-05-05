package com.emproto.hoabl.feature.login


import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentNameInputBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.viewmodels.AuthViewmodel
import com.emproto.hoabl.viewmodels.factory.AuthFactory
import com.emproto.networklayer.request.login.AddNameRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.login.AddNameResponse
import javax.inject.Inject


class NameInputFragment : BaseFragment() {


    lateinit var binding: FragmentNameInputBinding
    var charSequence1: Editable? = null
    var charSequence2: Editable? = null

    @Inject
    lateinit var authFactory: AuthFactory
    lateinit var authViewModel: AuthViewmodel

    @Inject
    lateinit var appPreference: AppPreference

    companion object {
        var firstName: String = ""
        var lastName: String = ""

        fun newInstance(firstName: String, lastName: String): NameInputFragment {
            val fragment = NameInputFragment()
            val bundle = Bundle()
            bundle.putString("firstName", firstName)
            bundle.putString("lastName", lastName)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            firstName = requireArguments().getString("firstName")!!
            lastName = requireArguments().getString("lastName")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        authViewModel = ViewModelProvider(requireActivity(), authFactory)[AuthViewmodel::class.java]
        binding = FragmentNameInputBinding.inflate(layoutInflater)

        initClickListner()

        return binding.root
    }

    private fun initClickListner() {
        if (firstName.isNotEmpty()) {
            binding.firstName.setText(firstName)
        }
        if (lastName.isNotEmpty()) {
            binding.secondName.setText(lastName)
        }

        binding.firstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                charSequence1 = p0
                if (p0.toString().isNullOrEmpty()) {
                    binding.submitBtn.isEnabled = false
                    binding.submitBtn.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.submitBtn.background =
                            getDrawable(requireContext(), R.drawable.unselect_button_bg)
                    }
                } else {
                    binding.submitBtn.isEnabled = true
                    binding.submitBtn.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.submitBtn.background =
                            getDrawable(requireContext(), R.drawable.button_bg)

                    }
                }
            }
        })

        binding.secondName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.submitBtn.setOnClickListener(View.OnClickListener {
            val addNameRequest = AddNameRequest(
                binding.firstName.text.toString(),
                binding.secondName.text.toString()
            )
            authViewModel.addUsernameDetails(addNameRequest)
                .observe(viewLifecycleOwner, object : Observer<BaseResponse<AddNameResponse>> {
                    override fun onChanged(it: BaseResponse<AddNameResponse>?) {

                        when (it!!.status) {
                            Status.LOADING -> {
                                binding.loader.visibility = View.VISIBLE
                                binding.submitBtn.visibility = View.GONE
                            }
                            Status.SUCCESS -> {
                                appPreference.saveLogin(true)
                                binding.loader.visibility = View.GONE
                                binding.submitBtn.visibility = View.VISIBLE
                                val dialog = SucessDialogFragment()
                                val bundle = Bundle()
                                bundle.putString("FirstName", binding.firstName.text.toString())
                                dialog.arguments = bundle
                                dialog.isCancelable = false
                                dialog.show(parentFragmentManager, "Welcome Card")
                            }
                            Status.ERROR -> {
                                (requireActivity() as AuthActivity).showErrorToast(
                                    it.message!!
                                )
                                binding.loader.visibility = View.GONE
                                binding.submitBtn.visibility = View.VISIBLE
                                (requireActivity() as AuthActivity).showErrorToast(
                                    t.message!!
                                )
                            }
                        }
                    }

                })
        })
    }

}