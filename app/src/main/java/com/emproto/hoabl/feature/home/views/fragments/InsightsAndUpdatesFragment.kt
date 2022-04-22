package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.emproto.core.BaseFragment

import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentInsightsUpdatesBinding
import com.emproto.hoabl.feature.home.views.HomeActivity

class InsightsAndUpdatesFragment : BaseFragment() {

    lateinit var fragmentInsightsUpdatesBinding: FragmentInsightsUpdatesBinding

    companion object{
        fun newInstance(): InsightsAndUpdatesFragment {
            val fragment = InsightsAndUpdatesFragment()
            /*val bundle = Bundle()
            fragment.arguments = bundle*/
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentInsightsUpdatesBinding= FragmentInsightsUpdatesBinding.inflate(layoutInflater)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility=View.GONE
        initView()
        initClickListener()
        return fragmentInsightsUpdatesBinding.root
    }

    private fun initView() {
        fragmentInsightsUpdatesBinding.tabLayout.setupWithViewPager(fragmentInsightsUpdatesBinding.viewpager)
        val pagerAdapter = ViewPagerAdapter(childFragmentManager, 0)

        pagerAdapter.addFragment(LatestUpdatesFragment(), getString(R.string.latest_updates))
        pagerAdapter.addFragment(InsightsFragment(), getString(R.string.insights))

        fragmentInsightsUpdatesBinding.viewpager.setAdapter(pagerAdapter)
    }

    private fun initClickListener() {

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