package com.emproto.hoabl.feature.chat.views.fragments

import android.os.Bundle
import android.util.Log
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
import com.emproto.hoabl.feature.chat.adapter.ChatsAdapter
import com.emproto.hoabl.feature.chat.model.*
import com.emproto.hoabl.feature.chat.model.ChatsListModel.ChatsModel
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.ChatsDetailAdapter
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.networklayer.response.enums.Status
import javax.inject.Inject

class ChatsDetailFragment : Fragment() {
    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel
    var chatsList: ChatResponse.ChatList? = null
    var chatsDetailModel = ArrayList<ChatsDetailModel>()
    var msgReceived = ArrayList<MessageReceived>()
    var msgOptions = ArrayList<MessageOptions>()

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
            binding.tvChatTime.visibility=View.VISIBLE
            binding.rvChat.visibility = View.VISIBLE
        }

        homeViewModel.chatInitiate().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.loader.show()
                    binding.rvChat.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    binding.rvChat.visibility = View.VISIBLE
//                    if (it.data?.chatList != null && it.data!!.chatList is List<ChatResponse.ChatList>) {
//                        Log.i("LastMsg",it.data!!.chatList.toString())
//                        binding.rvChats.layoutManager =
//                            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
//                        binding.rvChats.adapter = ChatsAdapter(context, it.data!!.chatList, this)
//                        Log.i("LastMsg",it.data!!.chatList.toString())
//                    }
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
        setChatsData()


    }

    private fun setChatsData() {
        msgReceived.add(
            MessageReceived(
                "0",
                "Please choose the relevant option which describes your issue:"            )
        )

        msgOptions.add(MessageOptions("0", "About Hoabl"))
        msgOptions.add(MessageOptions("1", "Promises"))
        msgOptions.add(MessageOptions("2", "Investments"))
        msgOptions.add(MessageOptions("3", "Others"))


        chatsDetailModel.add(
            ChatsDetailModel(
                msgReceived,
                msgOptions,
                null,
                TypeOfMessage.RECEIVER,
                1651773904410
            )
        )

        chatsDetailModel.add(
            ChatsDetailModel(
                null,
                null,
                "About Hoabl",
                TypeOfMessage.SENDER,
                1651773905410
            )
        )
    }

    private fun onBackPressed() {

    }


}
