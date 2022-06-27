package com.emproto.hoabl.feature.profile.fragments.faq

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentFaqBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.profile.adapter.faq.ProfileFaqCategoryAdapter
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.ProfileFaqResponse
import javax.inject.Inject


class ProfileFaqFragment : Fragment() {

    @Inject
    lateinit var homeFactory: HomeFactory
    private lateinit var profileViewModel: ProfileViewModel
    var faqCategory = ArrayList<ProfileFaqResponse.ProfileFaqData>()
    lateinit var binding: FragmentFaqBinding
    lateinit var profileFaqCategoryAdapter: ProfileFaqCategoryAdapter
    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFaqBinding.inflate(layoutInflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[ProfileViewModel::class.java]
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true
        initClickListener()
        (requireActivity() as HomeActivity).hideBottomNavigation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfileFaqData()

    }

    private fun getProfileFaqData() {
        val typeOfFAQ="3001"
        profileViewModel.getFaqList(typeOfFAQ).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.loader.show()
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    if (it.data?.data != null) {
//                        faqCategory=it.data!!.data
                        setAdapter()
                        profileFaqCategoryAdapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        })

    }

    private fun setAdapter() {
        binding.rvHelpCenterCategory.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        profileFaqCategoryAdapter = ProfileFaqCategoryAdapter(context, faqCategory)
        binding.rvHelpCenterCategory.adapter = profileFaqCategoryAdapter
    }


    private fun initClickListener() {
        binding.backAction.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        })
    }

/*
    private fun initData(): ArrayList<FaqData> {
        val dataList: ArrayList<FaqData> = ArrayList<FaqData>()
        dataList.add(
            FaqData(
                FaqViewAdapter.FAQ_TEXT_VIEW,
                "Is the Prize quoted per sqft. for ISLE of Bliss all inclusive or there are other cost and expenses?",
                "c",
                "t"
            )
        )
        dataList.add(
            FaqData(
                FaqViewAdapter.FAQ_TEXT_VIEW,
                "Answer will be written here. Answer will Answer will be written here. Answer will Answer will be written here.",
                "h",
                "m"
            )
        )
        dataList.add(
            FaqData(
                FaqViewAdapter.FAQ_TEXT_VIEW,
                "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?",
                "c",
                "t"
            )
        )
        dataList.add(
            FaqData(
                FaqViewAdapter.FAQ_TEXT_VIEW,
                "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?",
                "c",
                "t"
            )
        )
        dataList.add(
            FaqData(
                FaqViewAdapter.FAQ_TEXT_VIEW,
                "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?",
                "c",
                "t"
            )
        )
        dataList.add(FaqData(FaqViewAdapter.FAQ_CATEGORY_VIEW, "Category 2", "Category 2", "v"))
        dataList.add(
            FaqData(
                FaqViewAdapter.FAQ_TEXT_VIEW2,
                "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?",
                "c",
                "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?"
            )
        )
        dataList.add(
            FaqData(
                FaqViewAdapter.FAQ_TEXT_VIEW2,
                "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?",
                "c",
                "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?"
            )
        )

        return dataList
    }
*/
}
