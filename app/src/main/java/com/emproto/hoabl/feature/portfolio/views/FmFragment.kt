package com.emproto.hoabl.feature.portfolio.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.*
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.databinding.FragmentFmBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.model.ContactsModel
import com.emproto.hoabl.model.DownloadModel
import com.emproto.hoabl.model.UploadModel
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.webview.ShareObjectModel
import com.google.gson.Gson
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class FmFragment : BaseFragment() {

    lateinit var binding: FragmentFmBinding

    @Inject
    lateinit var homeFactory: HomeFactory
    private lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var appPreference: AppPreference

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var cameraFile: File? = null

    private var destinationFile = File("")

    private var contacts = HashMap<String, String>()

    private lateinit var permissionLauncherForContacts: ActivityResultLauncher<Array<String>>
    private lateinit var permissionLauncherForUpload: ActivityResultLauncher<Array<String>>
    private lateinit var builder: AlertDialog.Builder

    private val permissionRequestForContacts: MutableList<String> = ArrayList()
    private val permissionRequestForUpload: MutableList<String> = ArrayList()

    private var isContactPermissionGranted = false
    private var isReadPermissionGranted = false
    private var isWritePermissionGranted = false

    private var uploadObject = UploadModel("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        permissionLauncherForContacts =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isContactPermissionGranted =
                    permissions[Manifest.permission.READ_CONTACTS]
                        ?: isContactPermissionGranted

                if (isContactPermissionGranted) {
                    readContacts()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please give contacts permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        permissionLauncherForUpload =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

                isReadPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                    ?: isReadPermissionGranted
                isWritePermissionGranted =
                    permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                        ?: isWritePermissionGranted

                if (isWritePermissionGranted && isReadPermissionGranted) {
                    Toast.makeText(requireContext(), "Storage access granted", Toast.LENGTH_SHORT)
                        .show()
                    selectImage()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please give storage permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        builder = AlertDialog.Builder(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFmBinding.inflate(layoutInflater)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[ProfileViewModel::class.java]
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        (requireActivity() as HomeActivity).hideHeader()
        (requireActivity() as HomeActivity).showBottomNavigation()
        binding.webView.webViewClient =
            MyWebViewClient(
                binding.progressBaar,
                requireContext(),
                contacts,
                binding.webView,
                this,
                binding
            )
        true.also { binding.webView.settings.javaScriptEnabled = it }
        true.also { binding.webView.settings.builtInZoomControls = it }
        binding.webView.settings.displayZoomControls = false
        binding.webView.settings.domStorageEnabled = true
        binding.webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        binding.webView.addJavascriptInterface(JSBridge(), "JSBridge")
        binding.webView.loadUrl(param1.toString())

        //handling refresh scanario
        binding.noInternetView.textView6.setOnClickListener {
            binding.noInternetView.mainContainer.visibility = View.GONE
            binding.webView.loadUrl(param1.toString())
        }
        return binding.root
    }

    val onComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            binding.progressBaar.hide()
            Toast.makeText(requireContext(), "Downloading Complete", Toast.LENGTH_SHORT).show()
        }
    }

    inner class JSBridge() {
        @JavascriptInterface
        fun sendUploadActionInNative(obj: String) {
            Log.d("Share", "message from upload $obj")
            val gson = Gson()
            val objectUpl = gson.fromJson(obj, UploadModel::class.java)
            Log.d("Share", "message from upload $objectUpl")
            uploadObject = objectUpl
            requestStoragePermission()
        }

        @JavascriptInterface
        fun sendDownloadActionInNative(url: String) {
            Log.d("Share", "message from document $url")
            val gson = Gson()
            val urlD = gson.fromJson(url, DownloadModel::class.java)
            if (urlD != null) {
                binding.webView.post {
                    val request = DownloadManager.Request((Uri.parse(urlD.url)))
                    request.setDescription("Downloading file...")
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    val dm =
                        requireActivity().getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
                    binding.progressBaar.show()
                    dm!!.enqueue(request)
                    Toast.makeText(requireContext(), "Downloading...", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().registerReceiver(
                        onComplete,
                        IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                    )
                }
            } else {
                Toast.makeText(requireContext(), "Download url is empty", Toast.LENGTH_SHORT).show()
            }
        }

        @JavascriptInterface
        fun sendContactActionInNative() {
            Log.d("Share", "message from contact")
            if (Build.VERSION.SDK_INT >= 23) {
                requestContactPermission()
            } else {
                readContacts()
            }
        }

        @JavascriptInterface
        fun shareActionInNative(message: String) {
            Log.d("Share", "message from share= $message")
            val gson = Gson()
            val model = gson.fromJson(message, ShareObjectModel::class.java)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            shareIntent.type = "text/plain"
            when (model.download_url) {
                null -> {
                    shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "${model.message}"
                    )
                }
                else -> {
                    shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "${model.message}\nDownload the gatepass via this url: \n${model.download_url}"
                    )
                }
            }

            startActivity(shareIntent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FmFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    open class MyWebViewClient(
        val progressBar: ProgressBar, val requireContext: Context,
        var contacts: HashMap<String, String>,
        val webView: WebView,
        val fragment: FmFragment,
        val binding: FragmentFmBinding
    ) :
        WebViewClient() {

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            Log.d("Share", url.toString())
            if (url!!.startsWith("tel:")) {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse(url)
                )
                requireContext.startActivity(intent)
            } else if (url!!.startsWith("http:") || url!!.startsWith("https:")) {
                view!!.loadUrl(url!!)
            }
            return true
        }


        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.visibility = View.VISIBLE
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            Log.e("Error", error.toString())
            Toast.makeText(
                requireContext,
                "Your Internet Connection May not be active",
                Toast.LENGTH_SHORT
            ).show()
            //view!!.loadUrl("about:blank");
            binding.webView.visibility = View.GONE
            binding.noInternetView.mainContainer.visibility = View.VISIBLE
            binding.noInternetView.textView3.text = error!!.description.toString()
            binding.noInternetView.textView6.visibility = View.GONE
            binding.noInternetView.textView3.textSize = 18F
        }

        fun hasPermissions(context: Context?, vararg permissions: Array<String>): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (permission in permissions) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            permission[0]
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        return true
                    }
                }
            }
            return false
        }

    }

    private fun requestContactPermission() {
        isContactPermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        if (!isContactPermissionGranted) {
            permissionRequestForContacts.add(Manifest.permission.READ_CONTACTS)
        } else {
            readContacts()
        }

        if (permissionRequestForContacts.isNotEmpty()) {
            permissionLauncherForContacts.launch(permissionRequestForContacts.toTypedArray())
        }

    }

    private fun requestStoragePermission() {
        isReadPermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isWritePermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (!isReadPermissionGranted || !isWritePermissionGranted) {
            permissionRequestForUpload.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionRequestForUpload.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            selectImage()
        }
        if (permissionRequestForUpload.isNotEmpty()) {
            permissionLauncherForUpload.launch(permissionRequestForUpload.toTypedArray())
        }
    }

    @SuppressLint("Range")
    fun readContactsV() {
        val cr: ContentResolver = this.requireContext().contentResolver
        val cur = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )

        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID)
                )
                val name: String = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                if (cur.getInt(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    ) > 0
                ) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    if (pCur != null) {
                        val ctList = HashMap<String, String>()
                        while (pCur.moveToNext()) {
                            val phoneNo: String = pCur.getString(
                                pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                            ctList[phoneNo] = name
                        }
                        val gson = Gson()
                        binding.webView.post {
                            binding.webView.evaluateJavascript(
                                "javascript: " + "getContactListFromNative(\"" + gson.toJson(ctList) +
                                        "\")", null
                            )
                        }
                        Log.d("hello", "${gson.toJson(ctList)}")
                    }
                    pCur?.close()
                }
            }
        }
        cur?.close()
    }

    @SuppressLint("Range")
    fun readContacts() {
        val cArray = mutableListOf<ContactsModel>()
        val cr: ContentResolver = requireActivity().contentResolver
        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID)
                )
                val name: String = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                if (cur.getInt(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    ) > 0
                ) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    if (pCur != null) {

                        val hashMap = HashMap<String, String>()
                        while (pCur.moveToNext()) {
                            val phoneNo: String = pCur.getString(
                                pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                            hashMap[phoneNo.replace("\\s".toRegex(), "")] = name

                        }
                        for ((key, value) in hashMap) {
                            cArray.add(ContactsModel(name = value, phoneNo = key))
                        }
                        Log.d("Count", cArray.size.toString())
                        val gson = Gson()
                        val data = gson.toJson(cArray)
                        Log.d("JSON", data.toString())
                        binding.webView.post {
                            binding.webView.evaluateJavascript(
                                "javascript: getContactListFromNative( $data)", null
                            )
                            binding.progressBaar.hide()
                        }

                    }
                    pCur?.close()
                }
            }
        }
        cur?.close()
    }

    private fun selectImage() {
        val options =
            arrayOf<CharSequence>(
                Constants.TAKE_PHOTO,
                Constants.CHOOSE_FROM_GALLERY,
                Constants.CANCEL
            )

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
                    dialog.dismiss()
                }
                options[item] == Constants.CHOOSE_FROM_GALLERY -> {
                    when (uploadObject.type) {
                        "image" -> {
                            val intent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            dialog.dismiss()
                            resultLauncher.launch(intent)
                        }
                        "doc" -> {
                            val pdfIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                            pdfIntent.type = "application/pdf"
                            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
                            dialog.dismiss()
                            resultLauncher.launch(pdfIntent)
                        }
                    }
                }
                options[item] == Constants.CANCEL -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun getPhotoFile(context: Context): Uri? {
        val fileSuffix = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
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

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result != null && result.resultCode === Activity.RESULT_OK) {
            if (result.data != null) {
                onSelectFromGalleryResult(result.data!!)
            }
        }
    }

    private fun onCaptureImageResult() {
        destinationFile = Utility.getCompressedImageFile(cameraFile!!, context)!!
        callUploadApi(destinationFile)

    }

    private fun callUploadApi(destinationFile: File) {
        profileViewModel.uploadFm(uploadObject.type, uploadObject.page_name, destinationFile)
            .observe(
                viewLifecycleOwner
            ) {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBaar.show()
                    }
                    Status.SUCCESS -> {
                        binding.progressBaar.hide()
                        it.data?.let { fm ->
                            Log.d("upload", fm.toString())
                            val gson = Gson()
                            val jsonData = gson.toJson(fm)
                            Log.d("UploadSend", jsonData)
                            binding.webView.post {
                                binding.webView.evaluateJavascript(
                                    "javascript: getUploadActionFromNative( $jsonData)",
                                    null
                                )
                            }
                        }
                    }
                    Status.ERROR -> {
                        binding.progressBaar.hide()
                        (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                    }
                }
            }
    }

    private fun onSelectFromGalleryResult(data: Intent) {
        val selectedImage = data.data
        Log.e("pdfFile", selectedImage.toString())
        val inputStream =
            requireContext().contentResolver.openInputStream(selectedImage!!)
        if (uploadObject.type.equals("doc")) {
            try {
                Log.d("filepath", selectedImage.toString())
                destinationFile = fileFromContentUri(requireContext(), selectedImage)
                callUploadApi(destinationFile)

            } catch (e: Exception) {
                Log.e("Error", "onSelectFromGalleryResult: " + Constants.SOMETHING_WENT_WRONG)
            }
        } else {
            try {
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val bytes = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

                try {
                    val filePath = getRealPathFromURI_API19(requireContext(), selectedImage)
                    Log.d("filepath", filePath.toString())
                    destinationFile = File(filePath)
                    callUploadApi(destinationFile)

                } catch (e: Exception) {
                    Log.e("Error", "onSelectFromGalleryResult: " + Constants.SOMETHING_WENT_WRONG)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()

            }
        }

    }


    @SuppressLint("NewApi")
    fun getRealPathFromURI_API19(context: Context, uri: Uri): String? {
        val isKitKat = true

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
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "")
                }
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

    private fun encodeImage(path: String): String? {
        val imageFile = File(path)
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(imageFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bm = BitmapFactory.decodeStream(fis)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        //Base64.de
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

//    @SuppressLint("Range")
//    private fun getPdfPath(context:Context, uri:Uri):String?{
//        val uriString: String = uri.path.toString()
//        var pdfName: String? = null
//
//        if (uriString.startsWith("content://")) {
//            var myCursor: Cursor? = null
//            try {
//                myCursor = context.contentResolver.query(uri, null, null, null, null)
//                if (myCursor != null && myCursor.moveToFirst()) {
//                    pdfName =
//                        myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
//                }
//            } finally {
//                myCursor?.close()
//            }
//        }
//        return pdfName
//    }

    fun fileFromContentUri(context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }
}