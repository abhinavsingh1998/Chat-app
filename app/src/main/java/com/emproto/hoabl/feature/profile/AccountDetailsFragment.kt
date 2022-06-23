package com.emproto.hoabl.feature.profile

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseActivity
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.DocumentsBottomSheetBinding
import com.emproto.hoabl.databinding.FragmentAccountDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.portfolio.views.DocViewerFragment
import com.emproto.hoabl.feature.profile.adapter.accounts.*
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.AccountsResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AccountDetailsFragment : Fragment(), AccountsKycListAdapter.OnKycItemClickListener,
    AccountsPaymentListAdapter.OnPaymentItemClickListener,
    AccountKycUploadAdapter.OnKycItemUploadClickListener,
    AllDocumentAdapter.OnAllDocumentLabelClickListener,
    AccountsDocumentLabelListAdapter.OnDocumentLabelItemClickListener {

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioviewmodel: PortfolioViewModel

    lateinit var binding: FragmentAccountDetailsBinding
    val bundle = Bundle()
    lateinit var allPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>
    lateinit var allKycDocList: ArrayList<AccountsResponse.Data.Document>

    lateinit var documentBinding: DocumentsBottomSheetBinding
    lateinit var docsBottomSheet: BottomSheetDialog

    lateinit var cameraFile: File
    lateinit var destinationFile: File
    private val PICK_GALLERY_IMAGE = 1
    lateinit var bitmap: Bitmap

    private var isReadPermissonGranted: Boolean = false
    private var isWritePermissonGranted: Boolean = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val permissionRequest: MutableList<String> = ArrayList()
    var base64Data: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]

        portfolioviewmodel = ViewModelProvider(requireActivity(), portfolioFactory)[PortfolioViewModel::class.java]
        initView()
        initClickListener()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =false
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


        homeViewModel.getAccountsList().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (it.data?.data!!.documents != null && it.data!!.data.documents is List<AccountsResponse.Data.Document>) {
                        allKycDocList =
                            it.data!!.data.documents as ArrayList<AccountsResponse.Data.Document>
                        val kycLists = ArrayList<AccountsResponse.Data.Document>()
                        val documentList = ArrayList<AccountsResponse.Data.Document>()
                        for (document in allKycDocList) {
                            if (document.documentCategory == "KYC") {
                                kycLists.add(document)
                            } else {
                                documentList.add(document)
                            }
                        }
                        if (kycLists.isNullOrEmpty()) {
                            val newList = ArrayList<String>()
                            newList.add("Address Proof")
                            newList.add("Pan Card")

                            binding.rvKyc.layoutManager =
                                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                            binding.rvKyc.adapter = AccountKycUploadAdapter(
                                context,
                                newList, this
                            )
                        } else {
                            binding.rvKyc.layoutManager =
                                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                            binding.rvKyc.adapter = AccountsKycListAdapter(
                                context,
                                kycLists,
                                this
                            )
                        }
                        if (documentList.isNullOrEmpty()) {
                            binding.rvDocuments.visibility = View.GONE
                            binding.tvSeeAllDocuments.visibility = View.VISIBLE
                            binding.cvNoDoc.visibility = View.VISIBLE

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


    private fun initClickListener() {
        binding.backAction.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }

        binding.tvSeeAllPayment.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                "accountResponse",
                allPaymentList
            )
            val allPaymentHistoryFragment = AllPaymentHistoryFragment()
            allPaymentHistoryFragment.arguments = bundle
            (requireActivity() as HomeActivity).replaceFragment(
                allPaymentHistoryFragment.javaClass,
                "",
                true,
                bundle,
                null,
                0,
                false
            )
        }
        binding.tvSeeAllDocuments.setOnClickListener {
            docsBottomSheet.show()
            documentBinding.rvDocsItemRecycler.layoutManager =
                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            documentBinding.rvDocsItemRecycler.adapter =
                AllDocumentAdapter(context, allKycDocList, this)
        }
    }


    override fun onAccountsKycItemClick(
        accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
        view: View,
        position: Int,
        name: String,
        path: String?
    ) {
        openDocument(name, path.toString())
    }

    override fun onAccountsPaymentItemClick(
        accountsPaymentList: ArrayList<AccountsResponse.Data.PaymentHistory>,
        view: View,
        position: Int
    ) {
    }

    private fun openDocument(name: String, path: String) {
        (requireActivity() as HomeActivity).addFragment(
            DocViewerFragment.newInstance(true, "Test.ong"),
            false
        )
    }

    private fun openDocumentScreen(name: String, path: String) {
        val strings = name.split(".")
        if (strings[1] == "png" || strings[1] == "jpg") {
            //open image loading screen
            openDocument(name, path)
        } else if (strings[1] == "pdf") {
            getDocumentData(path)
        } else {

        }
    }

    fun getDocumentData(path: String) {
        portfolioviewmodel.downloadDocument(path)
            .observe(viewLifecycleOwner,
                androidx.lifecycle.Observer {
                    when (it.status) {
                        Status.LOADING -> {
                            binding.progressBar.show()
                        }
                        Status.SUCCESS -> {
                            binding.progressBar.hide()
                            requestPermisson(it.data!!.data)
                        }
                        Status.ERROR -> {
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                        }
                    }
                })
    }

    private fun requestPermisson(base64: String) {
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
                file!!
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
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

    override fun onUploadClick(newList: ArrayList<String>, view: View, position: Int) {
        selectImage()
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

    fun getPhotoFile(context: Context): Uri? {
        val fileSuffix = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        cameraFile = File(context.externalCacheDir, "$fileSuffix.jpg")
        return FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName + ".provider",
            cameraFile
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
        val selectedImage = cameraFile.path
        destinationFile = cameraFile
        if ((requireActivity() as BaseActivity).isNetworkAvailable()) {
//            callingUploadPicApi(cameraFile)
        } else {
            (requireActivity() as BaseActivity).showError(
                "Please check Internet Connections to upload image",
                binding.root

            )
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
//                    callingUploadPicApi(destinationFile)

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

    override fun onAllDocumentLabelClick(
        accountsDocumentList: ArrayList<AccountsResponse.Data.Document>,
        view: View,
        position: Int,
        name: String,
        path: String?
    ) {
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

}