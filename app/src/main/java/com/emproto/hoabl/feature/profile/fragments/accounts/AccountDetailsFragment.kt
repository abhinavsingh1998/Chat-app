package com.emproto.hoabl.feature.profile.fragments.accounts

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseActivity
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.DocumentsBottomSheetBinding
import com.emproto.hoabl.databinding.FragmentAccountDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.portfolio.views.DocViewerFragment
import com.emproto.hoabl.feature.profile.adapter.accounts.*
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.AccountsResponse
import com.emproto.networklayer.response.profile.KycUpload
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class AccountDetailsFragment : Fragment(),
    AccountsPaymentListAdapter.OnPaymentItemClickListener,
    AccountKycUploadAdapter.OnKycItemUploadClickListener,
    AllDocumentAdapter.OnAllDocumentLabelClickListener,
    AccountsDocumentLabelListAdapter.OnDocumentLabelItemClickListener {

    companion object {
        const val DOC_CATEGORY_KYC = 100100
        const val DOC_TYPE_PAN_CARD = 200100
        const val DOC_TYPE_ADDRESS_PROOF = 200101
        const val DOC_TYPE_UNVERIFIED_PAN_CARD = 200109
        const val DOC_TYPE_UNVERIFIED_ADDRESS_PROOF = 200110
    }

    private lateinit var kycUploadAdapter: AccountKycUploadAdapter

    @Inject
    lateinit var profileFactory: ProfileFactory
    lateinit var profileViewModel: ProfileViewModel

    lateinit var binding: FragmentAccountDetailsBinding
    val bundle = Bundle()
    private lateinit var allPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>

    private lateinit var allKycDocList: ArrayList<AccountsResponse.Data.Document>
    private lateinit var kycLists: ArrayList<AccountsResponse.Data.Document>
    private lateinit var documentList: ArrayList<AccountsResponse.Data.Document>


    private lateinit var documentBinding: DocumentsBottomSheetBinding
    private lateinit var docsBottomSheet: BottomSheetDialog

    private var cameraFile: File? = null
    private lateinit var destinationFile: File
    private val PICK_GALLERY_IMAGE = 1
    private lateinit var bitmap: Bitmap
    private var selectedDocumentType = 0
    private val kycUploadList = ArrayList<KycUpload>()

    private var isReadPermissionGranted: Boolean = false
    private var isWritePermissionGranted: Boolean = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val permissionRequest: MutableList<String> = ArrayList()
    private var base64Data: String = ""
    var status = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).hideBottomNavigation()
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel = ViewModelProvider(requireActivity(), profileFactory)[ProfileViewModel::class.java]
        initView()
        initClickListener()
        callPermissionLauncher()
        return binding.root
    }

    private fun callPermissionLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadPermissionGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                        ?: isReadPermissionGranted
                isWritePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    ?: isWritePermissionGranted

                if (isReadPermissionGranted && isWritePermissionGranted) {
                    openPdf(base64Data)
                }
            }
    }

    private fun initView() {
        documentBinding = DocumentsBottomSheetBinding.inflate(layoutInflater)
        docsBottomSheet = BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)
        docsBottomSheet.setContentView(documentBinding.root)
        documentBinding.ivDocsClose.setOnClickListener {
            docsBottomSheet.dismiss()
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getAccountsList().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data?.data!!.documents != null && it.data!!.data.documents is List<AccountsResponse.Data.Document>) {
                        allKycDocList = it.data!!.data.documents as ArrayList<AccountsResponse.Data.Document>
                        kycLists = ArrayList<AccountsResponse.Data.Document>()
                        documentList = ArrayList<AccountsResponse.Data.Document>()
                        for (document in allKycDocList) {
                            if (document.documentCategory == DOC_CATEGORY_KYC) {
                                kycLists.add(document)
                            } else {
                                documentList.add(document)
                            }
                        }
                        setKycList()
                        setDocumentList()
                    }
                    if (it.data?.data!!.paymentHistory != null && it.data!!.data.paymentHistory is List<AccountsResponse.Data.PaymentHistory>) {
                        allPaymentList =
                            it.data!!.data.paymentHistory as ArrayList<AccountsResponse.Data.PaymentHistory>
                        setAllPaymentList()
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        }
    }

    private fun setAllPaymentList() {
        if (allPaymentList.isNullOrEmpty()) {
            binding.tvPaymentHistory.visibility = View.VISIBLE
            binding.cvNoPayment.visibility = View.VISIBLE
            binding.tvSeeAllPayment.visibility = View.GONE
            binding.rvPaymentHistory.visibility = View.VISIBLE
            allPaymentList.add(
                AccountsResponse.Data.PaymentHistory(
                    "1",
                    "2",
                    "3",
                    "4",
                    AccountsResponse.Data.PaymentHistory.Document(
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10",
                        11,
                        "12",
                        "13",
                        "14",
                        "15",
                        "16"
                    ),
                    17,
                    "18",
                    19F,
                    "20",
                    "21"
                )
            )
            binding.rvPaymentHistory.layoutManager =
                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            binding.rvPaymentHistory.adapter = AccountsPaymentListAdapter(
                context,
                allPaymentList,
                this, "empty"
            )
        } else {
            binding.tvPaymentHistory.visibility = View.VISIBLE
            binding.tvSeeAllPayment.visibility = View.VISIBLE
            binding.cvNoPayment.visibility = View.GONE
            binding.rvPaymentHistory.visibility = View.VISIBLE
            binding.rvPaymentHistory.layoutManager =
                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            binding.rvPaymentHistory.adapter = AccountsPaymentListAdapter(
                context,
                allPaymentList,
                this, "not"
            )
        }
    }

    private fun setDocumentList() {
        if (documentList.isNullOrEmpty()) {
            binding.rvDocuments.visibility = View.VISIBLE
            binding.tvSeeAllDocuments.visibility = View.GONE
            documentList.add(
                AccountsResponse.Data.Document(
                    "1",
                    "2",
                    "3",
                    "4",
                    5,
                    6,
                    7,
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13"
                )
            )
            binding.rvDocuments.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            binding.rvDocuments.adapter = AccountsDocumentLabelListAdapter(context, documentList, this, "empty")

        } else {
            binding.rvDocuments.visibility = View.VISIBLE
            binding.tvSeeAllDocuments.visibility = View.VISIBLE
            binding.rvDocuments.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            binding.rvDocuments.adapter = AccountsDocumentLabelListAdapter(context, documentList, this, "not")
        }
    }

    private fun setKycList() {
        when {
            kycLists.isNullOrEmpty() -> {
                kycUploadList.add(
                    KycUpload(
                        "Address Proof",
                        documentCategory = DOC_CATEGORY_KYC,
                        documentType = DOC_TYPE_ADDRESS_PROOF,
                        "UPLOAD"
                    )
                )
                kycUploadList.add(
                    KycUpload(
                        "PAN Card",
                        documentCategory = DOC_CATEGORY_KYC,
                        documentType = DOC_TYPE_PAN_CARD,
                        "UPLOAD"
                    )
                )
                kycUploadAdapter = AccountKycUploadAdapter(context, kycUploadList, this, viewListener)
                binding.rvKyc.adapter = kycUploadAdapter
            }
            else -> {
                kycUploadList.clear()
                kycUploadList.addAll(getKycList(kycLists))
                kycUploadAdapter = AccountKycUploadAdapter(context, kycUploadList, this, viewListener)
                binding.rvKyc.adapter = kycUploadAdapter
            }
        }

    }

    private fun getKycList(kycLists: ArrayList<AccountsResponse.Data.Document>): Collection<KycUpload> {
        val kycUploadList = ArrayList<KycUpload>()
        for (item in kycLists) {
            val name = when (item.documentType) {
                DOC_TYPE_ADDRESS_PROOF -> {
                    "Address Proof"
                }
                DOC_TYPE_UNVERIFIED_ADDRESS_PROOF -> {
                    "Address Proof"
                }
                DOC_TYPE_PAN_CARD -> {
                    "PAN Card"
                }
                else -> {
                    "PAN Card"
                }
            }
            when (name) {
                "Address Proof" -> {
                    if (item.documentType == DOC_TYPE_ADDRESS_PROOF) {
                        status = "View"
                    } else if (item.documentType == DOC_TYPE_UNVERIFIED_ADDRESS_PROOF) {
                        status = "Verification Pending"
                    }
                }
                "PAN Card" -> {
                    if (item.documentType == DOC_TYPE_PAN_CARD) {
                        status = "View"
                    } else if (item.documentType == DOC_TYPE_UNVERIFIED_PAN_CARD) {
                        status = "Verification Pending"
                    }
                }
            }
            kycUploadList.add(
                KycUpload(
                    name,
                    documentCategory = item.documentCategory,
                    documentType = item.documentType,
                    status = status,
                    path = item.path!!,
                    name = item.name!!
                )
            )
        }
        when (kycLists.size) {
            1 -> {
                when (kycLists[0].documentType) {
                    DOC_TYPE_ADDRESS_PROOF -> {
                        val kycItem = KycUpload(
                            "PAN Card",
                            documentCategory = DOC_CATEGORY_KYC,
                            documentType = DOC_TYPE_PAN_CARD,
                            status = "UPLOAD"
                        )
                        kycUploadList.add(kycItem)
                    }
                    DOC_TYPE_UNVERIFIED_ADDRESS_PROOF -> {
                        val kycItem = KycUpload(
                            "PAN Card",
                            documentCategory = DOC_CATEGORY_KYC,
                            documentType = DOC_TYPE_PAN_CARD,
                            status = "UPLOAD"
                        )
                        kycUploadList.add(kycItem)
                    }
                    DOC_TYPE_PAN_CARD -> {
                        val kycItem = KycUpload(
                            "Address Proof",
                            documentCategory = DOC_CATEGORY_KYC,
                            documentType = DOC_TYPE_ADDRESS_PROOF,
                            status = "UPLOAD"
                        )
                        kycUploadList.add(kycItem)
                    }
                    else -> {
                        val kycItem = KycUpload(
                            "Address Proof",
                            documentCategory = DOC_CATEGORY_KYC,
                            documentType = DOC_TYPE_ADDRESS_PROOF,
                            status = "UPLOAD"
                        )
                        kycUploadList.add(kycItem)
                    }
                }
            }
        }
        return kycUploadList
    }

    private val viewListener = object : AccountKycUploadAdapter.OnKycItemClickListener {
        override fun onAccountsKycItemClick(
            accountsDocumentList: ArrayList<KycUpload>,
            view: View,
            position: Int,
            name: String,
            path: String?
        ) {
            openDocumentScreen(name, path.toString())
        }

    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.tvSeeAllPayment.setOnClickListener {
            profileViewModel.savePaymentHistory(allPaymentList)
            val allPaymentHistoryFragment = AllPaymentHistoryFragment()
            (requireActivity() as HomeActivity).addFragment(allPaymentHistoryFragment, true)
        }
        binding.tvSeeAllDocuments.setOnClickListener {
            docsBottomSheet.show()
            documentBinding.rvDocsItemRecycler.layoutManager =
                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            documentBinding.rvDocsItemRecycler.adapter =
                AllDocumentAdapter(context, documentList, this)
        }
    }

    override fun onAccountsPaymentItemClick(
        accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
        view: View,
        position: Int,
        name: String,
        path: String
    ) {
        openDocumentScreen(name, path)
    }

    override fun onAllDocumentLabelClick(
        accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
        view: View,
        position: Int,
        name: String,
        path: String?
    ) {
        docsBottomSheet.dismiss()
        openDocumentScreen(name, path.toString())
    }

    override fun onAccountsDocumentLabelItemClick(
        accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
        view: View,
        position: Int,
        name: String,
        path: String?
    ) {
        docsBottomSheet.dismiss()
        openDocumentScreen(name, path.toString())
    }

    private fun openDocument(name: String, path: String) {
        (requireActivity() as HomeActivity).addFragment(
            DocViewerFragment.newInstance(true, name, path),
            true
        )
    }

    private fun openDocumentScreen(name: String, path: String) {
        val strings = name.split(".")
        if (strings.size > 1) {
            if (strings[1] == Constants.PNG_SMALL || strings[1] == Constants.JPG_SMALL) {
                //open image loading screen
                openDocument(name, path)
            } else if (strings[1] == Constants.PDF) {
                getDocumentData(path)
            } else {
                Toast.makeText(context, Constants.INVALID_FORMAT, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDocumentData(path: String) {
        profileViewModel.downloadDocument(path)
            .observe(viewLifecycleOwner
            ) {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Status.SUCCESS -> {
                        binding.progressBar.hide()
                        requestPermission(it.data!!.data)
                    }
                    Status.ERROR -> {
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                    }
                }
            }
    }

    private fun requestPermission(base64: String) {
        isReadPermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isWritePermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (!isReadPermissionGranted || !isWritePermissionGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            openPdf(base64)
        }
        if (permissionRequest.isNotEmpty()) {
            base64Data = base64
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }

    private fun openPdf(stringBase64: String) {
        val file = Utility.writeResponseBodyToDisk(stringBase64)
        if (file != null) {
            val path = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + Constants.DOT_PROVIDER,
                file
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(path, Constants.APPLICATION_PDF)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(Constants.ERROR_OPEN_PDF, e.localizedMessage)
            }
        } else {
            (requireActivity() as HomeActivity).showErrorToast(Constants.SOMETHING_WENT_WRONG)
        }
    }

    override fun onUploadClick(kycUploadList: ArrayList<KycUpload>, view: View, documentType: Int) {
        selectImage()
        selectedDocumentType = documentType
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

    private fun getPhotoFile(context: Context): Uri? {
        val fileSuffix = SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault()).format(Date())
        cameraFile = File(context.externalCacheDir, "$fileSuffix.jpg")
        return FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName + Constants.DOT_PROVIDER,
            cameraFile!!
        )
    }

    private var cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode === Activity.RESULT_OK) {
            onCaptureImageResult()
        }
    }

    private fun onCaptureImageResult() {
        destinationFile = Utility.getCompressedImageFile(cameraFile!!, context)!!
        if ((requireActivity() as BaseActivity).isNetworkAvailable()) {
            val extension: String =
                cameraFile?.name!!.substring(cameraFile?.name!!.lastIndexOf(".") + 1)
            callingUploadPicApi(destinationFile, extension)
        } else {
            (requireActivity() as BaseActivity).showError(
                Constants.PLEASE_CHECK_INTERNET_CONNECTIONS_TO_UPLOAD_IMAGE,
                binding.root

            )
        }
    }

    private fun callingUploadPicApi(destinationFile: File, extension: String) {
        profileViewModel.uploadKycDocument(extension, destinationFile, selectedDocumentType)
            .observe(
                viewLifecycleOwner
            ) { it ->
                when (it?.status) {
                    Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Status.SUCCESS -> {
                        binding.progressBar.hide()
                        it.data?.let {
                            if (it.data.response != null) {
                                if (it.data.response.data.name != null && it.data.response.data.name != null) {
                                    for (item in kycUploadList) {
                                        if (selectedDocumentType == DOC_TYPE_UNVERIFIED_ADDRESS_PROOF && item.documentName == "Address Proof") {
                                            item.name = it.data.response.data.name
                                            item.path = it.data.response.data.path
                                            item.status = "Verification Pending"
                                        } else if (selectedDocumentType == DOC_TYPE_UNVERIFIED_PAN_CARD && item.documentName == "PAN Card") {
                                            item.name = it.data.response.data.name
                                            item.path = it.data.response.data.path
                                            item.status = "Verification Pending"
                                        }
                                    }
                                    kycUploadAdapter.notifyDataSetChanged()
                                    val dialog = AccountKycStatusPopUpFragment()
                                    dialog.isCancelable = false
                                    dialog.show(childFragmentManager, "submitted")
                                }
                            } else {
                                (requireActivity() as HomeActivity).showErrorToast(Constants.SOMETHING_WENT_WRONG)
                            }

                        }
                    }
                    Status.ERROR -> {
                        binding.progressBar.hide()
                        Toast.makeText(
                            requireContext(),
                            it.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    null -> {}
                }
            }
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result != null && result.resultCode === Activity.RESULT_OK) {
            if (result.data != null) {
                onSelectFromGalleryResult(result.data!!)
            }
        }
    }

    private fun onSelectFromGalleryResult(data: Intent) {
        val selectedImage = data.data
        val inputStream =
            requireContext().contentResolver.openInputStream(selectedImage!!)
        try {
            bitmap = BitmapFactory.decodeStream(inputStream)
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            try {
                val filePath = getRealPathFromURI_API19(requireContext(), selectedImage)
                if ((requireActivity() as BaseActivity).isNetworkAvailable()) {
                    destinationFile = Utility.getCompressedImageFile(File(filePath), context)!!
                    val extension: String =
                        destinationFile.name.substring(destinationFile.name.lastIndexOf(".") + 1)
                    callingUploadPicApi(destinationFile, extension)
                } else {
                    (requireActivity() as BaseActivity).showError(
                        Constants.PLEASE_CHECK_INTERNET_CONNECTIONS_TO_UPLOAD_IMAGE,
                        binding.root
                    )
                }
            } catch (e: Exception) {
                Log.e("Error", "onSelectFromGalleryResult: " + e.localizedMessage)
            }


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
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
                when {
                    Constants.IMAGE == type -> {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" == type -> {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" == type -> {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
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

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    private fun getDataColumn(
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


}