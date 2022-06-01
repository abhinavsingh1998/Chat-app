package com.emproto.hoabl.feature.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentAboutUsBinding
import com.emproto.hoabl.feature.profile.adapter.AboutUSAdapter
import com.emproto.hoabl.model.RecyclerViewItem



class AboutUsFragment : Fragment() {
    lateinit var binding: FragmentAboutUsBinding

    lateinit var adapter: AboutUSAdapter
    lateinit var ivarrow:ImageView

    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentAboutUsBinding.inflate(inflater,container,false)
        (requireActivity() as HomeActivity).hideHeader()
        (requireActivity() as HomeActivity).hideBottomNavigation()

        initClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fun initData(): ArrayList<RecyclerViewItem> {
            val dataList: ArrayList<RecyclerViewItem> = ArrayList<RecyclerViewItem>()
            dataList.add(RecyclerViewItem(AboutUSAdapter.VIEW_CORPORATE_PHILOSOPHY))
            dataList.add(RecyclerViewItem(AboutUSAdapter.VIEW_LIFESTYLE))
            dataList.add(RecyclerViewItem(AboutUSAdapter.VIEW_PRODUCT_CATEGORY))

            return dataList
        }
        val adapter = AboutUSAdapter(this.requireContext(), initData())
        binding.aboutUsRv.adapter = adapter
    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
              requireActivity().supportFragmentManager.popBackStack()
            }
        })

    }


}



