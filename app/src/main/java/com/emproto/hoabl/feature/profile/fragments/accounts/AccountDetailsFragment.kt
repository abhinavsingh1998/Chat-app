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
import android.graphics.Matrix
import android.media.ExifInterface
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseActivity
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

    lateinit var kycUploadAdapter: AccountKycUploadAdapter

    @Inject
    lateinit var profileFactory: ProfileFactory
    lateinit var profileViewModel: ProfileViewModel

    lateinit var binding: FragmentAccountDetailsBinding
    val bundle = Bundle()
    lateinit var allPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>

    lateinit var allKycDocList: ArrayList<AccountsResponse.Data.Document>
    lateinit var kycLists: ArrayList<AccountsResponse.Data.Document>


    lateinit var documentBinding: DocumentsBottomSheetBinding
    lateinit var docsBottomSheet: BottomSheetDialog

    private var cameraFile: File? = null
    lateinit var destinationFile: File
    private val PICK_GALLERY_IMAGE = 1
    lateinit var bitmap: Bitmap

    var selectedDocumentType = 0
    val kycUploadList = ArrayList<KycUpload>()

    private var isReadPermissonGranted: Boolean = false
    private var isWritePermissonGranted: Boolean = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val permissionRequest: MutableList<String> = ArrayList()
    var base64Data: String = ""
    var status = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)

        profileViewModel =
            ViewModelProvider(requireActivity(), profileFactory)[ProfileViewModel::class.java]
        initView()
        initClickListener()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            false
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadPermissonGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                        ?: isReadPermissonGranted
                isWritePermissonGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    ?: isWritePermissonGranted

                if (isReadPermissonGranted && isWritePermissonGranted) {
                    openPdf(base64Data)
                }
            }

        return binding.root
    }

    private fun initView() {
        documentBinding = DocumentsBottomSheetBinding.inflate(layoutInflater)
        docsBottomSheet =
            BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)
        docsBottomSheet.setContentView(documentBinding.root)

        documentBinding.ivDocsClose.setOnClickListener {
            docsBottomSheet.dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getAccountsList().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data?.data!!.documents != null && it.data!!.data.documents is List<AccountsResponse.Data.Document>) {
                        allKycDocList =
                            it.data!!.data.documents as ArrayList<AccountsResponse.Data.Document>
                        kycLists = ArrayList<AccountsResponse.Data.Document>()
                        val documentList = ArrayList<AccountsResponse.Data.Document>()
                        for (document in allKycDocList) {
                            if (document.documentCategory == DOC_CATEGORY_KYC) {
                                kycLists.add(document)
                            } else {
                                documentList.add(document)
                            }
                        }
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
                                kycUploadAdapter = AccountKycUploadAdapter(
                                    context,
                                    kycUploadList, this, viewListener
                                )
                                binding.rvKyc.adapter = kycUploadAdapter
                            }
                            else -> {
                                kycUploadList.clear()
                                kycUploadList.addAll(getKycList(kycLists))
                                kycUploadAdapter = AccountKycUploadAdapter(
                                    context,
                                    kycUploadList, this, viewListener
                                )
                                binding.rvKyc.adapter = kycUploadAdapter
                            }
                        }

                        if (documentList.isNullOrEmpty()) {
                            binding.rvDocuments.visibility = View.INVISIBLE
                            binding.tvSeeAllDocuments.visibility = View.GONE
                            binding.cvNoDoc.visibility = View.VISIBLE

                            val layout: RecyclerView =
                                requireActivity().findViewById(R.id.rvDocuments)
                            val params: ViewGroup.LayoutParams = layout.layoutParams
                            params.height = 100
                            params.width = 100
                            layout.layoutParams = params


                        } else {
                            binding.rvDocuments.visibility = View.VISIBLE
                            binding.tvSeeAllDocuments.visibility = View.VISIBLE
                            binding.cvNoDoc.visibility = View.GONE
                            binding.rvDocuments.layoutManager =
                                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                            binding.rvDocuments.adapter = AccountsDocumentLabelListAdapter(
                                context,
                                documentList,
                                this
                            )
                        }
                    }

                    if (it.data?.data!!.paymentHistory != null && it.data!!.data.paymentHistory is List<AccountsResponse.Data.PaymentHistory>) {
                        allPaymentList =
                            it.data!!.data.paymentHistory as ArrayList<AccountsResponse.Data.PaymentHistory>
                        if (allPaymentList.isNullOrEmpty()) {
                            binding.tvPaymentHistory.visibility = View.VISIBLE
                            binding.cvNoPayment.visibility = View.VISIBLE
                            binding.tvSeeAllPayment.visibility = View.GONE
                            binding.rvPaymentHistory.visibility = View.GONE
                        } else {
                            binding.tvPaymentHistory.visibility = View.VISIBLE
                            binding.tvSeeAllPayment.visibility = View.VISIBLE
                            binding.cvNoPayment.visibility = View.GONE
                            binding.rvPaymentHistory.visibility = View.VISIBLE
                            binding.rvPaymentHistory.layoutManager =
                                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                            binding.rvPaymentHistory.adapter = AccountsPaymentListAdapter(
                                context,
                                it.data!!.data.paymentHistory as ArrayList<AccountsResponse.Data.PaymentHistory>,
                                this
                            )
                        }


                    }
                }
                Status.ERROR -> {
                    binding.progressBar.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        })

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
                if (kycLists[0].documentType == DOC_TYPE_ADDRESS_PROOF) {
                    val kycItem = KycUpload(
                        "PAN Card",
                        documentCategory = DOC_CATEGORY_KYC,
                        documentType = DOC_TYPE_PAN_CARD,
                        status = "UPLOAD"
                    )
                    kycUploadList.add(kycItem)
                } else if (kycLists[0].documentType == DOC_TYPE_UNVERIFIED_ADDRESS_PROOF) {
                    val kycItem = KycUpload(
                        "PAN Card",
                        documentCategory = DOC_CATEGORY_KYC,
                        documentType = DOC_TYPE_PAN_CARD,
                        status = "UPLOAD"
                    )
                    kycUploadList.add(kycItem)
                } else if (kycLists[0].documentType == DOC_TYPE_PAN_CARD) {
                    val kycItem = KycUpload(
                        "Address Proof",
                        documentCategory = DOC_CATEGORY_KYC,
                        documentType = DOC_TYPE_ADDRESS_PROOF,
                        status = "UPLOAD"
                    )
                    kycUploadList.add(kycItem)
                } else {
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
        return kycUploadList
    }

    val viewListener = object : AccountKycUploadAdapter.OnKycItemClickListener {
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
        binding.backAction.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }

        binding.tvSeeAllPayment.setOnClickListener {
            profileViewModel.savePaymentHistory(allPaymentList)
            val allPaymentHistoryFragment = AllPaymentHistoryFragment()
            (requireActivity() as HomeActivity).supportFragmentManager.beginTransaction().add(
                R.id.container,
                allPaymentHistoryFragment,
                allPaymentHistoryFragment.javaClass.name
            )
                .addToBackStack(allPaymentHistoryFragment.javaClass.name).commit()
        }
        binding.tvSeeAllDocuments.setOnClickListener {
            docsBottomSheet.show()
            documentBinding.rvDocsItemRecycler.layoutManager =
                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            documentBinding.rvDocsItemRecycler.adapter =
                AllDocumentAdapter(context, allKycDocList, this)
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
            false
        )
    }

    private fun openDocumentScreen(name: String, path: String) {
        val strings = name.split(".")
        if (strings.size > 1) {
            if (strings[1] == "png" || strings[1] == "jpg") {
                //open image loading screen
                openDocument(name, path)
            } else if (strings[1] == "pdf") {
                getDocumentData(path)
            } else {
                Toast.makeText(context, "Invalid format", Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun getDocumentData(path: String) {
        profileViewModel.downloadDocument(path)
            .observe(viewLifecycleOwner,
                androidx.lifecycle.Observer {
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
                })
    }

    private fun requestPermission(base64: String) {
        isReadPermissonGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isWritePermissonGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (!isReadPermissonGranted || !isWritePermissonGranted) {
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
                requireContext().applicationContext.packageName + ".provider",
                file
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(path, "application/pdf")
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("Error:openPdf: ", e.localizedMessage)
            }
        } else {
            (requireActivity() as HomeActivity).showErrorToast("Something Went Wrong")
        }
    }

    override fun onUploadClick(kycUploadList: ArrayList<KycUpload>, view: View, documentType: Int) {
        selectImage()
        selectedDocumentType = documentType
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

    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
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

    var cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode === Activity.RESULT_OK) {
            onCaptureImageResult()
        }
    }

    private fun onCaptureImageResult() {
        val selectedImage = cameraFile?.path
        destinationFile = cameraFile!!
        val thumbnail = BitmapFactory.decodeFile(selectedImage)
        val ei = ExifInterface(cameraFile!!.path)
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        when (orientation) {
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
        if ((requireActivity() as BaseActivity).isNetworkAvailable()) {
            val extension: String =
                cameraFile?.name!!.substring(cameraFile?.name!!.lastIndexOf(".") + 1)
            callingUploadPicApi(cameraFile!!, extension)
        } else {
            (requireActivity() as BaseActivity).showError(
                "Please check Internet Connections to upload image",
                binding.root

            )
        }
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
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
                            if(it.data.response.data.name!=null && it.data.response.data.name!=null){
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
                }
            }
    }


    var resultLauncher = registerForActivityResult(
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
        var inputStream =
            requireContext().contentResolver.openInputStream(selectedImage!!)
        try {
            bitmap = BitmapFactory.decodeStream(inputStream)
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            try {
                val filePath = getRealPathFromURI_API19(requireContext(), selectedImage)
                if ((requireActivity() as BaseActivity).isNetworkAvailable()) {
                    destinationFile = File(filePath)
                    val extension: String =
                        destinationFile.name.substring(destinationFile.name.lastIndexOf(".") + 1)
                    callingUploadPicApi(destinationFile, extension)

                } else {
                    (requireActivity() as BaseActivity).showError(
                        "Please check Internet Connections to upload image",
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


}