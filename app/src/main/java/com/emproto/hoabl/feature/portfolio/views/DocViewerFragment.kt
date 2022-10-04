package com.emproto.hoabl.feature.portfolio.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

private const val FROM_PATH = "param1"
private const val ARG_PARAM2 = "param2"
private const val IMAGE_URL = "param3"

class DocViewerFragment : BaseFragment() {

    lateinit var binding: FragmentSingledocBinding

    // TODO: Rename and change types of parameters
    private var fromPath: Boolean = true
    private var name: String? = null
    private var imageUrl: String? = null

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioViewModel: PortfolioViewModel

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
    ): View {
        // Inflate the layout for this fragment
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)

        binding = FragmentSingledocBinding.inflate(layoutInflater)
        portfolioViewModel = ViewModelProvider(
            requireActivity(),
            portfolioFactory
        )[PortfolioViewModel::class.java]

        (requireActivity() as HomeActivity).hideHeader()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        initView()
        Log.d("tete", imageUrl.toString())
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
        portfolioViewModel.downloadDocument(imageUrl!!)
            .observe(
                viewLifecycleOwner
            ) {
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
                    Status.ERROR -> {}
                }
            }
    }

    companion object {

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