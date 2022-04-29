package com.emproto.hoabl.feature.profile

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentEditProfileBinding
import com.emproto.hoabl.databinding.FragmentNameInputBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.login.SucessDialogFragment
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.profile.EditUserNameRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.EditProfileResponse
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class EditProfileFragment : Fragment() {
    lateinit var binding: FragmentEditProfileBinding
    val bundle = Bundle()

    var charSequence1: Editable? = null
    var charSequence2: Editable? = null

    @Inject
    lateinit var profileFactory: ProfileFactory
    lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var appPreference: AppPreference
    companion object {
        var firstName: String = ""
        var lastName: String = ""
        var email:String= ""
        var dateOfBirth:String=""
        var gender:String=""
        var houseNumber:String=""
        var locality:String=""
        var pincode: String= ""
        var city:String=""
        var state:String=""
        var country:String=""

        fun newInstance(firstName: String, lastName: String,email:String,dateOfBirth:String,gender:String,houseNumber:String,locality:String,pincode:String,city:String,state:String,country:String): EditProfileFragment {
            val fragment = EditProfileFragment()
            val bundle = Bundle()
            bundle.putString("firstName", firstName)
            bundle.putString("lastName", lastName)
            bundle.putString("email",email)
            bundle.putString("dateOfBirth",dateOfBirth)
            bundle.putString("gender",gender)
            bundle.putString("houseNumber",houseNumber)
            bundle.putString("locality",locality)
            bundle.putString("pincode",pincode)
            bundle.putString("city",city)
            bundle.putString("state",state)
            bundle.putString("country",country)

            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            firstName = requireArguments().getString("firstName")!!
            lastName = requireArguments().getString("lastName")!!
            email = requireArguments().getString("email")!!
            dateOfBirth = requireArguments().getString("dateOfBirth")!!
            gender = requireArguments().getString("gender")!!
            houseNumber = requireArguments().getString("houseNumber")!!
            locality = requireArguments().getString("locality")!!
            pincode = requireArguments().getString("pincode")!!
            state = requireArguments().getString("state")!!
            country = requireArguments().getString("country")!!
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
//        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel = ViewModelProvider(requireActivity(), profileFactory)[ProfileViewModel::class.java]
        binding = FragmentEditProfileBinding.inflate(layoutInflater)

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            false


        val myCalender = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
            myCalender.set(Calendar.YEAR, year)
            myCalender.set(Calendar.MONTH, month)
            myCalender.set(Calendar.DAY_OF_MONTH, dayofMonth)
            updateLable(myCalender)
        }
        binding.tvDatePicker.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p1) {
                    context?.let {
                        DatePickerDialog(
                            it,
                            datePicker,
                            myCalender.get(Calendar.YEAR),
                            myCalender.get(Calendar.MONTH),
                            myCalender.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                }
            }
        })
        binding.tvDatePicker.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    datePicker,
                    myCalender.get(Calendar.YEAR),
                    myCalender.get(Calendar.MONTH),
                    myCalender.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        initClickListener()
        return binding.root
    }

    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.tvDatePicker.setText(sdf.format(myCalendar.time))
    }

    private fun initClickListener() {
        if (firstName.isNotEmpty()) {
            binding.firstnameTv.setText(firstName)
        }
        if (lastName.isNotEmpty()) {
            binding.lastnameTv.setText(lastName)
        }
        if (dateOfBirth.isNotEmpty()){
            binding.tvDatePicker.setText(dateOfBirth)

        }
        if (email.isNotEmpty()){
            binding.emailTv.setText(email)

        }
        if (dateOfBirth.isNotEmpty()){
            binding.dob.setTag(dateOfBirth)

        }
        if (gender.isNotEmpty()){
            binding.genderEditText.setText(gender)

        }
        if (houseNumber.isNotEmpty()){
            binding.houseNo.setText(houseNumber)

        }
        if (locality.isNotEmpty()){
            binding.locality.setText(locality)

        }

        if (city.isNotEmpty()){
            binding.city.setText(city)

        }
        if (state.isNotEmpty()){
            binding.stateEditText.setText(state)

        }
        if (country.isNotEmpty()){
            binding.countryEditText.setText(country)

        }
        if (pincode.isNotEmpty()){
            binding.pincodeEditText.setText(pincode)

        }

        binding.firstnameTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                charSequence1 = p0
                if (p0.toString().isNullOrEmpty()) {
                    binding.saveAndUpdate.isEnabled = false
                    binding.saveAndUpdate.isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.saveAndUpdate.background =
                            AppCompatResources.getDrawable(requireContext(),
                                R.drawable.unselect_button_bg)
                    }
                } else {
                    binding.saveAndUpdate.isEnabled = true
                    binding.saveAndUpdate.isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        binding.saveAndUpdate.background =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.button_bg)

                    }
                }
            }
        })
        binding.lastnameTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.emailTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
        binding.tvDatePicker.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
        binding.genderEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
        binding.houseNo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
        binding.locality.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
        binding.pincodeEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
        binding.city.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
        binding.stateEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
        binding.countryEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
        binding.tvDatePicker.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

            }


        })
        binding.saveAndUpdate.setOnClickListener(View.OnClickListener {
            val editUserNameRequest = EditUserNameRequest(
                binding.firstnameTv.text.toString(),
                binding.lastnameTv.text.toString(),
                binding.emailTv.text.toString(),
                binding.tvDatePicker.text.toString(),
                binding.genderEditText.text.toString(),
                binding.houseNo.text.toString(),
                binding.locality.text.toString(),
                binding.city.text.toString(),
                binding.pincodeEditText.text.toString(),
                binding.stateEditText.text.toString(),
                binding.countryEditText.text.toString(),
                binding.countryEditText.text.toString()
            )
            profileViewModel.editUserNameProfile(editUserNameRequest)
                .observe(viewLifecycleOwner, object : Observer<BaseResponse<EditProfileResponse>> {
                    override fun onChanged(t: BaseResponse<EditProfileResponse>?) {

                        when (t!!.status) {
                            Status.LOADING -> {

                                binding.saveAndUpdate.visibility = View.GONE
                            }
                            Status.SUCCESS -> {
                                appPreference.saveLogin(true)

                                binding.saveAndUpdate.visibility = View.VISIBLE
                                val dialog = SucessDialogFragment()
                                val bundle = Bundle()
                                bundle.putString("FirstName", binding.firstnameTv.text.toString())
                                dialog.arguments = bundle
                                dialog.isCancelable = false
                                dialog.show(parentFragmentManager, "Welcome Card")
                            }
                            Status.ERROR -> {

                                binding.saveAndUpdate.visibility = View.VISIBLE
                            }
                        }
                    }

                })
        })
        binding.backAction.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        })
    }
}









