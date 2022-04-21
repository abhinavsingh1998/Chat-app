package com.emproto.hoabl.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentFaqBinding
import com.emproto.hoabl.feature.profile.data.FaqData
import com.emproto.hoabl.feature.profile.adapter.FaqViewAdapter


class FaqFragment : Fragment() {
    lateinit var binding: FragmentFaqBinding
    lateinit var adapter: FaqViewAdapter
    lateinit var ivarrow1:ImageView
    lateinit var tvhelpcenter:TextView
    val bundle = Bundle()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFaqBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        val detailAdapter= FaqViewAdapter(requireContext(),initData())

        binding.recyclerView.adapter= detailAdapter
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=true
        initClickListener()
        return binding.root
    }
    private fun initClickListener() {

        binding.tvhelpcenter.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val aboutUsFragment = AboutUsFragment()
                (requireActivity()as HomeActivity).replaceFragment(aboutUsFragment.javaClass, "", true, bundle, null, 0, false)}
        })
        binding.ivarrow1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val healthCenterFragment = HealthCenterFragment()
                (requireActivity()as HomeActivity).replaceFragment(healthCenterFragment.javaClass, "", true, bundle, null, 0, false)}
        })
    }
    private fun initData(): ArrayList<FaqData> {
        val dataList: ArrayList<FaqData> = ArrayList<FaqData>()
        dataList.add(FaqData(FaqViewAdapter.FAQ_TEXT_VIEW, "Is the Prize quoted per sqft. for ISLE of Bliss all inclusive or there are other cost and expenses?","c","t"))
        dataList.add(FaqData(FaqViewAdapter.FAQ_TEXT_VIEW, "Answer will be written here. Answer will Answer will be written here. Answer will Answer will be written here.","h","m"))
        dataList.add(FaqData(FaqViewAdapter.FAQ_TEXT_VIEW, "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?","c","t"))
        dataList.add(FaqData(FaqViewAdapter.FAQ_TEXT_VIEW, "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?","c","t"))
        dataList.add(FaqData(FaqViewAdapter.FAQ_TEXT_VIEW, "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?","c","t"))
        dataList.add(FaqData(FaqViewAdapter.FAQ_CATEGORY_VIEW, "Category 2","Category 2","v"))
        dataList.add(FaqData(FaqViewAdapter.FAQ_TEXT_VIEW2, "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?","c","Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?"))
        dataList.add(FaqData(FaqViewAdapter.FAQ_TEXT_VIEW2, "Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?","c","Is the Prize quoted. for ISLE of Bliss all inclusive or there are other cost and expenses?"))

        return dataList
    }
}
