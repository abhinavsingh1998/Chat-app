package com.emproto.hoabl.feature.home.views

import android.content.Context
import android.os.Bundle
import com.emproto.core.BaseActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ActivityProjectDetailExpandBinding
import com.emproto.hoabl.model.ViewItem

class ProjectDetailsExpandView : BaseActivity() {
    private lateinit var listViews: ArrayList<ViewItem>
    lateinit var binding: ActivityProjectDetailExpandBinding
    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailExpandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        initView()
    }

    private fun initView() {
        listViews = ArrayList()
        listViews.add(ViewItem(1, R.drawable.ic_arrow_down))
        listViews.add(ViewItem(2, R.drawable.ic_bookmark))
        listViews.add(ViewItem(3, R.drawable.ic_arrow_left))
        listViews.add(ViewItem(4, R.drawable.ic_bookmark))
        listViews.add(ViewItem(5, R.drawable.ic_arrow_drop))

//        adapter = InvestmentViewPagerAdapter( listViews)
//        binding.imagesViewpager.adapter = adapter
//
//        binding.imagesViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            @SuppressLint("SetTextI18n")
//            override fun onPageScrollStateChanged(state: Int) {
//                binding.imageCount.text = state.toString() + "/" + listViews.size.toString()
//            }
//
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int,
//            ) {
//            }
//
//            override fun onPageSelected(position: Int) {
//            }
//        })
//
//    }
    }
}
