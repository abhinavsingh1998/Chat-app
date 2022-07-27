package com.emproto.hoabl.feature.chat.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentChatsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.adapter.ChatsAdapter
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.chats.CData
import com.emproto.networklayer.response.chats.ChatResponse.*
import com.emproto.networklayer.response.enums.Status
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ChatsFragment : BaseFragment(), ChatsAdapter.OnItemClickListener {

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel

    lateinit var binding: FragmentChatsBinding

    var timePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatas()
        callChatListApi()
    }

    private fun initDatas() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE
        binding.clRefresh.setOnClickListener{
            callChatListApi()
        }
    }

    private fun callChatListApi() {
        homeViewModel.getChatsList().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.loader.show()
                    binding.rvChats.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    binding.rvChats.visibility = View.VISIBLE
                    it.data.let {
                        if (it?.data != null && it.data is List<CData>) {
                            binding.rvChats.adapter = ChatsAdapter(requireContext(), it.data, this)
                            binding.tvChats.text = "Chat (${it.data.size.toString()})"
                            //setting last updated time
                            val dateListInMS = ArrayList<Long>()
                            for(item in it.data){
                                if(item.lastMessage!= null){
                                    val format = SimpleDateFormat(timePattern)
                                    format.timeZone = TimeZone.getTimeZone("GMT")
                                    val date = format.parse(item.lastMessage.createdAt)
                                    val createdTimeInMs = date?.time
                                    dateListInMS.add(createdTimeInMs.toString().toLong())
                                }
                            }
                            dateListInMS.sortDescending()
                            Log.d("chat","datelist= ${dateListInMS.toString()}")
                            var lastUpdateTimeInMs = 57577575757
                            if(dateListInMS.isNotEmpty()){
                                lastUpdateTimeInMs = dateListInMS[0]
                            }
                            Log.d("chat","lastupdatetime= ${lastUpdateTimeInMs.toString()}")
                            val currentTimeInMs = System.currentTimeMillis()
                            val differenceTimeInMs = currentTimeInMs - lastUpdateTimeInMs.toString().toLong()
                            Log.d("chat","differenceTimeInMs= ${differenceTimeInMs.toString()}")
                            val sdf = SimpleDateFormat("dd/MM/yyyy  hh:mm:ss aa")
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = lastUpdateTimeInMs
                            binding.tvLastUpdatedTime.text = sdf.format(calendar.time)
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

    override fun onChatItemClick(chat: List<CData>, view: View, position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("chatModel", chat[position] as Serializable)
        val chatsDetailFragment = ChatsDetailFragment()
        chatsDetailFragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(chatsDetailFragment,true)
    }
}