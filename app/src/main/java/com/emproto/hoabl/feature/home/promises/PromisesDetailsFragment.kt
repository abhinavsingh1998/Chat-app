package com.emproto.hoabl.feature.home.promises

import android.graphics.Paint
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
import com.emproto.hoabl.databinding.FragmentPromiseDetailsBinding
import com.emproto.hoabl.feature.home.promises.adapter.PromiseDetailsAdapter

import com.emproto.hoabl.feature.home.promises.data.DetailsScreenData


class PromisesDetailsFragment : Fragment() {

    lateinit var binding: FragmentPromiseDetailsBinding
    lateinit var adapter: PromiseDetailsAdapter
    lateinit var button: Button

    private lateinit var newArrayList: ArrayList<DetailsScreenData>
    lateinit var imageId: Array<Int>
    lateinit var heading: Array<String>
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentPromiseDetailsBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val detailadapter = PromiseDetailsAdapter(requireContext(), initData())
        binding.recyclerView.adapter = detailadapter
        binding.recyclerView.setHasFixedSize(true)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true

        getUserData()
        return binding.root


    }

    private fun initData(): ArrayList<DetailsScreenData> {
        val dataList: ArrayList<DetailsScreenData> = ArrayList<DetailsScreenData>()

        newArrayList = arrayListOf<DetailsScreenData>()
        imageId = arrayOf(
            R.drawable.ic_circle,
            R.drawable.ic_circle,
            R.drawable.ic_circle,
            R.drawable.ic_circle,
            R.drawable.ic_circle
        )
        heading =
            arrayOf(
                "New Generation Land is now safe and secure. Owning land will never feel like a gamble again.",
                "New Generation Land is now safe and secure. Owning land will never feel like a gamble again.",
                "New Generation Land is now safe and secure. Owning land will never feel like a gamble again.",
                "New Generation Land is now safe and secure. Owning land will never feel like a gamble again.",
                "New Generation Land is now safe and secure. Owning land will never feel like a gamble again."
            )

        binding.textViewTAndC.setPaintFlags(binding.textViewTAndC.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)


        return dataList

    }

    private fun getUserData() {
        for (i in imageId.indices) {
            val news = DetailsScreenData(imageId[i], heading[i])
            newArrayList.add(news)
        }
        binding.recyclerView.adapter = PromiseDetailsAdapter(requireContext(), newArrayList)
    }
}