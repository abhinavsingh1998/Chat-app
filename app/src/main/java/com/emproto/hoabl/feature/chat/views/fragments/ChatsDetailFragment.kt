package com.emproto.hoabl.feature.chat.views.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentChatsBinding
import com.emproto.hoabl.databinding.FragmentChatsDetailBinding
import com.emproto.hoabl.feature.home.views.fragments.HomeFragment
import com.emproto.hoabl.model.ChatsModel

class ChatsDetailFragment : Fragment() {
    var chatsModel: ChatsModel? = null
    lateinit var binding: FragmentChatsDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatsDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatsModel = arguments?.getSerializable("chatModel") as? ChatsModel
        binding.tvTitle.text = chatsModel!!.topic
        binding.ivThumb.setImageResource(chatsModel!!.image)
        binding.ivBack.setOnClickListener { onBackPressed() }

    }

    private fun onBackPressed() {

    }


}
