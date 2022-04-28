package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentLandSkusBinding
import com.emproto.hoabl.feature.investment.adapters.LandSkusAdapter
import com.emproto.hoabl.feature.investment.dialogs.ConfirmationDialog
import com.emproto.hoabl.model.RecyclerViewItem
import java.util.ArrayList

class LandSkusFragment:BaseFragment() {

    private lateinit var binding: FragmentLandSkusBinding
    private lateinit var landSkusAdapter: LandSkusAdapter

    val onLandSkusItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.btn_apply_now -> {
                    val confirmationDialog = ConfirmationDialog()
                    confirmationDialog.show(this.parentFragmentManager,"ConfirmationDialog")
                }
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentLandSkusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpRecyclerview()
    }

    private fun setUpUI() {
        (activity as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
    }

    private fun setUpRecyclerview() {
        val list = ArrayList<RecyclerViewItem>()
        list.add(RecyclerViewItem(1))
        list.add(RecyclerViewItem(2))
//        list.add(RecyclerViewItem(3))

        landSkusAdapter = LandSkusAdapter(this,list)
        binding.rvLandSkus.adapter = landSkusAdapter
    }


}