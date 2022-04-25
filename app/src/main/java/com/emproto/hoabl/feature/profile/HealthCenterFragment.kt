package com.emproto.hoabl.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentHealthCenterBinding

import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.profile.adapter.HoabelHealthAdapter
import com.emproto.hoabl.feature.profile.data.DataHealthCenter
import com.emproto.hoabl.feature.profile.data.HelpModel


class HealthCenterFragment :BaseFragment() {


    lateinit var binding: FragmentHealthCenterBinding
    private lateinit var adapter: HoabelHealthAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var tv_promise: TextView

    private var dataList = ArrayList<DataHealthCenter>()
    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHealthCenterBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true

        val dataList = ArrayList<DataHealthCenter>()
        dataList.add(
            DataHealthCenter(R.drawable.ic_view,
                "Read all our FAQs here",
                "Frequently Asked Questions",
                R.drawable.ic_faq,
                R.drawable.rightarrow,
                "Or Call us: +91 123 123 1231 Email us: help@hoabl.in"

            )

        )
        dataList.add(
            DataHealthCenter(R.drawable.ic_view,
                "Read our privacy policy",
                "Privacy Policy",
                R.drawable.ic_privacy_policy,
                R.drawable.rightarrow,
                "Or Call us: +91 123 123 1231 Email us: help@hoabl.in"

            )
        )
        dataList.add(
            DataHealthCenter(R.drawable.ic_view,
                "Read everything you want to know about us",
                "About Us",
                R.drawable.ic_info_button,
                R.drawable.rightarrow,
                "Or Call us: +91 123 123 1231 Email us: help@hoabl.in"

            )
        )
        dataList.add(
            DataHealthCenter(R.drawable.ic_view,
                "This will help us improve the app for you",
                "Share your feedback",
                R.drawable.ic_feedback,
                R.drawable.rightarrow,
                "Or Call us: +91 123 123 1231 Email us: help@hoabl.in"

            )
        )
        dataList.add(
            DataHealthCenter(R.drawable.ic_view,
                "Let us know your love for us! Rate us on the store",
                "Rate us!",
                R.drawable.ic_rating_2,
                R.drawable.rightarrow,
                "Or Call us: +91 123 123 1231 Email us: help@hoabl.in"

            )
        )
        val listHolder = ArrayList<HelpModel>()
        listHolder.add(HelpModel(HoabelHealthAdapter.VIEW_HELP_CENTER_LOCATION_ACCESS,
            "",
            "",
            R.drawable.ic_faq,
            R.drawable.rightarrow,
            emptyList()))
        listHolder.add(HelpModel(HoabelHealthAdapter.TYPE_LIST,
            "",
            "",
            R.drawable.ic_faq,
            R.drawable.rightarrow,
            dataList))


        val list = ArrayList<HelpModel>()
        val listPromises:RecyclerView
        list.add(HelpModel(HoabelHealthAdapter.VIEW_HELP_CENTER_LOCATION_ACCESS, "", "",R.drawable.ic_faq,R.drawable.rightarrow, emptyList()))
        list.add(HelpModel(HoabelHealthAdapter.TYPE_LIST, "", "",R.drawable.ic_faq,R.drawable.rightarrow,dataList))
        binding.healthCenterRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.healthCenterRecyclerView.adapter = HoabelHealthAdapter(requireContext(), list, object : HoabelHealthAdapter.HelpItemInterface {
                override fun onClickItem(position: Int) {
                    (requireActivity() as HomeActivity).addFragment(FaqFragment(),
                        false)
                }

            })
        return binding.root
    }


}
