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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentChatsDetailBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.model.ActionType
import com.emproto.hoabl.feature.chat.model.ChatDetailModel
import com.emproto.hoabl.feature.chat.model.MessageType
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.ChatsDetailAdapter
import com.emproto.hoabl.feature.investment.adapters.OnOptionClickListener
import com.emproto.hoabl.feature.investment.views.FaqDetailFragment
import com.emproto.hoabl.feature.investment.views.ProjectDetailFragment
import com.emproto.hoabl.feature.portfolio.views.BookingjourneyFragment
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
    var chatsList: CData? = null
    var chatHistoryList: Data? = null
    var chatDetailList: ChatDetailResponse.ChatDetailList? = null
    lateinit var chatsDetailAdapter: ChatsDetailAdapter
    var newChatMessageList = ArrayList<ChatDetailModel>()
    private var c: Calendar? = null
    private var sdf: SimpleDateFormat? = null
    private var time: String? = null
    private var isMessagesEnabled = true
    private var latestConversationId = 0
    private var isMyFirstCallCompleted = false
    lateinit var handler: Handler
    private var runnable: Runnable? = null

    lateinit var binding: FragmentChatsDetailBinding

    companion object{
        const val MORE_OPTIONS = 1
        const val NAVIGATE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        binding.tvChatTitle.text = chatsList?.project?.projectContent?.launchName.toString()
        context?.let {
            Glide.with(it)
                .load(chatsList?.project?.projectContent?.projectCoverImages?.chatPageMedia?.value?.url)
                .placeholder(R.drawable.ic_baseline_image_24).into(binding.ivChatThumb)
        }
        binding.ivBack.setOnClickListener {
            hideKeyboard()
            onBackPressed()
        }

        binding.etType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                when {
                    s.toString().length > 0 -> {
                        binding.clSend.background =
                            resources.getDrawable(R.drawable.send_button_blue_bg)
                    }
                }
            }

        })

        callChatHistoryApi()

        binding.clButtonStart.setOnClickListener {
            binding.clButtonStart.visibility = View.GONE
//            binding.tvDay.visibility = View.VISIBLE
            callChatInitiateApi()
            getDay()
        }
    }

    private fun callChatHistoryApi() {
        homeViewModel.getChatHistory(
            chatsList?.project?.crmId.toString(),
            chatsList?.isInvested!!
        ).observe(viewLifecycleOwner, Observer {
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
//                            binding.tvDay.visibility = View.VISIBLE
                            chatHistoryList = it.data
                            getTime()
                            newChatMessageList.clear()
                            if (it.data.conversation != null) {
                                when (it.data.conversation.isOpen) {
                                    true -> {
                                        isMessagesEnabled = true
                                    }
                                }
                            } else {
                                binding.clButtonStart.visibility = View.VISIBLE
                            }

                            //Welcome message
                            if (it.data.autoChat != null) {
                                newChatMessageList.add(
                                    ChatDetailModel(
                                        it.data.autoChat.chatJSON.welcomeMessage.toString(),
                                        null, MessageType.RECEIVER, time
                                    )
                                )
                            } else {
                                newChatMessageList.add(
                                    ChatDetailModel(
                                        "Hi Welcome",
                                        null, MessageType.RECEIVER, time
                                    )
                                )
                            }
                            //Old messages
                            for (item in it.data.messages) {
                                if (item.message != null) {
                                    when {
                                        item.origin == "2" -> {
                                            newChatMessageList.add(
                                                ChatDetailModel(
                                                    item.message,
                                                    item.options,
                                                    MessageType.RECEIVER, time,
                                                    item.conversationId
                                                )
                                            )

                                        }
                                        item.origin == "1" -> {
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
                                if (messagesList[i].origin == "2" && messagesList[i].message == resources.getString(
                                        R.string.describe_issue
                                    )
                                ) {
                                    binding.clType.visibility = View.VISIBLE
                                    binding.clButtonStart.visibility = View.INVISIBLE
                                    sendTypedMessage()
                                }
                            }
                            if (messagesList[messagesList.size - 1].message == resources.getString(R.string.thank_you_text)) {
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
        })
    }

    private fun callChatInitiateApi() {
        homeViewModel.chatInitiate(
            ChatInitiateRequest(
                chatsList?.isInvested,
                chatsList?.project?.crmId.toString()
            )
        ).observe(viewLifecycleOwner, Observer {
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
                        latestConversationId = it.data!!.chatDetailList.conversation.id
                        addMessages(it.data!!.chatDetailList)
                        chatsDetailAdapter.notifyDataSetChanged()
                        binding.rvChat.smoothScrollToPosition(newChatMessageList.size - 1)
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

    private fun addMessages(chatDetailList: ChatDetailResponse.ChatDetailList) {
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

    override fun onOptionClick(option: Option, view: View, position: Int, conversationId: Int) {
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

                    sendMessage(option.text, 1, option.action.toString().toInt(), null)

                    if (option.actionType == MORE_OPTIONS) {
                        when {
                            option.action == "105" -> {
                                if(chatsList?.isInvested == false){
                                    val fragment = FaqDetailFragment()
                                    val bundle = Bundle()
                                    bundle.putBoolean(Constants.IS_FROM_INVESTMENT, false)
                                    bundle.putString(Constants.PROJECT_NAME, "")
                                    fragment.arguments = bundle
                                    (requireActivity() as HomeActivity).addFragment(
                                        fragment,
                                        true
                                    )
                                }else{
                                    addingOptions(option)
                                }
                            }
                            else -> addingOptions(option)
                        }
                    } else if (option.actionType == NAVIGATE) {
                        when (option.action) {
                            "109" -> {
                                (requireActivity() as HomeActivity).addFragment(
                                    AboutUsFragment(),
                                    true
                                )
                            }
                            "111" -> {
                                chatsList?.project?.let {
                                    val bundle = Bundle()
                                    bundle.putInt(Constants.PROJECT_ID, it.projectContent.id)
                                    val fragment = ProjectDetailFragment()
                                    fragment.arguments = bundle
                                    (requireActivity() as HomeActivity).addFragment(
                                        fragment, true
                                    )
                                }
                            }
                            "113" -> {
                                (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)
                            }
                            "119" -> {
                                (requireActivity() as HomeActivity).navigate(R.id.navigation_portfolio)
                            }
                            "120" -> {
                                chatsList?.portfolioData?.let {
                                    (requireActivity() as HomeActivity).addFragment(
                                        BookingjourneyFragment.newInstance(
                                            it.investmentId,
                                            ""
                                        ), true
                                    )
                                }
                            }
                            "121" -> {
                                chatsList?.project?.let{
                                    (requireActivity() as HomeActivity).addFragment(
                                        ProjectTimelineFragment.newInstance(
                                            it.projectContent.id,
                                            ""
                                        ), true
                                    )
                                }
                            }
                            "114" -> {
                                chatsList?.project?.let {
                                    val fragment = FaqDetailFragment()
                                    val bundle = Bundle()
                                    bundle.putInt(Constants.PROJECT_ID, it.projectContent.id)
                                    bundle.putBoolean(Constants.IS_FROM_INVESTMENT, true)
                                    bundle.putString(Constants.PROJECT_NAME, it.launchName)
                                    fragment.arguments = bundle
                                    (requireActivity() as HomeActivity).addFragment(fragment, true)
                                }
                            }
                        }
                    } else if (option.action == "108") {
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
                                        chatDetailList!!.autoChat.chatJSON.allowTypingMessage,
                                        2,
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
                                        chatHistoryList!!.autoChat.chatJSON.allowTypingMessage,
                                        2,
                                        null,
                                        null
                                    )
                                }
                                runnable?.let { it1 -> handler.postDelayed(it1, 2000) }


                            }
                        }
                        binding.rvChat.smoothScrollToPosition(newChatMessageList.size - 1)
                        sendTypedMessage()

                    } else if (option.action == "100") {
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
                                        chatDetailList!!.autoChat.chatJSON.finalMessage,
                                        2,
                                        null,
                                        null
                                    )
                                }
                                runnable?.let { it1 -> handler.postDelayed(it1, 2000)}
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
                                        chatHistoryList!!.autoChat.chatJSON.finalMessage,
                                        2,
                                        null,
                                        null
                                    )
                                }
                                runnable?.let { it1 -> handler.postDelayed(it1, 2000)}
                            }
                        }

                        chatsDetailAdapter.notifyDataSetChanged()
                        binding.rvChat.smoothScrollToPosition(newChatMessageList.size - 1)

                    }
                }
            }
        }
    }

    private fun addingOptions(option: Option) {
        when{
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
                                chatDetailList!!.autoChat.chatJSON.chatBody[i].message,
                                2,
                                null,
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
                                chatHistoryList!!.autoChat.chatJSON.chatBody[i].message,
                                2,
                                null,
                                chatHistoryList!!.autoChat.chatJSON.chatBody[i].options
                            )
                        }
                        runnable?.let { it1 -> handler.postDelayed(it1, 2000) }
                        chatsDetailAdapter.notifyDataSetChanged()
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
                sendMessage(binding.etType.text.toString(), 1, null, null)
                chatsDetailAdapter.notifyDataSetChanged()
                binding.rvChat.smoothScrollToPosition(newChatMessageList.size - 1)
                binding.etType.text.clear();
            } else {
                Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun sendMessage(
        text: String?,
        origin: Int,
        selection: Int?,
        options: ArrayList<Option>?
    ) {
        when {
            chatDetailList != null -> {
                homeViewModel.sendMessage(
                    SendMessageBody(
                        conversationId = chatDetailList?.conversation?.id.toString(),
                        message = text.toString(),
                        crmProjectId = chatsList?.project?.crmProjectId.toString(),
                        origin = origin,
                        selection = selection,
                        crmLaunchPhaseId = chatsList?.project?.crmId.toString(),
                        launchPhaseId = chatsList?.project?.projectContent?.id.toString(),
                        options = options
                    )
                ).observe(this, Observer {
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
                })
            }
            else -> {
                homeViewModel.sendMessage(
                    SendMessageBody(
                        conversationId = chatHistoryList?.conversation?.id.toString(),
                        message = text.toString(),
                        crmProjectId = chatsList?.project?.crmProjectId.toString(),
                        origin = origin,
                        selection = selection,
                        crmLaunchPhaseId = chatsList?.project?.crmId.toString(),
                        launchPhaseId = chatsList?.project?.projectContent?.id.toString(),
                        options = options
                    )
                ).observe(this, Observer {
                    when (it.status) {
                        Status.LOADING -> {

                        }
                        Status.SUCCESS -> {
                            it.data?.let {
//                                if (it.data.message.origin == "1") {
//                                    isMyFirstCallCompleted = true
//                                }
                            }
                        }
                        Status.ERROR -> {
                            (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                        }
                    }
                })
            }
        }

    }

    private fun getTime() {
        c = Calendar.getInstance()
        sdf = SimpleDateFormat("h:mm a")
        time = sdf!!.format(c!!.time)
    }

    override fun onDestroy() {
        super.onDestroy()
        runnable?.let { handler.removeCallbacks(it) }
    }

}
