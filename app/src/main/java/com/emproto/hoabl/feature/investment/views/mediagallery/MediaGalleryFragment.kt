package com.emproto.hoabl.feature.investment.views.mediagallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.databinding.FragmentMediaGalleryBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.MediaViewPagerAdapter
import com.emproto.hoabl.model.MediaViewItem
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class MediaGalleryFragment : BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding: FragmentMediaGalleryBinding
    private lateinit var mediaViewPagerAdapter: MediaViewPagerAdapter
    val list = ArrayList<MediaViewItem>()

    private val tabList = arrayListOf(
        Constants.PHOTOS,
        Constants.VIDEOS,
        Constants.DRONE_SHOOTS,
        Constants.PHOTOS_360
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaGalleryBinding.inflate(layoutInflater)
        list.clear()
        arguments?.let {
            list.addAll(it.getSerializable(Constants.DATA) as List<MediaViewItem>)
//            investmentViewModel.isVideoSeeAllClicked = it.getBoolean("isVideoSeeAllClicked")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpTabLayoutViewPager()
    }

    private fun initViewModel() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory)[InvestmentViewModel::class.java]
        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).hideBottomNavigation()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpTabLayoutViewPager() {
        investmentViewModel.setMediaContent(list)
        mediaViewPagerAdapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)
        binding.vpMediaGallery.adapter = mediaViewPagerAdapter
        TabLayoutMediator(binding.tlMediaGallery, binding.vpMediaGallery) { tab, position ->
            tab.text = tabList[position]
        }.attach()

        when (investmentViewModel.isVideoSeeAllClicked) {
            true -> {
                binding.vpMediaGallery.setCurrentItem(1, false)
                mediaViewPagerAdapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)
                mediaViewPagerAdapter.notifyDataSetChanged()
                investmentViewModel.isVideoSeeAllClicked = false
            }
            else -> {}
        }
    }
}