package com.emproto.hoabl.feature.chat.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentChatsDetailBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.model.Action
import com.emproto.hoabl.feature.chat.model.ActionType
import com.emproto.hoabl.feature.chat.model.ChatDetailModel
import com.emproto.hoabl.feature.chat.model.MessageType
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.ChatsDetailAdapter
import com.emproto.hoabl.feature.investment.adapters.OnOptionClickListener
import com.emproto.hoabl.feature.profile.AboutUsFragment
import com.emproto.hoabl.fragments.PromisesFragment
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.chats.ChatInitiateRequest
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.networklayer.response.chats.Option
import com.emproto.networklayer.response.enums.Status
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ChatsDetailFragment : Fragment(), OnOptionClickListener {
    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel
    var chatsList: ChatResponse.ChatList? = null
    var chatDetailList: ChatDetailResponse.ChatDetailList? = null
    lateinit var chatsDetailAdapter: ChatsDetailAdapter
    var newChatMessageList = ArrayList<ChatDetailModel>()
    private var c: Calendar? = null
    private var sdf: SimpleDateFormat? = null
    private var time: String? = null


    lateinit var binding: FragmentChatsDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i("onCreate", "onCreate")

        // Inflate the layout for this fragment
        binding = FragmentChatsDetailBinding.inflate(layoutInflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.GONE
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("onViewCreated", "onViewCreated")
        if (newChatMessageList.isNotEmpty()) {
            binding.clButtonStart.visibility = View.GONE
        }
        binding.clButtonStart.setOnClickListener {
            binding.clButtonStart.visibility = View.GONE
            binding.tvDay.visibility = View.VISIBLE
            getData()
            getDay()


        }
        binding.rvChat.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        chatsDetailAdapter = ChatsDetailAdapter(context, newChatMessageList, this)
        binding.rvChat.adapter = chatsDetailAdapter



        chatsList = arguments?.getSerializable("chatModel") as? ChatResponse.ChatList
        binding.tvTitle.text = chatsList?.project?.launchName
        context?.let {
            Glide.with(it)
                .load(chatsList?.project?.projectCoverImages?.chatListViewPageMedia?.value?.url)
                .placeholder(R.drawable.ic_baseline_image_24).into(binding.ivThumb)
        }
        binding.ivBack.setOnClickListener { onBackPressed() }
    }

    private fun getDay() {
        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        val dayOfTheWeek = sdf.format(d)
        binding.tvDay.text = dayOfTheWeek
    }


    private fun getData() {
        homeViewModel.chatInitiate(ChatInitiateRequest(true)).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.loader.show()
                    binding.rvChat.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    binding.rvChat.visibility = View.VISIBLE
                    if (it.data?.chatDetailList != null) {
                        chatDetailList = it.data!!.chatDetailList
                        addMessages(it.data!!.chatDetailList)
                        Log.i("newChat", newChatMessageList.toString())
                        chatsDetailAdapter.notifyDataSetChanged()

                    }
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        })

    }


    private fun addMessages(chatDetailList: ChatDetailResponse.ChatDetailList) {
        getTime()
        newChatMessageList.add(
            ChatDetailModel(
                chatDetailList.autoChat.chatJSON.welcomeMessage,
                null, MessageType.RECEIVER, time
            )
        )
        newChatMessageList.add(
            ChatDetailModel(
                chatDetailList.autoChat.chatJSON.chatBody[0].message,
                chatDetailList.autoChat.chatJSON.chatBody[0].options,
                MessageType.RECEIVER, time
            )
        )

    }


    private fun onBackPressed() {

    }

    override fun onOptionClick(option: Option, view: View, position: Int) {
//        Toast.makeText(context, "$option isClicked", Toast.LENGTH_SHORT).show()
        newChatMessageList.add(
            ChatDetailModel(
                option.text,
                null,
                MessageType.SENDER, time
            )
        )

        if (option.actionType == ActionType.MORE_OPTIONS.name) {
            for (i in chatDetailList!!.autoChat.chatJSON.chatBody.indices) {

                if (option.optionNumber == chatDetailList!!.autoChat.chatJSON.chatBody[i].linkedOption) {
                    getTime()
                    newChatMessageList.add(
                        ChatDetailModel(
                            chatDetailList!!.autoChat.chatJSON.chatBody[i].message,
                            chatDetailList!!.autoChat.chatJSON.chatBody[i].options,
                            MessageType.RECEIVER, time
                        )
                    )


                    chatsDetailAdapter.notifyDataSetChanged()
                    break
                }
            }

        } else if (option.actionType == ActionType.NAVIGATE.name) {
            when (option.action) {

                Action.NAVIGATE_ABOUT_HOABL.name -> {
                    (requireActivity() as HomeActivity).addFragment(
                        AboutUsFragment(),
                        false
                    )
                }
                Action.NAVIGATE_PROMISES.name -> {
                    (requireActivity() as HomeActivity).addFragment(
                        PromisesFragment(),
                        false
                    )
                }
            }
        } else if (option.actionType == ActionType.ALLOW_TYPING.name) {
            binding.clType.visibility = View.VISIBLE
            binding.ivSend.setOnClickListener {
                if (binding.etType.text.isNotEmpty()) {
                    getTime()
                    newChatMessageList.add(
                        ChatDetailModel(
                            binding.etType.text.toString(),
                            null,
                            MessageType.SENDER, time
                        )
                    )
                    newChatMessageList.add(
                        ChatDetailModel(
                            chatDetailList!!.autoChat.chatJSON.finalMessage,
                            null, MessageType.RECEIVER, time
                        )
                    )
                    chatsDetailAdapter.notifyDataSetChanged()
                    binding.etType.text.clear();


                } else {
                    Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }


        } else if (option.actionType == ActionType.NOT_ALLOWED_TYPING.name) {
            binding.clType.visibility = View.GONE
            getTime()
            newChatMessageList.add(
                ChatDetailModel(
                    chatDetailList!!.autoChat.chatJSON.finalMessage,
                    null, MessageType.RECEIVER, time
                )
            )
            chatsDetailAdapter.notifyDataSetChanged()

        }
    }

    private fun getTime() {
        c = Calendar.getInstance()
        sdf = SimpleDateFormat("h:mm a")
        time = sdf!!.format(c!!.time)

    }


}
