package com.emproto.hoabl.feature.chat.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentChatsBinding
import com.emproto.hoabl.databinding.FragmentHomeBinding
import com.emproto.hoabl.feature.chat.adapter.ChatsAdapter
import com.emproto.hoabl.model.ChatsModel

class ChatsFragment : Fragment() {
    lateinit var binding: FragmentChatsBinding
    var chatsModel = ArrayList<ChatsModel>()

    private lateinit var chatsAdapter: ChatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatsBinding.inflate(layoutInflater)
        return binding.root

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChatsData()
        setRecycler()

    }

    private fun setChatsData() {
        for (i in 1..20) {
            chatsModel.add(
                ChatsModel(
                    R.drawable.img,
                    "HoABL Customer Chat Support",
                    "Welcome to HoABL. We are excited to.. ",
                    "1h"
                )
            )
        }
    }

    private fun setRecycler() {
        chatsAdapter = ChatsAdapter(context, chatsModel)

        binding.rvChats?.apply {
            adapter = chatsAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

}