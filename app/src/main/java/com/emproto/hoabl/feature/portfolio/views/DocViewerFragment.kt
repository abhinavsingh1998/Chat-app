package com.emproto.hoabl.feature.portfolio.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.core.Utility
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.response.enums.Status
import com.example.portfolioui.databinding.FragmentSingledocBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val FROM_PATH = "param1"
private const val ARG_PARAM2 = "param2"
private const val IMAGE_URL = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [DocViewerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DocViewerFragment : BaseFragment() {

    lateinit var binding: FragmentSingledocBinding

    // TODO: Rename and change types of parameters
    private var fromPath: Boolean = true
    private var name: String? = null
    private var imageUrl: String? = null

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioviewmodel: PortfolioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fromPath = it.getBoolean(FROM_PATH)
            name = it.getString(ARG_PARAM2)
            imageUrl = it.getString(IMAGE_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)

        binding = FragmentSingledocBinding.inflate(layoutInflater)
        portfolioviewmodel = ViewModelProvider(
            requireActivity(),
            portfolioFactory
        )[PortfolioViewModel::class.java]

        (requireActivity() as HomeActivity).hideHeader()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        initView()
        Log.d("tete",imageUrl.toString())
        if (fromPath) {
            initObserver()
        } else {
            //getting url from screen
            binding.tvMediaImageName.text = name
            binding.progressBar.visibility = View.GONE
            Glide.with(requireContext())
                .load(imageUrl)
                .into(binding.ivMediaPhoto)
        }
        return binding.root
    }

    private fun initView() {
        binding.tvMediaImageName.text = name
        binding.ivCloseButton.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }
    }

    private fun initObserver() {
        portfolioviewmodel.downloadDocument(imageUrl!!)
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        Status.SUCCESS -> {
                            binding.tvMediaImageName.text = name
                            binding.progressBar.visibility = View.GONE
                            Log.d("EEe",it.data?.data.toString())
                            when{
                                name!!.contains("pdf", ignoreCase = true) -> {
                                    openPdf(it.data!!.data)
                                }
                                name!!.contains("jpg", ignoreCase = true) -> {
                                    openImage(it.data!!.data)
                                }
                            }
                        }
                    }
                })
    }

    private fun openImage(data: String) {
        val bitmap = Utility.getBitmapFromBase64(data)
        val rotatedBitmap = bitmap?.let { it1 -> rotateImage(it1,90f) }
        binding.ivMediaPhoto.setImageBitmap(rotatedBitmap)
    }

    private fun openPdf(stringBase64: String) {
        Log.d("Base",stringBase64.toString())
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

    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
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

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri?): String? {
        val cursor = uri?.let { context?.getContentResolver()?.query(it, null, null, null, null) }
        cursor?.moveToFirst()
        val idx: Int = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)!!
        return cursor.getString(idx)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DocViewerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(fromPath: Boolean, name: String, imageUrl: String? = null) =
            DocViewerFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(FROM_PATH, fromPath)
                    putString(ARG_PARAM2, name)
                    putString(IMAGE_URL, imageUrl)
                }
            }
    }
}