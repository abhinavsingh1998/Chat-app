package com.emproto.hoabl.feature.promises

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPromiseDetailsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.promises.adapter.PromiseDetailsAdapter

import com.emproto.hoabl.feature.promises.data.DetailsScreenData
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import javax.inject.Inject


class PromisesDetailsFragment : Fragment() {

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    lateinit var binding: FragmentPromiseDetailsBinding
    lateinit var adapter: PromiseDetailsAdapter
    lateinit var button: Button

    private lateinit var newArrayList: ArrayList<DetailsScreenData>
    lateinit var imageId: Array<Int>
    lateinit var heading: Array<String>
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentPromiseDetailsBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory).get(HomeViewModel::class.java)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true

        initView()
        initObserver()

        return binding.root


    }

    private fun initView() {
        binding.textViewTAndC.setPaintFlags(binding.textViewTAndC.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
    }

    private fun initObserver() {
        homeViewModel.getSelectedPromise().observe(viewLifecycleOwner, Observer {
            binding.tvPromiseTitle.text = it.name
            binding.tvPromiseInfo.text = it.shortDescription
            binding.tvDescList.layoutManager = LinearLayoutManager(requireContext())
            binding.tvDescList.adapter = PromiseDetailsAdapter(requireContext(), it.description)
            binding.textviewApply1.text = it.howToApply.description
        })
    }

}