package com.emproto.hoabl.feature.portfolio.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emproto.hoabl.feature.portfolio.views.PortfolioExistingUsersFragment
import com.emproto.hoabl.feature.portfolio.views.ManageProjectFragment


class PortfolioPagerAdapter(fragmentManager: FragmentManager?, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager!!, lifecycle) {

    companion object{
        const val NUM_TABS = 2
    }

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return PortfolioExistingUsersFragment()

        }
        return ManageProjectFragment()
    }
    }
