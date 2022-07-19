package com.emproto.hoabl.feature.chat.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.emproto.hoabl.feature.profile.fragments.about_us.AboutUsFragment
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.request.chat.SendMessageBody
import com.emproto.networklayer.response.chats.*
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
        if (newChatMessageList.isNotEmpty()) {
            binding.clButtonStart.visibility = View.GONE
        }
        binding.viewBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.rvChat.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        chatsDetailAdapter = ChatsDetailAdapter(context, newChatMessageList, this)
        binding.rvChat.adapter = chatsDetailAdapter

        chatsList = arguments?.getSerializable("chatModel") as ChatResponse.ChatList
        binding.tvChatTitle.text = chatsList?.project?.launchName
        context?.let {
            Glide.with(it)
                .load(chatsList?.project?.projectCoverImages?.chatListViewPageMedia?.value?.url)
                .placeholder(R.drawable.ic_baseline_image_24).into(binding.ivChatThumb)
        }
        binding.ivBack.setOnClickListener { onBackPressed() }

        binding.etType.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                when{

                }
            }

        })

        callChatHistoryApi()

        binding.clButtonStart.setOnClickListener {
            binding.clButtonStart.visibility = View.GONE
            binding.tvDay.visibility = View.VISIBLE
            callChatInitiateApi()
            getDay()
        }
    }

    private fun callChatHistoryApi() {
        homeViewModel.getChatHistory(chatsList?.project?.crmLaunchPhase?.crmId.toString(),
            chatsList?.isInvested!!
        ).observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.LOADING -> {
                    binding.loader.show()
                    binding.rvChat.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    binding.rvChat.visibility = View.VISIBLE
                    it.data?.let {
                        Log.d("history",it.data.toString())
                        if(it.data.messages.isNotEmpty()){
                            binding.clButtonStart.visibility = View.GONE
                            binding.tvDay.visibility = View.VISIBLE
                            callChatInitiateForHistory(it.data)

                        }

                    }
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        })
    }

    private fun callChatInitiateApi() {
        homeViewModel.chatInitiate(ChatInitiateRequest(chatsList?.isInvested,chatsList?.project?.crmLaunchPhase?.crmId.toString())).observe(viewLifecycleOwner, Observer {
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
                        binding.rvChat.smoothScrollToPosition(newChatMessageList.size-1)
                    }
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        })

    }

    private fun callChatInitiateForHistory(data: Data) {
        homeViewModel.chatInitiate(ChatInitiateRequest(chatsList?.isInvested,chatsList?.project?.crmLaunchPhase?.crmId.toString())).observe(viewLifecycleOwner, Observer {
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
                        for(item in data.messages){
                            when(item.origin){
                                "cms" -> {
                                    if(item.message == "Please choose whats up"){
                                        chatDetailList?.let { chatDetail ->
                                            newChatMessageList.add(
                                                ChatDetailModel(
                                                    chatDetail.autoChat.chatJSON.chatBody[0].message,
                                                    chatDetail.autoChat.chatJSON.chatBody[0].options,
                                                    MessageType.RECEIVER, time
                                                )
                                            )
                                            chatsDetailAdapter.notifyDataSetChanged()
                                        }
                                    }
                                }
                                "user" -> {
                                    newChatMessageList.add(
                                        ChatDetailModel(
                                            item.message,
                                            null,
                                            MessageType.SENDER, time
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        })

    }

    private fun getDay() {
        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        val dayOfTheWeek = sdf.format(d)
        binding.tvDay.text = dayOfTheWeek
    }


    private fun getData() {

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

        sendMessage(option.text,"customer")

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

//                    sendMessage(chatDetailList!!.autoChat.chatJSON.chatBody[i].message,"cms")

                    chatsDetailAdapter.notifyDataSetChanged()
                    binding.rvChat.smoothScrollToPosition(newChatMessageList.size-1)
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
                    (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)
                }
            }
        } else if (option.actionType == ActionType.ALLOW_TYPING.name) {
            binding.clType.visibility = View.VISIBLE
            binding.rvChat.smoothScrollToPosition(newChatMessageList.size-1)
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
//                    sendMessage(binding.etType.text.toString(),"customer")
                    newChatMessageList.add(
                        ChatDetailModel(
                            chatDetailList!!.autoChat.chatJSON.finalMessage,
                            null, MessageType.RECEIVER, time
                        )
                    )
//                    sendMessage(chatDetailList!!.autoChat.chatJSON.finalMessage,"cms")
                    chatsDetailAdapter.notifyDataSetChanged()
                    binding.rvChat.smoothScrollToPosition(newChatMessageList.size-1)
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
//            sendMessage(chatDetailList!!.autoChat.chatJSON.finalMessage,"cms")
            chatsDetailAdapter.notifyDataSetChanged()
            binding.rvChat.smoothScrollToPosition(newChatMessageList.size-1)

        }
    }

    private fun sendMessage(text: String?,origin:String) {
        homeViewModel.sendMessage(SendMessageBody(
            conversationId = chatDetailList?.conversation?.id.toString(),
            message =  text.toString(),
            projectId = chatDetailList?.conversation?.projectId.toString(),
            smartKey= chatDetailList?.conversation?.smartKey.toString(),
            origin = origin
        )).observe(viewLifecycleOwner,Observer{
            when(it.status){
                Status.LOADING -> {
                    binding.loader.show()
                    binding.rvChat.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    binding.rvChat.visibility = View.VISIBLE
                    it.data?.let {
                        Log.d("SendMessage",it.data.toString())
                    }
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        })
    }

    private fun getTime() {
        c = Calendar.getInstance()
        sdf = SimpleDateFormat("h:mm a")
        time = sdf!!.format(c!!.time)

    }


}
