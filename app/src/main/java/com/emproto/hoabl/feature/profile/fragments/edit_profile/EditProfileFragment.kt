package com.emproto.hoabl.feature.profile.fragments.edit_profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.emproto.core.BaseActivity
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentEditProfileBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.request.login.profile.EditUserNameRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.*
import com.example.portfolioui.databinding.RemoveConfirmationBinding
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.let as let1


class EditProfileFragment : BaseFragment() {
    private var type: String? = null
    lateinit var datePicker: DatePickerDialog.OnDateSetListener
    val bundle = Bundle()

    @Inject
    lateinit var profileFactory: ProfileFactory
    lateinit var profileViewModel: ProfileViewModel
    lateinit var binding: FragmentEditProfileBinding

    var email = ""
    val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    var houseNo = ""
    var address = ""
    var locality = ""
    var pinCode = ""
    var countrySelected = ""
    var stateSelected = ""
    var citySelected = ""

    val pinCodePattern = Pattern.compile("([1-9]{1}[0-9]{5}|[1-9]{1}[0-9]{3}\\\\s[0-9]{3})")

    private val PICK_GALLERY_IMAGE = 1
    lateinit var bitmap: Bitmap
    var destinationFile = File("")

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadStorageGranted = false
    private var isWriteStorageGranted = false
    lateinit var removePictureDialog: Dialog

    val permissionRequest: MutableList<String> = ArrayList()
    private lateinit var countriesData: List<Countries>
    private lateinit var statesData: List<States>
    private lateinit var cityData: List<String>

    private val listCountries = ArrayList<String>()
    private val listCountryISO = ArrayList<String>()

    //    private val countryIsoCode = ArrayList<String>()
    private val listStates = ArrayList<String>()
    private val listStatesISO = ArrayList<String>()
    private val listCities = ArrayList<String>()

    private val countryIsoCode = "IN"
    lateinit var state: String
    lateinit var stateIso: String
    lateinit var country: String
    lateinit var countryIso: String
    lateinit var city: String

    lateinit var preSelectedCountryIso: String

    lateinit var gender: String
    private var cameraFile: File? = null

    @Inject
    lateinit var appPreference: AppPreference
    lateinit var data: Data


    companion object {
        fun newInstance():
                EditProfileFragment {
            val fragment = EditProfileFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            data = requireArguments().getSerializable("profileData") as Data
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel =
            ViewModelProvider(requireActivity(), profileFactory)[ProfileViewModel::class.java]
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            false

        val myCalender = Calendar.getInstance()
        datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
            myCalender.set(Calendar.YEAR, year)
            myCalender.set(Calendar.MONTH, month)
            myCalender.set(Calendar.DAY_OF_MONTH, dayofMonth)

            updateLable(myCalender)
        }
        binding.tvDatePicker.setOnClickListener {
            context?.let1 { it1 ->
                val dialog = DatePickerDialog(
                    it1,
                    datePicker,
                    myCalender.get(Calendar.YEAR),
                    myCalender.get(Calendar.MONTH),
                    myCalender.get(Calendar.DAY_OF_MONTH)
                )
                dialog.datePicker.maxDate = System.currentTimeMillis()
                dialog.show()

            }
        }
        initView()
        setGenderSpinnersData()
        getCountries(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (data.profilePictureUrl.isNullOrEmpty()) {
            binding.tvremove.isClickable = false
            binding.tvRemove1.setTextColor(Color.parseColor("#9d9eb1"))
            binding.tvRemove2.setTextColor(Color.parseColor("#9d9eb1"))
        } else {
            binding.tvremove.isClickable = true
            binding.tvRemove1.setTextColor(Color.parseColor("#9192a0"))
            binding.tvRemove2.setTextColor(Color.parseColor("#9192a0"))
        }
        initClickListener()
    }

    private fun getCountries(refresh: Boolean) {
        profileViewModel.getCountries(refresh).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBaar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBaar.hide()
                    it.data?.data?.let1 { it ->
                        countriesData = it
                    }
                    if (data.country != null) {
                        for (i in countriesData) {
                            if (data.country == i.name) {
                                countryIso = i.isoCode
                            }
                            listCountries.add(i.name)
                            listCountryISO.add(i.isoCode)
                        }
                    } else {
                        for (i in countriesData.indices) {
                            listCountries.add(countriesData[i].name)
                            listCountryISO.add(countriesData[i].isoCode)
                        }
                    }
                    setCountrySpinnersData()
                }
                Status.ERROR -> {
                    binding.progressBaar.hide()
                }
            }
        }
    }

    private fun getStates(countryIso: String, refresh: Boolean) {
        profileViewModel.getStates(countryIso, refresh).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBaar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBaar.hide()
                    it.data?.data?.let1 { data ->
                        statesData = data
                    }
                    if (data.state != null) {
                        for (i in statesData) {
                            if (data.state == i.name) {
                                stateIso = i.isoCode
                            }
                            listStates.add(i.name)
                            listStatesISO.add(i.isoCode)
                        }
                    } else {
                        for (i in statesData.indices) {
                            listStates.add(statesData[i].name)
                            listStatesISO.add(statesData[i].isoCode)

                        }
                    }
                    setStateSpinnersData()
                }
                Status.ERROR -> {
                    binding.progressBaar.hide()
                }
            }
        }
    }

    private fun getCities(value1: String, isoCode: String, refresh: Boolean) {
        profileViewModel.getCities(value1, isoCode, refresh).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBaar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBaar.hide()
                    it.data?.data.let1 { data ->
                        cityData = data!!
                    }
                    for (i in cityData.indices) {
                        listCities.add(cityData[i])
                    }
                    setCitiesSpinner()
                }
                Status.ERROR -> {
                    binding.progressBaar.hide()
                }
            }
        }
    }

    private fun setGenderSpinnersData() {
        val list3 = ArrayList<String>()
        list3.add("Male")
        list3.add("Female")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_text, list3)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

        binding.autoGender.setAdapter(adapter)

        binding.autoGender.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                gender = parent?.adapter?.getItem(position).toString().substring(0, 1)
                enableGenderEdit()
            }
    }

    private fun setCountrySpinnersData() {
        val countryArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.spinner_text, listCountries)
        countryArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        binding.autoCountry.setAdapter(countryArrayAdapter)
        binding.autoCountry.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                binding.autoState.setText("")
                binding.autoCity.setText("")
                country = listCountries[position]
                countryIso = listCountryISO[position]
                listStates.clear()
                getStates(countryIso, true)
            }
        if (data.country != null) {
            listStates.clear()
            getStates(countryIso, false)
        }

    }
    private fun setStateSpinnersData() {
        val stateArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.spinner_text, listStates)
        stateArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        binding.autoState.setAdapter(stateArrayAdapter)
        binding.autoState.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                binding.autoCity.setText("")
                state = listStates[position]
                stateIso = listStatesISO[position]
                getCities(stateIso, countryIso, true)
            }
        if (data.state != null) {
            listStates.clear()
            getCities(stateIso, countryIso, true)
        }
        enableStateEdit()
    }

    private fun setCitiesSpinner() {
        val cityAdapter = ArrayAdapter(requireContext(), R.layout.spinner_text, listCities)
        cityAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        binding.autoCity.setAdapter(cityAdapter)
        binding.autoCity.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                city = listCities[position]
                getCities(stateIso,countryIso, false)

            }
        if (data.country != null) {
            listCities.clear()
            getCities(stateIso,countryIso, false)
        }
        enableCityEdit()
    }

    private fun initView() {
        binding.emailTv.setText("")
        binding.completeAddress.setText("")
        binding.houseNo.setText("")
        binding.pincodeEditText.setText("")
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadStorageGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                    ?: isReadStorageGranted
                isWriteStorageGranted =
                    permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                        ?: isWriteStorageGranted
            }
        requestPermission()

        binding.textviewEnterName.text = data.firstName + " " + data.lastName
        Log.i("name", data.firstName + " " + data.lastName + data.email)
        binding.enterPhonenumberTextview.text = data.phoneNumber
        if (!data.email.isNullOrEmpty()) {
            binding.emailTv.setText(data.email)
        } else {
            binding.emailTv.setText("")
        }
        if (!data.dateOfBirth.isNullOrEmpty()) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")
            val date: Date = inputFormat.parse(data.dateOfBirth)
            val formattedDate: String = outputFormat.format(date)
            binding.tvDatePicker.setText(formattedDate)
        } else {
            binding.tvDatePicker.setText("")
        }
        if (!data.gender.isNullOrEmpty()) {
            binding.autoGender.setText(data.gender)
            enableGenderEdit()
        } else {
            binding.autoGender.setText("")
        }
        if (!data.houseNumber.isNullOrEmpty()) {
            binding.houseNo.setText(data.houseNumber)
        } else {
            binding.houseNo.setText("")
        }
        if (!data.streetAddress.isNullOrEmpty()) {
            binding.completeAddress.setText(data.streetAddress)
        } else {
            binding.completeAddress.setText("")
        }
        if (!data.locality.isNullOrEmpty()) {
            binding.locality.setText(data.locality)
        } else {
            binding.locality.setText("")
        }
        if (!data.country.isNullOrEmpty()) {
            binding.autoCountry.setText(data.country)
            enableCountryEdit()
        } else {
            binding.autoCountry.setText("")

        }
        if (!data.state.isNullOrEmpty()) {
            binding.autoState.setText(data.state)
            enableStateEdit()
        } else {
            binding.autoState.setText("")

        }
        if (!data.city.isNullOrEmpty()) {
            binding.autoCity.setText(data.city)
            enableCityEdit()
        } else {
            binding.autoCity.setText("")

        }
        if (!data.pincode.toString().isNullOrEmpty()) {
            if (data.pincode.toString() == "null") {
                binding.pincodeEditText.setText("")

            } else {
                binding.pincodeEditText.setText(data.pincode.toString())

            }

        } else if (data.pincode.toString() == null) {
            binding.pincodeEditText.setText("")

        } else {
            binding.pincodeEditText.setText("")
        }
        if (data.profilePictureUrl.isNullOrEmpty()) {
            binding.profileImage.visibility = View.GONE
            binding.profileUserLetters.visibility = View.VISIBLE
            binding.tvremove.isClickable = false
            binding.tvRemove1.setTextColor(Color.parseColor("#9d9eb1"))
            binding.tvRemove2.setTextColor(Color.parseColor("#9d9eb1"))
            setUserNamePIC(data)
        } else {
            binding.profileImage.visibility = View.VISIBLE
            binding.profileUserLetters.visibility = View.GONE
            binding.tvRemove1.setTextColor(Color.parseColor("#9192a0"))
            binding.tvRemove2.setTextColor(Color.parseColor("#9192a0"))
            Glide.with(requireContext())
                .load(data.profilePictureUrl)
                .dontTransform()
                .into(binding.profileImage)

        }
    }

    private fun enableGenderEdit() {
        binding.autoGender.isCursorVisible = false
        binding.autoGender.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return false
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }
        }
        binding.autoGender.setTextIsSelectable(false)
        binding.autoGender.isLongClickable = false
    }

    private fun enableCountryEdit() {
        binding.autoCountry.isCursorVisible = false
        binding.autoCountry.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return false
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }
        }
        binding.autoCountry.setTextIsSelectable(false)
        binding.autoCountry.isLongClickable = false
    }

    private fun enableCityEdit() {
        binding.autoCity.isCursorVisible = false
        binding.autoCity.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return false
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }
        }
        binding.autoCity.setTextIsSelectable(false)
        binding.autoCity.isLongClickable = false
    }

    private fun enableStateEdit() {
        binding.autoState.isCursorVisible = false
        binding.autoState.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return false
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }
        }
        binding.autoState.setTextIsSelectable(false)
        binding.autoState.isLongClickable = false
    }

    private fun setUserNamePIC(data: Data) {
        if (this.data.lastName.isNullOrEmpty()) {
            val firstLetter: String = this.data.firstName.substring(0, 2)

            binding.tvUserName.text = firstLetter
        } else {
            val firstLetter: String = this.data.firstName.substring(0, 1)

            val lastLetter: String = this.data.lastName.substring(0, 1)
            binding.tvUserName.text = firstLetter + "" + lastLetter

        }
    }

    private fun updateLable(myCalendar: Calendar) {
        val sdf = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ssZ", Locale.ENGLISH)
        var dateSelected = sdf.format(myCalendar.time)
        binding.tvDatePicker.setText(dateSelected.substring(0, 10))

    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.emailTv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                binding.tvEmail.isErrorEnabled = false
            }
        })
        binding.houseNo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {


                binding.floorHouseNum.isErrorEnabled = false
            }
        })
        binding.completeAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                binding.comAdd.isErrorEnabled = false
            }
        })
        binding.locality.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                binding.tvLocality.isErrorEnabled = false
            }
        })
        binding.pincodeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                binding.pincode.isErrorEnabled = false
            }
        })

        binding.uploadNewPicture.setOnClickListener { selectImage() }

        removePictureDialog()
        binding.saveAndUpdate.setOnClickListener {
            if ((requireActivity() as BaseActivity).isNetworkAvailable()) {
                if (type == "CAMERA_CLICK")
                    callingUploadPicApi(cameraFile!!)
                else if (type == "GALLERY_CLICK") {
                    callingUploadPicApi(destinationFile)
                } else {

                }
            } else {
                (requireActivity() as BaseActivity).showError(
                    "Please check Internet Connections to upload image",
                    binding.root
                )
            }

            email = binding.emailTv.text.toString()
            Log.i("email", email)
            if (!email.isNullOrEmpty() && email.isValidEmail()) {
                binding.tvEmail.isErrorEnabled = false
                email = binding.emailTv.text.toString()
            } else {
                binding.tvEmail.error = "Please enter valid email"
                email = binding.emailTv.text.toString()
                if (email.length == 150) {
                    binding.tvEmail.error = "You have reached the max characters limit"
                    Toast.makeText(
                        context,
                        "You have reached the max characters limit",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            houseNo = binding.houseNo.text.toString()
            when {
                houseNo.length == 150 -> {
                    binding.floorHouseNum.error = "You have reached the max characters limit"
                }
                houseNo.isEmpty() -> {
                    binding.floorHouseNum.error = "Field cannot be empty"
                }
                else -> {
                    houseNo = binding.houseNo.text.toString()
                }
            }

            address = binding.completeAddress.text.toString()
            when {
                address.length == 150 -> {
                    binding.comAdd.error = "You have reached the max characters limit"
                }
                address.isEmpty() -> {
                    binding.comAdd.error = "Field cannot be empty"
                }
                else -> {
                    address = binding.completeAddress.text.toString()
                }
            }
            locality = binding.locality.text.toString()
            when {
                locality.length == 150 -> {
                    binding.tvLocality.error = "You have reached the max characters limit"
                }
                locality.isEmpty() -> {
                    binding.tvLocality.error = "Field cannot be empty"
                }
                else -> {
                    locality = binding.locality.text.toString()
                }
            }
            pinCode = binding.pincodeEditText.text.toString()
            if (pinCode.isValidPinCode()) {
                binding.pincode.isErrorEnabled = false
            } else if (pinCode.isEmpty()) {
                binding.pincode.error = "Field cannot be empty"

            } else {
                binding.pincode.error = "Please enter valid pincode"
                pinCode = binding.pincodeEditText.text.toString()
                if (pinCode.length > 6) {
                    binding.pincode.error = "Invalid Pincode"
                    Toast.makeText(
                        context,
                        "You have reached the max characters limit",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            countrySelected = binding.autoCountry.text.toString()
            if (!countrySelected.isNullOrEmpty()) {
                binding.spinnerCountry.isErrorEnabled = false
            } else {
                binding.spinnerCountry.error = "Select Country"
                countrySelected = binding.autoCountry.text.toString()
            }
            stateSelected = binding.autoState.text.toString()
            if (!stateSelected.isNullOrEmpty()) {
                binding.spinnerState.isErrorEnabled = false
            } else {
                binding.spinnerState.error = "Select State"
                stateSelected = binding.autoState.text.toString()
            }
            citySelected = binding.autoCity.text.toString()
            if (!citySelected.isNullOrEmpty()) {
                binding.spinnerCity.isErrorEnabled = false
            } else {
                binding.spinnerCity.error = "Select City"
                citySelected = binding.autoCity.text.toString()
            }
            if (!email.isNullOrEmpty() && email.isValidEmail() && !houseNo.isNullOrEmpty() && !address.isNullOrEmpty() && !locality.isNullOrEmpty() && pinCode.isValidPinCode() && !countrySelected.isNullOrEmpty() && !stateSelected.isNullOrEmpty() && !citySelected.isNullOrEmpty()) {
                val validEmail = binding.emailTv.text
                val validHouse = binding.houseNo.text
                val validAdd = binding.completeAddress.text
                val validLocality = binding.locality.text
                val validPinCode = binding.pincodeEditText.text
                val validCountry = binding.autoCountry.text
                val validState = binding.autoState.text
                val validCity = binding.autoCity.text
                sendProfileDetail(
                    validEmail,
                    validHouse,
                    validAdd,
                    validLocality,
                    validPinCode,
                    validCountry,
                    validState,
                    validCity
                )
                val dialog = EditProfileUpdatedPopUpFragment()
                dialog.isCancelable = false
                dialog.show(childFragmentManager, "submitted")
                changeFontOnSave()
            }
        }
    }

    private fun removeSemiPictureDialog() {
        val removeDialogLayout = RemoveConfirmationBinding.inflate(layoutInflater)
        removePictureDialog = Dialog(requireContext())
        removePictureDialog.setCancelable(false)
        removePictureDialog.setContentView(removeDialogLayout.root)

        removeDialogLayout.actionYes.setOnClickListener {
            binding.profileImage.visibility = View.GONE
            binding.profileUserLetters.visibility = View.VISIBLE
            setUserNamePIC(data)
            removePictureDialog.dismiss()
            binding.textremove.visibility = View.GONE
            binding.tvremove.visibility = View.VISIBLE

        }
        removeDialogLayout.actionNo.setOnClickListener {
            removePictureDialog.dismiss()
        }
        binding.textremove.setOnClickListener {
            removePictureDialog.show()

        }
    }

    private fun removePictureDialog() {
        val removeDialogLayout = RemoveConfirmationBinding.inflate(layoutInflater)
        removePictureDialog = Dialog(requireContext())
        removePictureDialog.setCancelable(false)
        removePictureDialog.setContentView(removeDialogLayout.root)

        removeDialogLayout.actionYes.setOnClickListener {
            callDeletePic(data)
            binding.profileImage.visibility = View.GONE
            binding.profileUserLetters.visibility = View.VISIBLE
            setUserNamePIC(data)
        }
        removeDialogLayout.actionNo.setOnClickListener {
            removePictureDialog.dismiss()
        }
        binding.tvremove.setOnClickListener {
            if (data.profilePictureUrl.isNullOrEmpty()) {
                Toast.makeText(context, "Picture already removed", Toast.LENGTH_SHORT).show()
            } else {
                removePictureDialog.show()
            }
        }
    }

    private fun changeFontOnSave() {
        val typeface1 = context?.let1 { it1 -> ResourcesCompat.getFont(it1, R.font.jost_medium) }
        binding.emailTv.typeface = typeface1
        val typeface2 = context?.let1 { it1 -> ResourcesCompat.getFont(it1, R.font.jost_medium) }
        binding.houseNo.typeface = typeface2
        val typeface3 = context?.let1 { it1 -> ResourcesCompat.getFont(it1, R.font.jost_medium) }
        binding.completeAddress.typeface = typeface3
        val typeface4 = context?.let1 { it1 -> ResourcesCompat.getFont(it1, R.font.jost_medium) }
        binding.locality.typeface = typeface4
        val typeface5 = context?.let1 { it1 -> ResourcesCompat.getFont(it1, R.font.jost_medium) }
        binding.autoCountry.typeface = typeface5
        val typeface6 = context?.let1 { it1 -> ResourcesCompat.getFont(it1, R.font.jost_medium) }
        binding.autoState.typeface = typeface6
        val typeface7 = context?.let1 { it1 -> ResourcesCompat.getFont(it1, R.font.jost_medium) }
        binding.autoCity.typeface = typeface7
        val typeface8 = context?.let1 { it1 -> ResourcesCompat.getFont(it1, R.font.jost_medium) }
        binding.pincodeEditText.typeface = typeface8
        val typeface9 = context?.let1 { it1 -> ResourcesCompat.getFont(it1, R.font.jost_medium) }
        binding.autoGender.typeface = typeface9
        val typeface10 = context?.let1 { it1 -> ResourcesCompat.getFont(it1, R.font.jost_medium) }
        binding.tvDatePicker.typeface = typeface10
    }

    private fun sendProfileDetail(
        validEmail: Editable,
        validHouse: Editable,
        validAdd: Editable,
        validLocality: Editable,
        validPinCode: Editable,
        validCountry: Editable,
        validState: Editable,
        validCity: Editable
    ) {
        val editUserNameRequest = EditUserNameRequest(
            data.firstName,
            data.lastName,
            validEmail.toString(),
            binding.tvDatePicker.text.toString(),
            binding.autoGender.text.toString(),
            validHouse.toString(),
            validAdd.toString(),
            validLocality.toString(),
            validPinCode.toString(),
            validCity.toString(),
            validState.toString(),
            validCountry.toString()
        )
        profileViewModel.editUserNameProfile(editUserNameRequest)
            .observe(
                viewLifecycleOwner
            ) { t ->
                when (t!!.status) {
                    Status.LOADING -> {
                        binding.saveAndUpdate.visibility = View.GONE

                    }
                    Status.SUCCESS -> {
                        appPreference.saveLogin(true)
                        binding.saveAndUpdate.visibility = View.VISIBLE
                        binding.emailTv.clearFocus()
                        binding.houseNo.clearFocus()
                        binding.completeAddress.clearFocus()
                        binding.tvLocality.clearFocus()
                        binding.pincodeEditText.clearFocus()
                    }
                    Status.ERROR -> {
                        binding.saveAndUpdate.visibility = View.VISIBLE
                    }
                }
            }

    }


    fun CharSequence?.isValidEmail() =
        emailPattern.matcher(this).matches()

    fun CharSequence?.isValidPinCode() =
        pinCodePattern.matcher(this).matches()

    /*----------upload picture--------------*/
    private fun requestPermission() {
        isReadStorageGranted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isWriteStorageGranted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        if (!isReadStorageGranted && !isWriteStorageGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    private fun onCaptureImageResult() {
        val selectedImage = cameraFile?.path
        destinationFile = cameraFile!!
        val thumbnail = BitmapFactory.decodeFile(selectedImage)
        val ei = ExifInterface(cameraFile!!.path)
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        val rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotateImage(thumbnail, 90f)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotateImage(thumbnail, 180f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotateImage(thumbnail, 270f)
            }
            ExifInterface.ORIENTATION_NORMAL -> {
                thumbnail
            }
            else -> {
                thumbnail
            }
        }
        binding.profileImage.visibility = View.VISIBLE
        binding.profileUserLetters.visibility = View.GONE
        try {
            binding.profileImage.setImageBitmap(rotatedBitmap)
            binding.tvremove.visibility = View.GONE
            binding.textremove.visibility = View.VISIBLE
            binding.tvRemove2.setTextColor(Color.parseColor("#9192a0"))

            removeSemiPictureDialog()
//            binding.tvRemove1.setTextColor(Color.parseColor("#9192a0"))

        } catch (e: Exception) {
            e.message
        }
        type = "CAMERA_CLICK"
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    private fun onSelectFromGalleryResult(data: Intent) {
        val selectedImage = data.data
        var inputStream =
            requireContext().contentResolver.openInputStream(selectedImage!!)
        try {
            bitmap = BitmapFactory.decodeStream(inputStream)
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            try {
                val filePath = getRealPathFromURI_API19(requireContext(), selectedImage)
                destinationFile = File(filePath)
                type = "GALLERY_CLICK"
            } catch (e: Exception) {
                Log.e("Error", "onSelectFromGalleryResult: " + e.localizedMessage)
            }

            binding.profileImage.visibility = View.VISIBLE
            binding.profileUserLetters.visibility = View.GONE
            binding.profileImage.setImageBitmap(bitmap)
            binding.tvremove.visibility = View.GONE
            binding.textremove.visibility = View.VISIBLE
            binding.tvRemove2.setTextColor(Color.parseColor("#9192a0"))

            removeSemiPictureDialog()
//            binding.tvRemove1.setTextColor(Color.parseColor("#9192a0"))


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun callingUploadPicApi(destinationFile: File) {
        profileViewModel.uploadProfilePicture(destinationFile, destinationFile.name)
            .observe(
                viewLifecycleOwner,
                object : Observer<BaseResponse<ProfilePictureResponse>> {
                    override fun onChanged(it: BaseResponse<ProfilePictureResponse>?) {
                        when (it?.status) {
                            Status.LOADING -> {
                                binding.progressBaar.show()
                            }
                            Status.SUCCESS -> {
                                binding.tvRemove1.setTextColor(Color.parseColor("#9192a0"))
                                binding.tvRemove2.setTextColor(Color.parseColor("#9192a0"))
                                binding.progressBaar.hide()
                            }
                            Status.ERROR -> {
                                binding.progressBaar.hide()
                            }
                        }
                    }
                })
    }

    private fun callDeletePic(data: Data) {
        val fileName: String = data.profilePictureUrl.toString()
            .substring(data.profilePictureUrl.toString().lastIndexOf('/') + 1)
        Log.i("profileUrl", fileName)
        profileViewModel.deleteProfileImage(fileName)
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBaar.show()
                    }
                    Status.SUCCESS -> {
                        binding.progressBaar.hide()
                        binding.tvremove.isClickable = false
                        binding.tvRemove1.setTextColor(Color.parseColor("#9d9eb1"))
                        binding.tvRemove2.setTextColor(Color.parseColor("#9d9eb1"))
                        removePictureDialog.dismiss()
                        if (data.profilePictureUrl == null) {
                            binding.profileImage.visibility = View.GONE
                            binding.profileUserLetters.visibility = View.VISIBLE
                            setUserNamePIC(data)
                        }

                    }
                    Status.ERROR -> {
                        binding.progressBaar.hide()
                        Toast.makeText(
                            this.requireContext(),
                            it.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.i("delete api error", it.message.toString())
                    }
                }
            })
    }

    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        getPhotoFile(requireContext())
                    )
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    cameraLauncher.launch(intent)

                }
                options[item] == "Choose from Gallery" -> {
                    val intent =
                        Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                    resultLauncher.launch(intent)
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    var cameraLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode === Activity.RESULT_OK) {
            onCaptureImageResult()
        }
    }

    var resultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result != null && result.resultCode === Activity.RESULT_OK) {
            if (result.data != null) {
                onSelectFromGalleryResult(result.data!!)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_GALLERY_IMAGE) {
                onSelectFromGalleryResult(data!!)
            } else {
                (requireActivity() as BaseActivity).showError(
                    "Nothing Selected",
                    binding.root
                )
            }
        }
    }

    @SuppressLint("NewApi")
    fun getRealPathFromURI_API19(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs!!)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun getPhotoFile(context: Context): Uri? {
        val fileSuffix = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        cameraFile = File(context.externalCacheDir, "$fileSuffix.jpg")
        return FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName + ".provider",
            cameraFile!!
        )
    }

}








