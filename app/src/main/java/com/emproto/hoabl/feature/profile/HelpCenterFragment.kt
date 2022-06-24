package com.emproto.hoabl.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentHelpCenterBinding
import com.emproto.hoabl.feature.chat.views.fragments.ChatsDetailFragment
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.views.FaqDetailFragment
import com.emproto.hoabl.feature.profile.adapter.HelpCenterAdapter
import com.emproto.hoabl.feature.profile.data.DataHealthCenter
import com.emproto.hoabl.feature.profile.data.HelpModel
import javax.inject.Inject


class HelpCenterFragment : BaseFragment() {

    @Inject
    lateinit var binding: FragmentHelpCenterBinding
    private lateinit var adapter: HelpCenterAdapter
    lateinit var recyclerView: RecyclerView
    private var dataList = ArrayList<DataHealthCenter>()
    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelpCenterBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {

        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.GONE
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            false

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
        listHolder.add(HelpModel(HelpCenterAdapter.VIEW_ITEM, item1))
        listHolder.add(HelpModel(HelpCenterAdapter.VIEW_ITEM, item2))
        listHolder.add(HelpModel(HelpCenterAdapter.VIEW_ITEM, item3))
        listHolder.add(HelpModel(HelpCenterAdapter.VIEW_ITEM, item4))
        listHolder.add(HelpModel(HelpCenterAdapter.VIEW_ITEM, item5))
        listHolder.add(HelpModel(HelpCenterAdapter.VIEW_FOOTER, item1))

        binding.rvHelpCenter.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHelpCenter.adapter = HelpCenterAdapter(
            requireContext(),
            listHolder,
            object : HelpCenterAdapter.HelpItemInterface {
                override fun onClickItem(position: Int) {
                    when (position) {
                        0 -> {
                            val fragment = FaqDetailFragment()
                            val bundle = Bundle()
                            bundle.putBoolean("isFromInvestment",false)
                            bundle.putString("ProjectName","General")
                            fragment.arguments = bundle
                            (requireActivity() as HomeActivity).addFragment(
                                fragment,
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
                        3 -> {
                            (requireActivity() as HomeActivity).addFragment(
                                FeedbackFragment(),
                                false
                            )
                        }
                    }

                }

            },
            object : HelpCenterAdapter.FooterInterface {
                override fun onChatClick(position: Int) {
                    (requireActivity() as HomeActivity).addFragment(
                        ChatsFragment(),
                        false
                    )
                }

            }
        )
        //back click
        binding.backAction.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }




}
