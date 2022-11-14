package com.emproto.hoabl.feature.chat.views.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentChatsDetailBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.model.ChatDetailModel
import com.emproto.hoabl.feature.chat.model.MessageType
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.ChatsDetailAdapter
import com.emproto.hoabl.feature.investment.adapters.OnOptionClickListener
import com.emproto.hoabl.feature.investment.views.FaqDetailFragment
import com.emproto.hoabl.feature.investment.views.ProjectDetailFragment
import com.emproto.hoabl.feature.portfolio.views.BookingJourneyFragment
import com.emproto.hoabl.feature.portfolio.views.ProjectTimelineFragment
import com.emproto.hoabl.feature.profile.fragments.about_us.AboutUsFragment
import com.emproto.hoabl.utils.Extensions.hideKeyboard
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.request.chat.SendMessageBody
import com.emproto.networklayer.response.chats.*
import com.emproto.networklayer.response.enums.Status
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ChatsDetailFragment : Fragment(), OnOptionClickListener {
    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel
    private var chatsList: CData? = null
    private var chatHistoryList: Data? = null
    private var chatDetailList: DData? = null
    private lateinit var chatsDetailAdapter: ChatsDetailAdapter
    private var newChatMessageList = ArrayList<ChatDetailModel>()
    private var c: Calendar? = null
    private var sdf: SimpleDateFormat? = null
    private var time: String? = null
    private var isMessagesEnabled = true
    private var latestConversationId = 0
    private lateinit var handler: Handler
    private var runnable: Runnable? = null

    lateinit var binding: FragmentChatsDetailBinding

    companion object {
        const val MORE_OPTIONS = 1
        const val NAVIGATE = 2
        const val FINAL_MESSAGE = 100
        const val OTHERS = 105
        const val START_TYPING = 108
        const val REDIRECT_ABOUT_HOABL = 109
        const val REDIRECT_INVESTMENTS = 110
        const val REDIRECT_PROJECT = 111
        const val REDIRECT_OTHERS = 112
        const val REDIRECT_PROMISE = 113
        const val REDIRECT_FAQ = 114
        const val REDIRECT_PORTFOLIO = 119
        const val REDIRECT_BOOKING_JOURNEY = 120
        const val REDIRECT_PROJECT_TIMELINE = 121
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatsDetailBinding.inflate(layoutInflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
        (requireActivity() as HomeActivity).hideHeader()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        handler = Handler(Looper.getMainLooper())
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

        chatsList = arguments?.getSerializable(Constants.CHAT_MODEL) as CData
        binding.tvChatTitle.text = chatsList?.name.toString()
        context?.let {
            Glide.with(it)
                .load(chatsList?.booking?.crmLaunchPhase?.projectContent?.projectCoverImages?.chatPageMedia?.value?.url)
                .placeholder(R.drawable.ic_baseline_image_24).into(binding.ivChatDetailThumb)
        }
        binding.ivBack.setOnClickListener {
            hideKeyboard()
            onBackPressed()
        }

        binding.etType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etType.text.toString().trim()

            }

            override fun afterTextChanged(s: Editable?) {
                when {
                    s.toString().trim().isEmpty() -> {
                        binding.ivSend.isClickable = false
                        binding.clSend.background =
                            ContextCompat.getDrawable(context!!, R.drawable.send_button_bg)
                    }
                    s.toString().isNotEmpty() -> {
                        binding.ivSend.isClickable = true
                        binding.clSend.background =
                            ContextCompat.getDrawable(context!!, R.drawable.send_button_blue_bg)
                    }
                }
            }
        })

        callChatHistoryApi()
        binding.clButtonStart.setOnClickListener {
            binding.clButtonStart.visibility = View.GONE
            callChatInitiateApi()
            getDay()
        }
    }


    private fun callChatHistoryApi() {
        homeViewModel.getChatHistory(
            chatsList?.topicId.toString(),
            chatsList?.isInvested!!
        ).observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Status.LOADING -> {
                    binding.loader.show()
                    binding.rvChat.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    binding.rvChat.visibility = View.VISIBLE
                    it.data?.let {
                        if (it.data.messages.isNotEmpty()) {
                            binding.clButtonStart.visibility = View.GONE
                            chatHistoryList = it.data
                            getTime()
                            newChatMessageList.clear()
                            if (it.data.conversation != null) {
                                when (it.data.conversation.isOpen) {
                                    true -> {
                                        isMessagesEnabled = true
                                    }
                                    else -> {
                                        isMessagesEnabled = false
                                    }
                                }
                            } else {
                                binding.clButtonStart.visibility = View.VISIBLE
                            }

                            for (item in it.data.messages) {
                                if (item.message != null) {
                                    when (item.origin) {
                                        3 -> {
                                            newChatMessageList.add(
                                                ChatDetailModel(
                                                    item.message,
                                                    item.options,
                                                    MessageType.RECEIVER, time,
                                                    item.conversationId
                                                )
                                            )

                                        }
                                        2 -> {
                                            newChatMessageList.add(
                                                ChatDetailModel(
                                                    item.message,
                                                    item.options,
                                                    MessageType.RECEIVER, time,
                                                    item.conversationId
                                                )
                                            )

                                        }
                                        1 -> {
                                            newChatMessageList.add(
                                                ChatDetailModel(
                                                    item.message,
                                                    null,
                                                    MessageType.SENDER, time,
                                                    item.conversationId
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            binding.rvChat.smoothScrollToPosition(it.data.messages.size)
                            val messagesList = it.data.messages
                            for (i in messagesList.size - 1 downTo 0) {
                                if (messagesList[i].origin == 2 && messagesList[i].message == resources.getString(
                                        R.string.describe_issue
                                    )
                                ) {
                                    binding.clType.visibility = View.VISIBLE
                                    binding.clButtonStart.visibility = View.INVISIBLE
                                    sendTypedMessage()
                                }
                            }
                            if (messagesList[messagesList.size - 1].message == resources.getString(R.string.thank_you_text) ||
                                messagesList[messagesList.size - 1].message == resources.getString(R.string.request_time_out_text)
                            ) {
                                isMessagesEnabled = false
                            }
                            latestConversationId =
                                messagesList[messagesList.size - 1].conversationId

                            chatsDetailAdapter.notifyDataSetChanged()
                        }

                    }
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        }
    }

    private fun callChatInitiateApi() {
        homeViewModel.chatInitiate(
            chatsList?.topicId.toString(),
            chatsList!!.isInvested
        ).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.loader.show()
                    binding.rvChat.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    binding.rvChat.visibility = View.VISIBLE
                    if (it.data?.data != null) {
                        chatDetailList = it.data!!.data
                        latestConversationId = it?.data!!.data?.conversation!!.id
                        addMessages(it.data!!.data)
                        chatsDetailAdapter.notifyDataSetChanged()
                        binding.rvChat.smoothScrollToPosition(newChatMessageList.size - 1)
                    }
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        }

    }

    private fun getDay() {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        val d = Date()
        val dayOfTheWeek = sdf.format(d)
        binding.tvDay.text = dayOfTheWeek
    }

    private fun addMessages(chatDetailList: DData) {
        getTime()
        newChatMessageList.add(
            ChatDetailModel(
                chatDetailList.autoChat.chatJSON.welcomeMessage,
                null, MessageType.RECEIVER, time,
                latestConversationId
            )
        )
        newChatMessageList.add(
            ChatDetailModel(
                chatDetailList.autoChat.chatJSON.chatBody[0].message,
                chatDetailList.autoChat.chatJSON.chatBody[0].options,
                MessageType.RECEIVER, time,
                latestConversationId
            )
        )
        isMessagesEnabled = true
    }


    private fun onBackPressed() {

    }

    override fun onOptionClick(
        option: Option,
        view: View,
        position: Int,
        conversationId: Int,
        actionType: Int?
    ) {
        if (conversationId == latestConversationId) {
            when (isMessagesEnabled) {
                true -> {
                    newChatMessageList.add(
                        ChatDetailModel(
                            option.text,
                            null,
                            MessageType.SENDER, time
                        )
                    )

                    sendMessage(1, option.text, option.action, actionType, null)
                    when {
                        option.actionType == MORE_OPTIONS -> {
                            when (option.action) {
                                OTHERS -> {
                                    if (chatsList?.isInvested == false) {
                                        val fragment = FaqDetailFragment()
                                        val bundle = Bundle()
                                        bundle.putBoolean(Constants.IS_FROM_INVESTMENT, false)
                                        bundle.putString(Constants.PROJECT_NAME, "")
                                        fragment.arguments = bundle
                                        (requireActivity() as HomeActivity).addFragment(
                                            fragment,
                                            true
                                        )
                                    } else {
                                        addingOptions(option)
                                    }
                                }
                                else -> addingOptions(option)
                            }
                        }
                        option.actionType == NAVIGATE -> {
                            when (option.action) {
                                REDIRECT_ABOUT_HOABL -> {
                                    (requireActivity() as HomeActivity).addFragment(
                                        AboutUsFragment(),
                                        true
                                    )
                                }
                                REDIRECT_INVESTMENTS -> {
                                    (requireActivity() as HomeActivity).navigate(R.id.navigation_investment)
                                }
                                REDIRECT_PROJECT -> {
                                    chatsList?.let {
                                        val bundle = Bundle()
                                        bundle.putString(Constants.PROJECT_ID, it.topicId)
                                        val fragment = ProjectDetailFragment()
                                        fragment.arguments = bundle
                                        (requireActivity() as HomeActivity).addFragment(
                                            fragment, true
                                        )
                                    }
                                }
                                REDIRECT_OTHERS -> {
                                    val fragment = FaqDetailFragment()
                                    val bundle = Bundle()
                                    bundle.putBoolean(Constants.IS_FROM_INVESTMENT, false)
                                    bundle.putString(Constants.PROJECT_NAME, "")
                                    fragment.arguments = bundle
                                    (requireActivity() as HomeActivity).addFragment(
                                        fragment,
                                        true
                                    )
                                }
                                REDIRECT_PROMISE -> {
                                    (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)
                                }
                                REDIRECT_PORTFOLIO -> {
                                    (requireActivity() as HomeActivity).navigate(R.id.navigation_portfolio)
                                }
                                REDIRECT_BOOKING_JOURNEY -> {
                                    chatsList?.let {
                                        (requireActivity() as HomeActivity).addFragment(
                                            BookingJourneyFragment.newInstance(
                                                it.booking.id,
                                                ""
                                            ), true
                                        )
                                    }
                                }
                                REDIRECT_PROJECT_TIMELINE -> {
                                    chatsList?.let {
                                        (requireActivity() as HomeActivity).addFragment(
                                            ProjectTimelineFragment.newInstance(
                                                it.topicId.toInt(),
                                                ""
                                            ), true
                                        )
                                    }
                                }
                                REDIRECT_FAQ -> {
                                    chatsList?.let {
                                        val fragment = FaqDetailFragment()
                                        val bundle = Bundle()
                                        bundle.putString(Constants.PROJECT_ID, it.topicId)
                                        bundle.putBoolean(Constants.IS_FROM_INVESTMENT, true)
                                        bundle.putString(Constants.PROJECT_NAME, it.name)
                                        fragment.arguments = bundle
                                        (requireActivity() as HomeActivity).addFragment(
                                            fragment,
                                            true
                                        )
                                    }
                                }
                            }
                        }
                        option.action == START_TYPING -> {
                            binding.clType.visibility = View.VISIBLE
                            binding.clButtonStart.visibility = View.INVISIBLE
                            isMessagesEnabled = false
                            getTime()
                            when {
                                chatDetailList != null -> {
                                    newChatMessageList.add(
                                        ChatDetailModel(
                                            chatDetailList!!.autoChat.chatJSON.allowTypingMessage,
                                            null, MessageType.RECEIVER, time,
                                            latestConversationId
                                        )
                                    )
                                    isMessagesEnabled = false
                                    runnable = Runnable {
                                        sendMessage(
                                            2,
                                            chatDetailList!!.autoChat.chatJSON.allowTypingMessage,
                                            null,
                                            null,
                                            null
                                        )
                                    }
                                    runnable?.let { it1 -> handler.postDelayed(it1, 2000) }
                                }
                                else -> {
                                    newChatMessageList.add(
                                        ChatDetailModel(
                                            chatHistoryList!!.autoChat.chatJSON.allowTypingMessage,
                                            null, MessageType.RECEIVER, time,
                                            latestConversationId
                                        )
                                    )
                                    isMessagesEnabled = false
                                    runnable = Runnable {
                                        sendMessage(
                                            2,
                                            chatHistoryList!!.autoChat.chatJSON.allowTypingMessage,
                                            null,
                                            null,
                                            null
                                        )
                                    }
                                    runnable?.let { it1 -> handler.postDelayed(it1, 2000) }


                                }
                            }
                            binding.rvChat.smoothScrollToPosition(newChatMessageList.size - 1)
                            sendTypedMessage()

                        }
                        option.action == FINAL_MESSAGE -> {
                            binding.clType.visibility = View.GONE
                            getTime()
                            when {
                                chatDetailList != null -> {
                                    newChatMessageList.add(
                                        ChatDetailModel(
                                            chatDetailList!!.autoChat.chatJSON.finalMessage,
                                            null, MessageType.RECEIVER, time,
                                            latestConversationId
                                        )
                                    )
                                    isMessagesEnabled = false
                                    runnable = Runnable {
                                        sendMessage(
                                            2,
                                            chatDetailList!!.autoChat.chatJSON.finalMessage,
                                            null,
                                            null,
                                            null
                                        )
                                    }
                                    runnable?.let { it1 -> handler.postDelayed(it1, 2000) }
                                }
                                else -> {
                                    newChatMessageList.add(
                                        ChatDetailModel(
                                            chatHistoryList!!.autoChat.chatJSON.finalMessage,
                                            null, MessageType.RECEIVER, time,
                                            latestConversationId
                                        )
                                    )
                                    isMessagesEnabled = false
                                    runnable = Runnable {
                                        sendMessage(
                                            2,
                                            chatHistoryList!!.autoChat.chatJSON.finalMessage,
                                            null,
                                            null,
                                            null
                                        )
                                    }
                                    runnable?.let { it1 -> handler.postDelayed(it1, 2000) }
                                }
                            }

                            chatsDetailAdapter.notifyDataSetChanged()
                            binding.rvChat.smoothScrollToPosition(newChatMessageList.size - 1)

                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun addingOptions(option: Option) {
        when {
            chatDetailList != null -> {
                for (i in chatDetailList!!.autoChat.chatJSON.chatBody.indices) {
                    if (option.optionNumber == chatDetailList!!.autoChat.chatJSON.chatBody[i].linkedOption) {
                        getTime()
                        newChatMessageList.add(
                            ChatDetailModel(
                                chatDetailList!!.autoChat.chatJSON.chatBody[i].message,
                                chatDetailList!!.autoChat.chatJSON.chatBody[i].options,
                                MessageType.RECEIVER, time,
                                latestConversationId
                            )
                        )
                        runnable = Runnable {
                            sendMessage(
                                2,
                                chatDetailList!!.autoChat.chatJSON.chatBody[i].message,
                                null,
                                chatDetailList!!.autoChat.chatJSON.chatBody[i].options?.get(i)?.actionType,
                                chatDetailList!!.autoChat.chatJSON.chatBody[i].options
                            )
                        }
                        runnable?.let { it1 -> handler.postDelayed(it1, 2000) }
                        chatsDetailAdapter.notifyDataSetChanged()
                        binding.rvChat.smoothScrollToPosition(newChatMessageList.size - 1)
                        break
                    }
                }
            }
            else -> {
                for (i in chatHistoryList!!.autoChat.chatJSON.chatBody.indices) {
                    if (option.optionNumber == chatHistoryList!!.autoChat.chatJSON.chatBody[i].linkedOption) {
                        getTime()
                        newChatMessageList.add(
                            ChatDetailModel(
                                chatHistoryList!!.autoChat.chatJSON.chatBody[i].message,
                                chatHistoryList!!.autoChat.chatJSON.chatBody[i].options,
                                MessageType.RECEIVER, time,
                                latestConversationId
                            )
                        )

                        runnable = Runnable {
                            sendMessage(
                                2,
                                chatHistoryList!!.autoChat!!.chatJSON!!.chatBody[i]!!.message,
                                null,
                                null,
                                chatHistoryList!!.autoChat!!.chatJSON!!.chatBody[i]!!.options
                            )
                        }
                        runnable?.let { it1 -> handler.postDelayed(it1, 2000) }
                        chatsDetailAdapter.run { notifyDataSetChanged() }
                        binding.rvChat.smoothScrollToPosition(newChatMessageList.size - 1)
                        break
                    }
                }
            }
        }

    }

    private fun sendTypedMessage() {
        binding.ivSend.setOnClickListener {
            if (binding.etType.text.isNotEmpty()) {
                getTime()
                newChatMessageList.add(
                    ChatDetailModel(
                        binding.etType.text.toString(),
                        null,
                        MessageType.SENDER, time,
                        latestConversationId
                    )
                )
                sendMessage(1, binding.etType.text.toString(), null, null, null)
                chatsDetailAdapter.notifyDataSetChanged()
                binding.rvChat.smoothScrollToPosition(newChatMessageList.size - 1)
                binding.etType.text.clear()
            } else {
                Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun sendMessage(
        origin: Int,
        text: String?,
        selection: Int?,
        actionType: Int?,
        options: ArrayList<Option>?
    ) {
        when {
            chatDetailList != null -> {
                homeViewModel.sendMessage(
                    SendMessageBody(
                        conversationId = chatDetailList!!.conversation!!.id,
                        origin = origin,
                        message = text.toString(),
                        selection = selection,
                        actionType = actionType,
                        options = options
                    )
                ).observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            it.data?.let {

                            }
                        }
                        Status.ERROR -> {
                            (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                        }
                    }
                }
            }
            else -> {
                homeViewModel.sendMessage(
                    SendMessageBody(
                        conversationId = chatHistoryList?.conversation!!.id,
                        origin = origin,
                        message = text.toString(),
                        selection = selection,
                        actionType = actionType,
                        options = options
                    )
                ).observe(this) {
                    when (it.status) {
                        Status.LOADING -> {

                        }
                        Status.SUCCESS -> {
                            it.data?.let {

                            }
                        }
                        Status.ERROR -> {
                            (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                        }
                    }
                }
            }
        }

    }

    private fun getTime() {
        c = Calendar.getInstance()
        sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        time = sdf!!.format(c!!.time)
    }

    override fun onDestroy() {
        super.onDestroy()
        runnable?.let { handler.removeCallbacks(it) }
    }

}
