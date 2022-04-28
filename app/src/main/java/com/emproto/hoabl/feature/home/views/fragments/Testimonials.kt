package com.emproto.hoabl.feature.home.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentTestimonialsBinding


class Testimonials : BaseFragment() {


    private lateinit var binding: FragmentTestimonialsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTestimonialsBinding.inflate(layoutInflater)

        return binding.root
    }

}