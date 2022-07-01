package com.emproto.hoabl.feature.portfolio.views

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                            val bitmap = Utility.getBitmapFromBase64(it.data!!.data)
                            binding.ivMediaPhoto.setImageBitmap(bitmap)

                        }
                    }
                })
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
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