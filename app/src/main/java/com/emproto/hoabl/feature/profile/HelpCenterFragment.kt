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


class HelpCenterFragment : BaseFragment() {


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
        initView()

        return binding.root
    }

    private fun initView() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true

        val item1 = DataHealthCenter(
            "Frequently Asked Questions",
            "Read all our FAQs here",
            R.drawable.ic_faq,
            R.drawable.rightarrow,
            "Or Call us: +91 123 123 1231 Email us: help@hoabl.in"

        )

        val item2 =
            DataHealthCenter(
                "Privacy Policy",
                "Read our privacy policy",
                R.drawable.ic_privacy_policy,
                R.drawable.rightarrow,
                "Or Call us: +91 123 123 1231 Email us: help@hoabl.in"

            )

        val item3 = DataHealthCenter(
            "About Us",
            "Read everything you want to know about us",
            R.drawable.ic_info_button,
            R.drawable.rightarrow,
            "Or Call us: +91 123 123 1231 Email us: help@hoabl.in"
        )

        val item4 = DataHealthCenter(
            "Share your feedback",
            "This will help us improve the app for you",
            R.drawable.ic_feedback,
            R.drawable.rightarrow,
            "Or Call us: +91 123 123 1231 Email us: help@hoabl.in"

        )
        val item5 = DataHealthCenter(
            "Rate us!",
            "Let us know your love for us! Rate us on the store",
            R.drawable.ic_rating_2,
            R.drawable.rightarrow,
            "Or Call us: +91 123 123 1231 Email us: help@hoabl.in"

        )
        val listHolder = ArrayList<HelpModel>()
        listHolder.add(HelpModel(HoabelHealthAdapter.VIEW_ITEM, item1))
        listHolder.add(HelpModel(HoabelHealthAdapter.VIEW_ITEM, item2))
        listHolder.add(HelpModel(HoabelHealthAdapter.VIEW_ITEM, item3))
        listHolder.add(HelpModel(HoabelHealthAdapter.VIEW_ITEM, item4))
        listHolder.add(HelpModel(HoabelHealthAdapter.VIEW_ITEM, item5))
        listHolder.add(HelpModel(HoabelHealthAdapter.VIEW_FOOTER, item1))

        //list.add(HelpModel(HoabelHealthAdapter.TYPE_LIST, "", "",R.drawable.ic_faq,R.drawable.rightarrow,dataList))
        binding.healthCenterRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.healthCenterRecyclerView.adapter = HoabelHealthAdapter(
            requireContext(),
            listHolder,
            object : HoabelHealthAdapter.HelpItemInterface {
                override fun onClickItem(position: Int) {
                    when (position) {
                        0 -> {
                            (requireActivity() as HomeActivity).addFragment(
                                FaqFragment(),
                                false
                            )
                        }
                        2 -> {
                            (requireActivity() as HomeActivity).addFragment(
                                AboutUsFragment(),
                                false
                            )
                        }
                        1 -> {
                            (requireActivity() as HomeActivity).addFragment(
                                PrivacyFragment(),
                                false
                            )
                        }
                    }

                }

            })
        //back click
        binding.backAction.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


}
