package com.emproto.hoabl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.adapters.MyProjectsAdapter
import com.emproto.hoabl.databinding.FragmentMyProjectsBinding

class MyProjectsFragment : BaseFragment() {

    lateinit var fragmentMyProjectsBinding: FragmentMyProjectsBinding
    lateinit var myProjectsAdapter: MyProjectsAdapter
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {

        fragmentMyProjectsBinding = FragmentMyProjectsBinding.inflate(layoutInflater)

        initView()
        return fragmentMyProjectsBinding.root
    }

    private fun initView() {
        val list: ArrayList<String> = arrayListOf()
        list.add(Constants.AMOUNT)
        list.add(Constants.AMOUNT)
        list.add(Constants.AMOUNT)
        list.add(Constants.AMOUNT)

        myProjectsAdapter = MyProjectsAdapter(requireActivity(), list)
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        fragmentMyProjectsBinding.ongoingRecyclerview.layoutManager = linearLayoutManager
        fragmentMyProjectsBinding.ongoingRecyclerview.adapter = myProjectsAdapter

        fragmentMyProjectsBinding.ongoingAll.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

            }
        })

    }

}
