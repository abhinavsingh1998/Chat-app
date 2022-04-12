package com.emproto.hoabl.feature.home.promisesUi

import com.emproto.hoabl.feature.home.promisesUi.adapter.HoabelPromiseAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentHoabelPromisesBinding
import com.emproto.hoabl.feature.home.promisesUi.data.DataModel


class HoabelPromises : Fragment() {

    lateinit var binding: FragmentHoabelPromisesBinding
    private lateinit var adapter: HoabelPromiseAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var tv_promise:TextView
    private var dataList = ArrayList<DataModel>()
    val bundle = Bundle()
    private val onItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.title ->{
                    val firstPromiseScreenFragment = PromiseSecondScreenFragment()
                    (requireActivity() as HomeActivity).replaceFragment(firstPromiseScreenFragment.javaClass, "", true, null, null, 0, false)
                }
                R.id.fullview_Hoabel -> {
                    val firstPromiseScreenFragment= PromiseSecondScreenFragment()
                    (requireActivity() as HomeActivity).replaceFragment(firstPromiseScreenFragment.javaClass, "", true, null, null, 0, false)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoabelPromisesBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager= GridLayoutManager(requireContext(),2)
        val adapter= HoabelPromiseAdapter(requireContext())
        binding.recyclerView.adapter= adapter
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible = true




        dataList.add(DataModel("Security","100% safe and secure physical possession",
            R.drawable.securitylock,
            R.drawable.ic_combined_shape__2_))
        dataList.add(DataModel("Wealth","100% assured resale with Capital Protection",
            R.drawable.ic_group_64,
            R.drawable.ic_combined_shape__2_))
        dataList.add(DataModel("Transperancy","100% Money back before registration.",
            R.drawable.ic_path_33289,
            R.drawable.ic_combined_shape__2_))
        dataList.add(DataModel("Liquidity","Owning land will never feel like a gamble again.",
            R.drawable.ic_path_33289,
            R.drawable.ic_combined_shape__2_))
        dataList.add(DataModel("Liquidity","Owning land will never feel like a gamble again.",
            R.drawable.ic_path_33289,
            R.drawable.ic_combined_shape__2_))
        dataList.add(DataModel("Liquidity","Owning land will never feel like a gamble again.",
            R.drawable.ic_path_33289,
            R.drawable.ic_combined_shape__2_))
        adapter.setDataList(dataList)
        initClickListener()
        binding.recyclerView.adapter = adapter
        adapter.setItemClickListener(onItemClickListener)
        return binding.root
    }

    private fun initClickListener() {
        binding.tvPromise.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val firstPromiseScreenFragment =PromiseSecondScreenFragment()
                (requireActivity()as HomeActivity).replaceFragment(firstPromiseScreenFragment.javaClass, "", true, bundle, null, 0, false)}
        })

    }
}


