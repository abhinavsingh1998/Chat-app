package com.emproto.hoabl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.emproto.core.BaseFragment
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPortfolioBinding

class PortfolioFragment : BaseFragment() {

    lateinit var binding: FragmentPortfolioBinding
    lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPortfolioBinding.inflate(layoutInflater)
        initUi()
        return binding.root
    }

    private fun initUi() {

        binding.tabs.setupWithViewPager(binding.viewpager)
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, 0)
        viewPagerAdapter.addFragment(MyPortfolioFragment(), "My Portfolio Summary")
        viewPagerAdapter.addFragment(MyProjectsFragment(), "My Projects")
        binding.viewpager.setAdapter(viewPagerAdapter)

    }


    class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        private val fragments: MutableList<Fragment> = ArrayList()
        private val fragmentTitles: MutableList<String> = ArrayList()

        //add fragment to the viewpager
        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            fragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        //to setup title of the tab layout
        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitles[position]
        }
    }


}


