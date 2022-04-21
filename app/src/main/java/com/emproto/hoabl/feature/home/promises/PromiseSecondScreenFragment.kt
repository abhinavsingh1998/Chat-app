package com.emproto.hoabl.feature.home.promises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPromiseSecondScreenBinding
import com.emproto.hoabl.feature.home.promises.adapter.PromiseSecondScreenAdapter

import com.emproto.hoabl.feature.home.promises.data.SecondScreenPromiseData


class PromiseSecondScreenFragment : Fragment() {

    lateinit var binding: FragmentPromiseSecondScreenBinding
    lateinit var adapter: PromiseSecondScreenAdapter
    lateinit var button: Button

    private lateinit var newArrayList: ArrayList<SecondScreenPromiseData>
    lateinit var imageId: Array<Int>
    lateinit var heading: Array<String>
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentPromiseSecondScreenBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val detailadapter = PromiseSecondScreenAdapter(requireContext(),initData())
        binding.recyclerView.adapter = detailadapter
        binding.recyclerView.setHasFixedSize(true)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true

        getUserData()
        return binding.root
    }
    private fun initData(): ArrayList<SecondScreenPromiseData> {
        val dataList: ArrayList<SecondScreenPromiseData> = ArrayList<SecondScreenPromiseData>()

        newArrayList = arrayListOf<SecondScreenPromiseData>()
        imageId = arrayOf(R.drawable.ic_circle,
            R.drawable.ic_circle,
            R.drawable.ic_circle,
            R.drawable.ic_circle,
            R.drawable.ic_circle)
        heading =
            arrayOf("New Generation Land is now safe and secure. Owning land will never feel like a gamble again.",
                "New Generation Land is now safe and secure. Owning land will never feel like a gamble again.",
                "New Generation Land is now safe and secure. Owning land will never feel like a gamble again.",
                "New Generation Land is now safe and secure. Owning land will never feel like a gamble again.",
                "New Generation Land is now safe and secure. Owning land will never feel like a gamble again.")
        return dataList
    }
    private fun getUserData() {
        for (i in imageId.indices) {
            val news = SecondScreenPromiseData(imageId[i], heading[i])
            newArrayList.add(news)
        }
       binding.recyclerView.adapter = PromiseSecondScreenAdapter(requireContext(),newArrayList)
    }
}