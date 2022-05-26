package com.emproto.hoabl.feature.chat.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentChatsDetailBinding
import com.emproto.hoabl.feature.chat.model.*
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.ChatsDetailAdapter
import com.emproto.hoabl.feature.investment.adapters.OnOptionClickListener
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.networklayer.response.chats.Option
import com.emproto.networklayer.response.enums.Status
import javax.inject.Inject

class ChatsDetailFragment : Fragment() ,OnOptionClickListener {
    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var chatMessage1: ChatDetailModel
    lateinit var chatMessage2: ChatDetailModel
    var chatsList: ChatResponse.ChatList? = null
    var chatDetailList: ChatDetailResponse.ChatDetailList? = null

    var newChatMessage1List = ArrayList<ChatDetailModel>()
    var newChatMessage2List = ArrayList<ChatDetailModel>()


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
        binding.clButtonStart.setOnClickListener {
            binding.clButtonStart.visibility = View.GONE
            binding.tvWelcomeMsg.visibility = View.VISIBLE
            binding.tvDay.visibility = View.VISIBLE
            binding.tvChatTime.visibility = View.VISIBLE
            binding.rvChat.visibility = View.VISIBLE
        }
        binding.rvChat.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        binding.rvChat.adapter =
            ChatsDetailAdapter(context,newChatMessage1List, this)

        homeViewModel.chatInitiate().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.loader.show()
                    binding.rvChat.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    binding.rvChat.visibility = View.VISIBLE
                    if (it.data?.chatDetailList != null ) {
                        chatDetailList=it.data!!.chatDetailList
                        addMessages(it.data!!.chatDetailList)
                    }
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        })


        chatsList = arguments?.getSerializable("chatModel") as? ChatResponse.ChatList
        binding.tvTitle.text = chatsList?.project?.launchName
        context?.let {
            Glide.with(it)
                .load(chatsList?.project?.projectCoverImages?.chatListViewPageMedia?.value?.url)
                .placeholder(R.drawable.ic_baseline_image_24).into(binding.ivThumb)
        }
        binding.ivBack.setOnClickListener { onBackPressed() }
    }


    private fun addMessages(chatDetailList: ChatDetailResponse.ChatDetailList) {
        newChatMessage1List.add(ChatDetailModel(chatDetailList.autoChat.welcomeMessage, null))
        newChatMessage1List.add(ChatDetailModel(chatDetailList.autoChat.chatJSON.chatBody[0].message, chatDetailList.autoChat.chatJSON.chatBody[0].options))

    }


    private fun onBackPressed() {

    }

    override fun onOptionClick(option: Option, view: View, position: Int) {

        if(option.actionType== ActionType.MORE_OPTIONS.name){

        }
    }


}
