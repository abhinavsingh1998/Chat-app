package com.emproto.hoabl.feature.chat.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.databinding.FragmentChatsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.adapter.ChatsAdapter
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.chats.CData
import com.emproto.networklayer.response.enums.Status
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ChatsFragment : BaseFragment(), ChatsAdapter.OnItemClickListener {

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    lateinit var binding: FragmentChatsBinding

    private var timePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        callChatListApi()
    }

    private fun initData() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        binding.clRefresh.setOnClickListener {
            callChatListApi()
        }
    }

    private fun callChatListApi() {
        homeViewModel.getChatsList().observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.LOADING -> {
                    binding.loader.show()
                    binding.rvChats.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    binding.rvChats.visibility = View.VISIBLE
                    response.data.let { chatResponse ->
                        if (chatResponse?.data != null && chatResponse.data is List<CData>) {
                            binding.rvChats.adapter =
                                ChatsAdapter(requireContext(), chatResponse.data, this)
                            "Chat (${chatResponse.data.size})".also { binding.tvChats.text = it }
                            //setting last updated time
                            val dateListInMS = ArrayList<Long>()
                            for (item in chatResponse.data) {
                                if (item.lastMessage != null) {
                                    val format = SimpleDateFormat(timePattern, Locale.getDefault())
                                    format.timeZone =
                                        TimeZone.getTimeZone("GMT") //setting timezone for avoiding minus 5hrs
                                    val date = format.parse(item.lastMessage.createdAt)
                                    val createdTimeInMs = date?.time // get time in milliseconds
                                    dateListInMS.add(
                                        createdTimeInMs.toString().toLong()
                                    )  //adding time in arraylist
                                }
                            }
                            dateListInMS.sortDescending() //sorting in descending order to get max milliseconds
                            if (dateListInMS.isNotEmpty()) {
                                val lastUpdateTimeInMs =
                                    dateListInMS[0] //picking first one in arraylist because more milliseconds means its the latest time
                                val sdf =
                                    SimpleDateFormat(
                                        "dd/MM/yyyy  hh:mm:ss aa",
                                        Locale.getDefault()
                                    )  //creating pattern based on our requirement
                                val calendar = Calendar.getInstance()
                                calendar.timeInMillis = lastUpdateTimeInMs
                                binding.tvLastUpdatedTime.text =
                                    sdf.format(calendar.time) //formatting time with our pattern based on calendar
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(response.message!!)
                }
            }
        }
    }

    override fun onChatItemClick(chatList: List<CData>, view: View, position: Int) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.CHAT_MODEL, chatList[position] as Serializable)
        val chatsDetailFragment = ChatsDetailFragment()
        chatsDetailFragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(chatsDetailFragment, true)
    }
}