package com.emproto.hoabl.feature.home.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emproto.hoabl.feature.home.views.fragments.FinancialSummaryFragment
import com.emproto.hoabl.feature.home.views.fragments.ManageProjectFragment

private const val NUM_TABS = 2
class PortfolioPagerAdapter(fragmentManager: FragmentManager?, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager!!, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FinancialSummaryFragment()

        }
        return ManageProjectFragment()
    }
    }
