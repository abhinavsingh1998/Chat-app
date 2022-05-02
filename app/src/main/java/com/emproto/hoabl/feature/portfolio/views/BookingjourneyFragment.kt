package com.emproto.hoabl.feature.portfolio.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.hoabl.R
import com.example.portfolioui.adapters.BookingJourneyAdapter
import com.example.portfolioui.databinding.FragmentBookingjourneyBinding
import com.example.portfolioui.models.BookingModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Bookingjourney.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingjourneyFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    lateinit var mBinding: FragmentBookingjourneyBinding

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
        initView()
        return mBinding.root
    }

    private fun initView() {
        val bookingList = ArrayList<BookingModel>()
        bookingList.add(BookingModel(BookingJourneyAdapter.TYPE_HEADER))
        mBinding.bookingjourneyList.layoutManager = LinearLayoutManager(requireContext())
        mBinding.bookingjourneyList.adapter =
            BookingJourneyAdapter(requireContext(), bookingList, null)
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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingjourneyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}