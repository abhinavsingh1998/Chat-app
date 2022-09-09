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
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.emproto.core.BaseActivity
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentEditProfileBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.request.profile.EditUserNameRequest
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
    var houseNo = ""
    var address = ""
    var locality = ""
    var pinCode = ""
    var countrySelected = ""
    var stateSelected = ""
    var citySelected = ""
    var dob: String? = null

    val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    val pinCodePattern = Pattern.compile("([1-9]{1}[0-9]{5}|[1-9]{1}[0-9]{3}\\\\s[0-9]{3})")

    private val PICK_GALLERY_IMAGE = 1
    lateinit var bitmap: Bitmap
    var destinationFile = File("")

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadStorageGranted = false
    private var isWriteStorageGranted = false
    val permissionRequest: MutableList<String> = ArrayList()

    lateinit var removePictureDialog: Dialog

    private lateinit var countriesData: List<Countries>
    private lateinit var statesData: List<States>
    private lateinit var cityData: List<String>

    private val listCountries = ArrayList<String>()
    private val listCountryISO = ArrayList<String>()

    private val listStates = ArrayList<String>()
    private val listStatesISO = ArrayList<String>()
    private val listCities = ArrayList<String>()

    lateinit var state: String
    lateinit var stateIso: String
    lateinit var country: String
    lateinit var countryIso: String
    lateinit var city: String

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
            data = requireArguments().getSerializable(Constants.PROFILE_DATA) as Data
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

        (requireActivity() as HomeActivity).hideBottomNavigation()

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
        isReadStorageGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        isWriteStorageGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (data.profilePictureUrl.isNullOrEmpty()) {
            binding.tvremove.isClickable = false
            setColor()

        } else {
            binding.tvremove.isClickable = true
            setLightColor()

        }
        initClickListener()
    }

    private fun setColor() {
        binding.tvRemove1.setTextColor(Color.parseColor("#9d9eb1"))
        binding.tvRemove2.setTextColor(Color.parseColor("#9d9eb1"))
    }

    private fun setLightColor() {
        binding.tvRemove1.setTextColor(Color.parseColor("#9192a0"))
        binding.tvRemove2.setTextColor(Color.parseColor("#9192a0"))
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
        profileViewModel.getStates(countryIso, refresh)
            .observe(viewLifecycleOwner, object : Observer<BaseResponse<StatesResponse>> {
                override fun onChanged(it: BaseResponse<StatesResponse>) {
                    when (it!!.status) {
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

            })
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
                enableEdit(binding.autoGender)
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
                if (::countryIso.isInitialized)
                    getStates(countryIso, true)
            }
        if (data.country != null) {
            listStates.clear()
            if (::countryIso.isInitialized)
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
                listCities.clear()
                if (::stateIso.isInitialized)
                    getCities(stateIso, countryIso, true)
            }
        if (data.state != null) {
            listCities.clear()
            if (::stateIso.isInitialized)
                getCities(stateIso, countryIso, false)
        }
        enableEdit(binding.autoState)
    }

    private fun setCitiesSpinner() {
        val cityAdapter = ArrayAdapter(requireContext(), R.layout.spinner_text, listCities)
        cityAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        binding.autoCity.setAdapter(cityAdapter)
        binding.autoCity.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                city = listCities[position]
            }
        enableEdit(binding.autoCity)
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

                Log.d("permission3", "$isReadStorageGranted $isWriteStorageGranted")

                if (isReadStorageGranted && isWriteStorageGranted) {
                    selectImage()
                }else{
                    showPermissionDeniedDialog()
                }
            }

        "${data.firstName} ${data.lastName}".also { binding.textviewEnterName.text = it }
        Log.i("name", data.firstName + " " + data.lastName + data.email)
        "${data.countryCode} ${data.phoneNumber}".also {
            binding.enterPhonenumberTextview.text = it
        }
        if (data.email?.isNotEmpty() == true) {
            binding.emailTv.setText(data.email)
        } else {
            binding.emailTv.setText("")
        }
        if (data.dateOfBirth?.isNotEmpty() == true) {
            val date = Utility.parseDate(data.dateOfBirth)
            binding.tvDatePicker.setText(date)
        } else {
            binding.tvDatePicker.setText("")
        }
        if (data.gender?.isNotEmpty() == true) {
            binding.autoGender.setText(data.gender)
            enableEdit(binding.autoGender)
        } else {
            binding.autoGender.setText("")
        }
        if (data.houseNumber?.isNotEmpty() == true) {
            binding.houseNo.setText(data.houseNumber)
        } else {
            binding.houseNo.setText("")
        }
        if (data.streetAddress?.isNotEmpty() == true) {
            binding.completeAddress.setText(data.streetAddress)
        } else {
            binding.completeAddress.setText("")
        }
        if (data.locality?.isNotEmpty() == true) {
            binding.locality.setText(data.locality)
        } else {
            binding.locality.setText("")
        }
        if (data.country?.isNotEmpty() == true) {
            binding.autoCountry.setText(data.country)
            enableEdit(binding.autoCountry)
        } else {
            binding.autoCountry.setText("")

        }
        if (data.state?.isNotEmpty() == true) {
            binding.autoState.setText(data.state)
            enableEdit(binding.autoState)
        } else {
            binding.autoState.setText("")

        }
        if (data.city?.isNotEmpty() == true) {
            binding.autoCity.setText(data.city)
            enableEdit(binding.autoCity)
        } else {
            binding.autoCity.setText("")

        }
        if (data.pincode.toString() != null && data.pincode.toString().isNotEmpty()) {
            if (data.pincode.toString() == "null") {
                binding.pincodeEditText.setText("")

            } else {
                binding.pincodeEditText.setText(data.pincode.toString())

            }

        } else {
            binding.pincodeEditText.setText("")
        }
        if (data.profilePictureUrl.isNullOrEmpty()) {
            binding.cvProfileImage.visibility = View.GONE
            binding.profileUserLetters.visibility = View.VISIBLE
            binding.tvremove.isClickable = false
            setColor()
            setUserNamePIC(data)
        } else {
            binding.cvProfileImage.visibility = View.VISIBLE
            binding.profileUserLetters.visibility = View.GONE
            setLightColor()
            Glide.with(requireContext())
                .load(data.profilePictureUrl)
                .transform(CircleTransform(requireContext()))
                .into(binding.ivProfile)

        }
    }

    private fun enableEdit(autoValue: AutoCompleteTextView) {
        autoValue.isCursorVisible = false
        autoValue.customSelectionActionModeCallback = object : ActionMode.Callback {
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
        autoValue.setTextIsSelectable(false)
        autoValue.isLongClickable = false
    }

    private fun setUserNamePIC(profileData: Data) {
        if (!profileData.firstName.isNullOrEmpty() && profileData.lastName.isNullOrEmpty()) {
            val firstLetter: String = profileData.firstName!!.substring(0, 2)
            binding.tvUserName.text = firstLetter
        } else if (profileData.firstName.isNullOrEmpty() && profileData.lastName.isNullOrEmpty()) {
            binding.tvUserName.text = "AB"
        } else if (profileData.firstName.isNullOrEmpty() && !(profileData.lastName.isNullOrEmpty())) {
            val lastLetter: String = profileData.lastName!!.substring(0, 2)
            binding.tvUserName.text = lastLetter
        } else {
            val firstLetter: String = profileData.firstName!!.substring(0, 1)
            val lastLetter: String = profileData.lastName!!.substring(0, 1)
            "${firstLetter}${lastLetter}".also { binding.tvUserName.text = it }
        }
    }

    private fun updateLable(myCalendar: Calendar) {
        val sdf = SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ssZ", Locale.ENGLISH)
        val dateSelected = sdf.format(myCalendar.time)
        binding.tvDatePicker.setText(dateSelected.substring(0, 10))

    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            hideSoftKeyboard()
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

        binding.uploadNewPicture.setOnClickListener {
            Log.d("permission1", "" +isReadStorageGranted + " " + isWriteStorageGranted)
            if (!isReadStorageGranted && !isWriteStorageGranted) {
                requestPermission()
            } else if (isReadStorageGranted && isWriteStorageGranted) {
                selectImage()
            }
        }

        removePictureDialog()
        binding.saveAndUpdate.setOnClickListener {
            if ((requireActivity() as BaseActivity).isNetworkAvailable()) {
                if (type == Constants.CAMERA_CLICK)
                    callingUploadPicApi(destinationFile!!)
                else if (type == Constants.GALLERY_CLICK) {
                    callingUploadPicApi(destinationFile)
                } else {

                }
            } else {
                (requireActivity() as BaseActivity).showError(
                    Constants.PLEASE_CHECK_INTERNET_CONNECTIONS_TO_UPLOAD_IMAGE,
                    binding.root
                )
            }
            if (binding.tvDatePicker.text.toString().isNotEmpty()) {
                dob = Utility.convertDate(binding.tvDatePicker.text.toString())
            }

            email = binding.emailTv.text.toString()
            if (email.isValidEmail()) {
                binding.tvEmail.isErrorEnabled = false
            } else if (email.isNotEmpty()) {
                binding.tvEmail.isErrorEnabled = true
                binding.tvEmail.error = Constants.PLEASE_ENTER_VALID_EMAIL
            }

            houseNo = binding.houseNo.text.toString()
            if (houseNo.isNotEmpty()) {
                if (houseNo.length == 150) {
                    binding.floorHouseNum.error =
                        Constants.YOU_HAVE_REACHED_THE_MAX_CHARACTERS_LIMIT
                } else {
                    houseNo = binding.houseNo.text.toString()
                }
            }
            address = binding.completeAddress.text.toString()
            if (address.isNotEmpty()) {
                if (address.length == 150) {
                    binding.comAdd.error = Constants.YOU_HAVE_REACHED_THE_MAX_CHARACTERS_LIMIT
                } else {
                    address = binding.completeAddress.text.toString()
                }
            }
            locality = binding.locality.text.toString()

            if (locality.isNotEmpty()) {
                if (locality.length == 150) {
                    binding.tvLocality.error = Constants.YOU_HAVE_REACHED_THE_MAX_CHARACTERS_LIMIT
                } else {
                    locality = binding.locality.text.toString()
                }
            }
            pinCode = binding.pincodeEditText.text.toString()
            if (locality.isNotEmpty()) {
                binding.pincode.isErrorEnabled = false
                pinCode = binding.pincodeEditText.text.toString()

            }
            countrySelected = binding.autoCountry.text.toString()
            if (countrySelected.isNotEmpty()) {
                binding.spinnerCountry.isErrorEnabled = false
                countrySelected = binding.autoCountry.text.toString()

            }
            stateSelected = binding.autoState.text.toString()
            if (stateSelected.isNotEmpty()) {
                binding.spinnerState.isErrorEnabled = false
                stateSelected = binding.autoState.text.toString()

            }
            citySelected = binding.autoCity.text.toString()
            if (citySelected.isNotEmpty()) {
                binding.spinnerCity.isErrorEnabled = false
                citySelected = binding.autoCity.text.toString()
            }

            if (email.isNotEmpty() && !email.isValidEmail()) {
                binding.tvEmail.error = Constants.PLEASE_ENTER_VALID_EMAIL
            } else {
                sendProfileDetail(
                    dob,
                    email,
                    houseNo,
                    address,
                    locality,
                    pinCode,
                    countrySelected,
                    stateSelected,
                    citySelected
                )
//                changeFontOnSave()

            }
        }

    }

    private fun removeSemiPictureDialog() {
        val removeDialogLayout = RemoveConfirmationBinding.inflate(layoutInflater)
        removePictureDialog = Dialog(requireContext())
        removePictureDialog.setCancelable(false)
        removePictureDialog.setContentView(removeDialogLayout.root)

        removeDialogLayout.actionYes.setOnClickListener {
            binding.cvProfileImage.visibility = View.GONE
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

    private fun showPermissionDeniedDialog() {
        val removeDialogLayout = RemoveConfirmationBinding.inflate(layoutInflater)
        removePictureDialog= Dialog(requireContext())
        removePictureDialog.setCancelable(true)
        removePictureDialog.setContentView(removeDialogLayout.root)
        removeDialogLayout.actionYes.setOnClickListener {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", context?.packageName, null)
            intent.data = uri
            startActivityForResult(intent, PICK_GALLERY_IMAGE)
        }
        removePictureDialog.show()

    }

    private fun removePictureDialog() {
        val removeDialogLayout = RemoveConfirmationBinding.inflate(layoutInflater)
        removePictureDialog = Dialog(requireContext())
        removePictureDialog.setCancelable(false)
        removePictureDialog.setContentView(removeDialogLayout.root)

        removeDialogLayout.actionYes.setOnClickListener {
            callDeletePic(data)
            binding.cvProfileImage.visibility = View.GONE
            binding.profileUserLetters.visibility = View.VISIBLE
            setUserNamePIC(data)
        }
        removeDialogLayout.actionNo.setOnClickListener {
            removePictureDialog.dismiss()
        }
        binding.tvremove.setOnClickListener {
            if (data.profilePictureUrl.isNullOrEmpty()) {
                Toast.makeText(context, Constants.PICTURE_ALREADY_REMOVED, Toast.LENGTH_SHORT)
                    .show()
            } else {
                removePictureDialog.show()
            }
        }
    }

    private fun sendProfileDetail(
        validDOB: String?,
        validEmail: String,
        validHouse: String,
        validAdd: String,
        validLocality: String,
        validPinCode: String,
        validCountry: String,
        validState: String,
        validCity: String
    ) {
        val editUserNameRequest = EditUserNameRequest(
            data.firstName ?: "",
            data.lastName ?: "",
            validEmail,
            validDOB,
            binding.autoGender.text.toString(),
            validHouse,
            validAdd,
            validLocality,
            validPinCode,
            validCity,
            validState,
            validCountry
        )
        profileViewModel.editUserNameProfile(editUserNameRequest)
            .observe(
                viewLifecycleOwner
            ) { it ->
                when (it!!.status) {
                    Status.LOADING -> {
                        binding.progressBaar.show()
                    }
                    Status.SUCCESS -> {
                        val dialog = EditProfileUpdatedPopUpFragment()
                        dialog.isCancelable = false
                        dialog.show(childFragmentManager, "submitted")
                        binding.progressBaar.hide()
                        appPreference.saveLogin(true)
                        binding.emailTv.clearFocus()
                        binding.houseNo.clearFocus()
                        binding.completeAddress.clearFocus()
                        binding.tvLocality.clearFocus()
                        binding.pincodeEditText.clearFocus()
                    }
                    Status.ERROR -> {
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                        binding.progressBaar.hide()
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
        if (!isReadStorageGranted && !isWriteStorageGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        Log.d("permission2", "" +isReadStorageGranted + " " + isWriteStorageGranted)

        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    private fun onCaptureImageResult() {
        destinationFile = Utility.getCompressedImageFile(cameraFile!!, context)!!
        val selectedImage = destinationFile?.path
        val thumbnail = BitmapFactory.decodeFile(selectedImage)
        binding.cvProfileImage.visibility = View.VISIBLE
        binding.profileUserLetters.visibility = View.GONE
        try {
            Glide.with(requireContext())
                .load(thumbnail)
                .transform(CircleTransform(requireContext()))
                .into(binding.ivProfile)
            binding.tvremove.visibility = View.GONE
            binding.textremove.visibility = View.VISIBLE
            binding.tvRemove2.setTextColor(Color.parseColor("#9192a0"))

            removeSemiPictureDialog()

        } catch (e: Exception) {
            e.message
        }
        type = Constants.CAMERA_CLICK
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
                type = Constants.GALLERY_CLICK
            } catch (e: Exception) {
                Log.e("Error", "onSelectFromGalleryResult: " + e.localizedMessage)
            }

            binding.cvProfileImage.visibility = View.VISIBLE
            binding.profileUserLetters.visibility = View.GONE
            Glide.with(requireContext())
                .load(bitmap)
                .transform(CircleTransform(requireContext()))
                .into(binding.ivProfile)
            binding.tvremove.visibility = View.GONE
            binding.textremove.visibility = View.VISIBLE
            binding.tvRemove2.setTextColor(Color.parseColor("#9192a0"))

            removeSemiPictureDialog()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun callingUploadPicApi(destinationFile: File) {
        profileViewModel.uploadProfilePicture(destinationFile, destinationFile.name)
            .observe(
                viewLifecycleOwner
            ) { it ->
                when (it?.status) {
                    Status.LOADING -> {
                        binding.progressBaar.show()
                    }
                    Status.SUCCESS -> {
                        setLightColor()
                        binding.progressBaar.hide()
                    }
                    Status.ERROR -> {
                        binding.progressBaar.hide()
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                    }
                }
            }
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
                        setColor()
                        removePictureDialog.dismiss()
                        if (data.profilePictureUrl == null) {
                            binding.cvProfileImage.visibility = View.GONE
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
            arrayOf<CharSequence>(
                Constants.TAKE_PHOTO,
                Constants.CHOOSE_FROM_GALLERY,
                Constants.CANCEL
            )
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(Constants.ADD_PHOTO)
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == Constants.TAKE_PHOTO -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        getPhotoFile(requireContext())
                    )
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    cameraLauncher.launch(intent)

                }
                options[item] == Constants.CHOOSE_FROM_GALLERY -> {
                    val intent =
                        Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                    resultLauncher.launch(intent)
                }
                options[item] == Constants.CANCEL -> {
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
                    Constants.NOTHING_SELECTED,
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
                if (Constants.IMAGE == type) {
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
            requireContext().applicationContext.packageName + Constants.DOT_PROVIDER,
            cameraFile!!
        )
    }

}








