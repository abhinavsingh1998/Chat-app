package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentLandSkusBinding
import com.emproto.hoabl.feature.investment.adapters.LandSkusAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import java.util.ArrayList

class LandSkusFragment:BaseFragment() {

    private lateinit var binding: FragmentLandSkusBinding
    private lateinit var landSkusAdapter: LandSkusAdapter

    private val onLandSkusItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.tv_item_land_skus_apply_now -> {
                    Toast.makeText(this.requireContext(), "Hello boys!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentLandSkusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerview()
    }

    private fun setUpRecyclerview() {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(1))
        list.add(RecyclerViewItem(2))

        landSkusAdapter = LandSkusAdapter(list)
        binding.rvLandSkus.adapter = landSkusAdapter
        landSkusAdapter.setItemClickListener(onLandSkusItemClickListener)
    }




}