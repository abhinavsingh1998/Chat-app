package com.emproto.hoabl.feature.profile.fragments.help_center

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentHelpCenterBinding
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.views.FaqDetailFragment
import com.emproto.hoabl.feature.profile.adapter.HelpCenterAdapter
import com.emproto.hoabl.feature.profile.data.DataHealthCenter
import com.emproto.hoabl.feature.profile.data.HelpModel
import com.emproto.hoabl.feature.profile.fragments.about_us.AboutUsFragment
import com.emproto.hoabl.feature.profile.fragments.feedback.FeedbackFragment
import com.emproto.hoabl.feature.profile.fragments.privacy.PrivacyFragment
import com.emproto.hoabl.utils.ItemClickListener
import javax.inject.Inject


class HelpCenterFragment : BaseFragment() {

    @Inject
    lateinit var binding: FragmentHelpCenterBinding
    private lateinit var adapter: HelpCenterAdapter
    lateinit var recyclerView: RecyclerView
    private var dataList = ArrayList<DataHealthCenter>()
    val bundle = Bundle()

    private var isAboutUsActive = false
    private var isTermsActive = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelpCenterBinding.inflate(inflater, container, false)
        arguments.let {
            isAboutUsActive = it?.getBoolean(Constants.IS_ABOUT_US_ACTIVE, false) as Boolean
            isTermsActive = it.getBoolean(Constants.IS_TERM_ACTIVE, false)

        }
        initView()
        return binding.root
    }

    private fun initView() {

        (requireActivity() as HomeActivity).hideHeader()
        (requireActivity() as HomeActivity).hideBottomNavigation()

        val item1 = DataHealthCenter(
            Constants.FAQ_TITLE,
            Constants.FAQ_DESCRIPTION,
            R.drawable.ic_faq,
            R.drawable.rightarrow,
            Constants.INFO
        )
        val item2 =
            DataHealthCenter(
                Constants.PRIVACY_POLICY_TITLE,
                Constants.PRIVACY_POLICY_DESCRIPTION,
                R.drawable.ic_privacy_policy,
                R.drawable.rightarrow,
                Constants.INFO
            )
        val item3 = DataHealthCenter(
            Constants.ABOUT_US_TITLE,
            Constants.ABOUT_US_DESCRIPTION,
            R.drawable.ic_info_button,
            R.drawable.rightarrow,
            Constants.INFO
        )
        val item4 = DataHealthCenter(
            Constants.FEEDBACK_TITLE,
            Constants.FEEDBACK_DESCRIPTION,
            R.drawable.ic_feedback,
            R.drawable.rightarrow,
            Constants.INFO
        )
        val item5 = DataHealthCenter(
            Constants.RATE_US_TITLE,
            Constants.RATE_US_DESCRIPTION,
            R.drawable.ic_rating_2,
            R.drawable.rightarrow,
            Constants.INFO
        )

        val listHolder = ArrayList<HelpModel>()
        listHolder.add(HelpModel(HelpCenterAdapter.VIEW_ITEM, item1))
        when (isTermsActive) {
            true -> listHolder.add(HelpModel(HelpCenterAdapter.VIEW_ITEM, item2))
        }
        when (isAboutUsActive) {
            true -> listHolder.add(HelpModel(HelpCenterAdapter.VIEW_ITEM, item3))
        }
        listHolder.add(HelpModel(HelpCenterAdapter.VIEW_ITEM, item4))
        listHolder.add(HelpModel(HelpCenterAdapter.VIEW_ITEM, item5))
        listHolder.add(HelpModel(HelpCenterAdapter.VIEW_FOOTER, item1))

        binding.rvHelpCenter.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHelpCenter.adapter = HelpCenterAdapter(
            requireContext(),
            listHolder,
            object : ItemClickListener {
                override fun onItemClicked(view: View, position: Int, item: String) {
                    when (item) {
                        Constants.FAQ_TITLE -> {
                            val fragment = FaqDetailFragment()
                            val bundle = Bundle()
                            bundle.putBoolean(Constants.IS_FROM_INVESTMENT, false)
                            bundle.putString(Constants.PROJECT_NAME, "")
                            fragment.arguments = bundle
                            (requireActivity() as HomeActivity).addFragment(
                                fragment,
                                true
                            )
                        }
                        Constants.ABOUT_US_TITLE -> {
                            (requireActivity() as HomeActivity).addFragment(
                                AboutUsFragment(),
                                true
                            )
                        }
                        Constants.PRIVACY_POLICY_TITLE -> {
                            (requireActivity() as HomeActivity).addFragment(
                                PrivacyFragment(),
                                true
                            )
                        }
                        Constants.FEEDBACK_TITLE -> {
                            (requireActivity() as HomeActivity).addFragment(
                                FeedbackFragment(),
                                true
                            )
                        }
                    }

                }

            },
            object : HelpCenterAdapter.FooterInterface {
                override fun onChatClick(position: Int) {
                    (requireActivity() as HomeActivity).addFragment(
                        ChatsFragment(),
                        true
                    )
                }

                override fun onPhoneNumberClick(position: Int) {
                    val u = Uri.parse("tel:" +Constants.TELEPHONE_NO )
                    val intent = Intent(Intent.ACTION_DIAL, u)
                    try {
                        startActivity(intent)
                    } catch (s: SecurityException) {
                        Toast.makeText(context, Constants.AN_ERROR_OCCURRED, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onEmailClick(position: Int) {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    val uri = Uri.parse(Constants.EMAIL)
                    intent.data = uri
                    startActivity(intent)
                }

            }
        )
        //back click
        binding.backAction.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


}
