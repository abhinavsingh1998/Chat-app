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
import com.emproto.hoabl.databinding.FragmentChatsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.adapter.ChatsAdapter
import com.emproto.networklayer.response.chats.ChatResponse
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.response.chats.ChatResponse.*
import com.emproto.networklayer.response.enums.Status
import java.io.Serializable
import javax.inject.Inject

class ChatsFragment : Fragment(), ChatsAdapter.OnItemClickListener {
    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatsBinding.inflate(layoutInflater)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]

        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getChatsList().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.loader.show()
                    binding.rvChats.visibility = View.INVISIBLE
                }
                Status.SUCCESS -> {
                    binding.loader.hide()
                    binding.rvChats.visibility = View.VISIBLE
                    if (it.data?.chatList != null && it.data!!.chatList is List<ChatList>) {
                        Log.i("LastMsg",it.data!!.chatList.toString())
                        binding.rvChats.layoutManager =
                            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                        binding.rvChats.adapter = ChatsAdapter(context, it.data!!.chatList, this)

                        val chatListSize=it.data!!.chatList.size.toString()
                        binding.tvChats.text="Chat($chatListSize)"

                    }
                }
                Status.ERROR -> {
                    binding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(it.message!!)
                }
            }
        })
    }
    override fun onChatItemClick(chat: List<ChatList>, view: View, position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("chatModel", chat[position])
        val chatsDetailFragment = ChatsDetailFragment()
        chatsDetailFragment.arguments = bundle
        (requireActivity() as HomeActivity).replaceFragment(chatsDetailFragment.javaClass,
            "",
            true,
            bundle,
            null,
            0,
            false
        )
    }
}