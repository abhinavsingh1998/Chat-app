package com.emproto.hoabl.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentHealthCenterBinding
import com.emproto.hoabl.feature.home.profileAdapter.HoabelHealthAdapter
import com.emproto.hoabl.feature.home.profileAdapter.data.DataHealthCenter


class HealthCenterFragment : Fragment() {

    lateinit var binding: FragmentHealthCenterBinding
    lateinit var adapter: HoabelHealthAdapter
    lateinit var button: Button
    lateinit var ivleftarrow:ImageView
    lateinit var hoabelImg:ImageView
    lateinit var tvhealthCenter:TextView
    lateinit var recyclerView: RecyclerView
    val bundle = Bundle()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentHealthCenterBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        val detailAdapter= HoabelHealthAdapter(requireContext(),initData())
        binding.recyclerView.adapter= detailAdapter
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=true
        initClickListener()
        return binding.root

    }

    private fun initClickListener() {
        binding.ivleftarrow.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val profileFragment = ProfileFragment()
                (requireActivity()as HomeActivity).replaceFragment(profileFragment.javaClass, "", true, bundle, null, 0, false)}
        })

        binding.tvhealthCenter.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val faqFragment = FaqFragment()
                (requireActivity()as HomeActivity).replaceFragment(faqFragment.javaClass, "", true, bundle, null, 0, false)}
        })


    }

    private fun initData(): ArrayList<DataHealthCenter> {
        val dataList: ArrayList<DataHealthCenter> = ArrayList<DataHealthCenter>()
        dataList.add(DataHealthCenter(HoabelHealthAdapter.VIEW_HELP_CENTER_ONE, "Read all our FAQs here","Frequently Asked Questions",
            R.drawable.ic_faq,
            R.drawable.ic_path,
            R.drawable.ic_faq,"Chat with us",
            R.drawable.ic_path,"Or\n" +
                    "Call us: +91 123 123 1231\n" +
                    "Email us: help@hoabl.in"))

        dataList.add(DataHealthCenter(HoabelHealthAdapter.VIEW_HELP_CENTER_ONE, "Read our privacy policy","Privacy Policy",
            R.drawable.ic_privacy_policy,
            R.drawable.ic_path,
            R.drawable.ic_privacy_policy,"Chat with us",
            R.drawable.ic_path,"Or\n" +
                    "Call us: +91 123 123 1231\n" +
                    "Email us: help@hoabl.in"))
        dataList.add(DataHealthCenter(HoabelHealthAdapter.VIEW_HELP_CENTER_ONE, "Read everything you want to know about us","About Us",
            R.drawable.ic_info_button,
            R.drawable.ic_path,
            R.drawable.ic_info_button,"Chat with us",
            R.drawable.ic_path,"Or\n" +
                    "Call us: +91 123 123 1231\n" +
                    "Email us: help@hoabl.in"))
        dataList.add(DataHealthCenter(HoabelHealthAdapter.VIEW_HELP_CENTER_ONE, "This will help us improve the app for you","Share your feedback",
            R.drawable.ic_feedback,
            R.drawable.ic_path,
            R.drawable.ic_feedback,"Chat with us",
            R.drawable.ic_path,"Or\n" +
                    "Call us: +91 123 123 1231\n" +
                    "Email us: help@hoabl.in"))
        dataList.add(DataHealthCenter(HoabelHealthAdapter.VIEW_HELP_CENTER_ONE, "Let us know your love for us! Rate us on the store","Rate us!",
            R.drawable.ic_rating_2,
            R.drawable.ic_rating_2,
            R.drawable.ic_faq,"Chat with us",
            R.drawable.ic_path,"Or\n" +
                    "Call us: +91 123 123 1231\n" +
                    "Email us: help@hoabl.in"))

        dataList.add(DataHealthCenter(HoabelHealthAdapter.VIEW_HELP_CENTER_TWO, "Want to connect?","hii",
            R.drawable.ic_faq,
            R.drawable.ic_path,
            R.drawable.ic_faq,"Chat with us",
            R.drawable.ic_path,"Or\n" +

                    "Call us: +91 123 123 1231\n" +

                    "Email us: help@hoabl.in"))

        return dataList

    }





}
