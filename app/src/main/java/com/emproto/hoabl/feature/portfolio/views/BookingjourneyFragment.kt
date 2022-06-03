package com.emproto.hoabl.feature.portfolio.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.example.portfolioui.adapters.BookingJourneyAdapter
import com.example.portfolioui.databinding.FragmentBookingjourneyBinding
import com.example.portfolioui.models.BookingModel
import com.example.portfolioui.models.BookingStepsModel
import java.util.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Bookingjourney.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingjourneyFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null
    lateinit var mBinding: FragmentBookingjourneyBinding

    val permissionRequest: MutableList<String> = ArrayList()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    var isReadPermissonGranted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentBookingjourneyBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        initView()
        return mBinding.root
    }

    private fun initView() {
        val bookingList = ArrayList<BookingModel>()
        val list = ArrayList<BookingStepsModel>()
        list.add(BookingStepsModel(0, "Application", "Payment 1"))
        list.add(BookingStepsModel(1, "Allotment", "payment 2"))
        bookingList.add(BookingModel(BookingJourneyAdapter.TYPE_HEADER))
        bookingList.add(BookingModel(BookingJourneyAdapter.TYPE_LIST, list))
        bookingList.add(BookingModel(BookingJourneyAdapter.TYPE_LIST, list))
        bookingList.add(BookingModel(BookingJourneyAdapter.TYPE_LIST, list))
        bookingList.add(BookingModel(BookingJourneyAdapter.TYPE_LIST, list))
        bookingList.add(BookingModel(BookingJourneyAdapter.TYPE_LIST, list))
        bookingList.add(BookingModel(BookingJourneyAdapter.TYPE_LIST, list))
        mBinding.bookingjourneyList.layoutManager = LinearLayoutManager(requireContext())
        mBinding.bookingjourneyList.adapter =
            BookingJourneyAdapter(
                requireContext(),
                bookingList,
                object : BookingJourneyAdapter.TimelineInterface {
                    override fun onClickItem(position: Int) {
                        TODO("Not yet implemented")
                    }

                    override fun viewDetails(position: Int, data: String) {
                        //get write permisson
                        //requestPermisson()
                        //openPdf("")
                    }

                })

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadPermissonGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissonGranted
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Bookingjourney.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingjourneyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun requestPermisson() {
        isReadPermissonGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (!isReadPermissonGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }

    private fun openPdf(stringBase64: String) {

    }

}