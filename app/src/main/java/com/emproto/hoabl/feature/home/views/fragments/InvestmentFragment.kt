package com.emproto.hoabl.feature.home.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentInvestmentBinding
import com.emproto.hoabl.feature.home.views.ProjectDetailsExpandView
import com.emproto.hoabl.feature.investment.adapters.InvestmentAdapter
import com.emproto.hoabl.model.ViewItem
import com.emproto.networklayer.response.investment.SimilarInvestment

class InvestMentFragment : BaseFragment() {
    private lateinit var listViews: ArrayList<ViewItem>

    // private lateinit var adapter: ImageviewPagerAdapter
    private lateinit var investmentAdapter: InvestmentAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var currentIndex: Int = 0
    lateinit var binding: FragmentInvestmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInvestmentBinding.inflate(layoutInflater)
        initUI()
        initListener()
        return binding.root
    }

    private fun initUI() {
        listViews = ArrayList()
        listViews.add(ViewItem(1, R.drawable.ic_arrow_down))
        listViews.add(ViewItem(2, R.drawable.ic_bookmark))
        listViews.add(ViewItem(3, R.drawable.ic_arrow_left))
        listViews.add(ViewItem(4, R.drawable.ic_bookmark))
        listViews.add(ViewItem(5, R.drawable.ic_arrow_drop))

        /* adapter = ImageviewPagerAdapter(requireActivity(), listViews)
         binding.viewPager.adapter = adapter
         addPageIndicators()
         binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
             override fun onPageScrollStateChanged(state: Int) {

             }

             override fun onPageScrolled(
                 position: Int,
                 positionOffset: Float,
                 positionOffsetPixels: Int,
             ) {

             }

             override fun onPageSelected(position: Int) {
                 updatePageIndicator(position)
             }
         })*/

        val list: ArrayList<SimilarInvestment> = ArrayList()

//        investmentAdapter = InvestmentAdapter(requireActivity(), list, itemClickListener)
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        binding.smartdealsRecyclerview.layoutManager = linearLayoutManager
//        binding.smartdealsRecyclerview.adapter = investmentAdapter


    }


    private fun initListener() {
        binding.imageLayout.setOnClickListener {
            startActivity(Intent(activity, ProjectDetailsExpandView::class.java))
        }
    }

//    private fun addPageIndicators() {
//        binding.slider.removeAllViews()
//        for (i in listViews.indices) {
//            val view = ImageView(requireActivity())
//            view.setImageResource(R.drawable.unselected_circle)
//
//            binding.slider.addView(view)
//        }
//        updatePageIndicator(currentIndex)
//    }
//
//    fun updatePageIndicator(position: Int) {
//        var imageView: ImageView
//        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT)
//        lp.setMargins(5, 0, 5, 0)
//        for (i in 0 until binding.slider.childCount) {
//            imageView = binding.slider.getChildAt(i) as ImageView
//            imageView.layoutParams = lp
//            if (position == i) {
//                imageView.setImageResource(R.drawable.selected_circle)
//            } else {
//                imageView.setImageResource(R.drawable.unselected_circle)
//            }
//        }
//    }
}