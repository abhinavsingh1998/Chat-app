package com.emproto.hoabl.feature.chat.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentChatsDetailBinding
import com.emproto.hoabl.feature.chat.model.*
import com.emproto.hoabl.feature.chat.model.ChatsListModel.ChatsModel
import com.emproto.hoabl.feature.investment.adapters.ChatsDetailAdapter
import com.emproto.networklayer.response.chats.ChatResponse

class ChatsDetailFragment : Fragment() {
    var chatsList: ChatResponse.ChatList? = null
    var chatsDetailModel = ArrayList<ChatsDetailModel>()
    var msgReceived = ArrayList<MessageReceived>()
    var msgOptions = ArrayList<MessageOptions>()


    private lateinit var chatsDetailAdapter: ChatsDetailAdapter

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
                "Hi Anuj Singh! Thanks for connecting. I am Chat Bot. I will be helping you with your queries today."
            )
        )
        msgReceived.add(
            MessageReceived(
                "1",
                "Please choose the relevant option which describes your issue:"
            )
        )

        msgOptions.add(MessageOptions("0", "About Hoabl"))
        msgOptions.add(MessageOptions("1", "Promises"))
        msgOptions.add(MessageOptions("2", "Investments"))
        msgOptions.add(MessageOptions("3", "Others"))


        chatsDetailModel.add(ChatsDetailModel(msgReceived, msgOptions, null, TypeOfMessage.RECEIVER, 1651773904410))

        chatsDetailModel.add(ChatsDetailModel(null, null, "About Hoabl", TypeOfMessage.SENDER, 1651773905410))
    }

    private fun onBackPressed() {

    }


}
