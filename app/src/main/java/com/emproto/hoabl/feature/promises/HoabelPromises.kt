package com.emproto.hoabl.feature.promises

import com.emproto.hoabl.feature.promises.adapter.HoabelPromiseAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentHoabelPromisesBinding
import com.emproto.hoabl.feature.promises.data.DataModel
import com.emproto.hoabl.feature.promises.data.PromisesData


class HoabelPromises : BaseFragment() {

    lateinit var binding: FragmentHoabelPromisesBinding
    private lateinit var adapter: HoabelPromiseAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var tv_promise: TextView
    private var dataList = ArrayList<DataModel>()
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoabelPromisesBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true
        val dataList = ArrayList<DataModel>()
        dataList.add(
            DataModel(
                "Security", "100% safe and secure physical possession",
                R.drawable.securitylock,
                R.drawable.ic_combined_shape__2_
            )
        )
        dataList.add(
            DataModel(
                "Wealth", "100% assured resale with Capital Protection",
                R.drawable.ic_group_64,
                R.drawable.ic_combined_shape__2_
            )
        )
        dataList.add(
            DataModel(
                "Transperancy", "100% Money back before registration.",
                R.drawable.ic_path_33289,
                R.drawable.ic_combined_shape__2_
            )
        )
        dataList.add(
            DataModel(
                "Liquidity", "Owning land will never feel like a gamble again.",
                R.drawable.ic_path_33289,
                R.drawable.ic_combined_shape__2_
            )
        )
        dataList.add(
            DataModel(
                "Liquidity", "Owning land will never feel like a gamble again.",
                R.drawable.ic_path_33289,
                R.drawable.ic_combined_shape__2_
            )
        )
        dataList.add(
            DataModel(
                "Liquidity", "Owning land will never feel like a gamble again.",
                R.drawable.ic_path_33289,
                R.drawable.ic_combined_shape__2_
            )
        )
        dataList.add(
            DataModel(
                "Liquidity", "Owning land will never feel like a gamble again.",
                R.drawable.ic_path_33289,
                R.drawable.ic_combined_shape__2_
            )
        )
        dataList.add(
            DataModel(
                "Liquidity", "Owning land will never feel like a gamble again.",
                R.drawable.ic_path_33289,
                R.drawable.ic_combined_shape__2_
            )
        )
        val list = ArrayList<PromisesData>()
        list.add(PromisesData(HoabelPromiseAdapter.TYPE_HEADER, "", "", emptyList()))
        list.add(PromisesData(HoabelPromiseAdapter.TYPE_LIST, "", "", dataList))

        binding.listPromises.layoutManager = LinearLayoutManager(requireActivity())
        binding.listPromises.adapter = HoabelPromiseAdapter(
            requireContext(),
            list,
            object : HoabelPromiseAdapter.PromisedItemInterface {
                override fun onClickItem(position: Int) {
                    (requireActivity() as HomeActivity).addFragment(PromisesDetailsFragment(), false)
                }

            })


        return binding.root
    }


}


