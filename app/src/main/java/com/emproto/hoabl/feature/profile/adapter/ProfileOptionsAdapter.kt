package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.BuildConfig
import com.emproto.hoabl.databinding.ProfileFooterBinding
import com.emproto.hoabl.databinding.ProfileOptionViewBinding
import com.emproto.hoabl.feature.profile.data.ProfileModel

class ProfileOptionsAdapter(
    var context: Context,
    private val dataList: ArrayList<ProfileModel>,
    private val profileItemInterface: ProfileItemInterface,
    private val profileFooterInterface: ProfileFooterInterface

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_ITEM = 0
        const val VIEW_FOOTER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            HelpCenterAdapter.VIEW_ITEM -> {
                val view =
                    ProfileOptionViewBinding.inflate(
                        LayoutInflater.from(parent.context), parent,
                        false
                    )
                return ProfileOptionViewHolder(view)
            }

            else -> {
                val view =
                    ProfileFooterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ProfileFooterViewHolder(view)
            }
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_ITEM -> {
                val headerHolder = holder as ProfileOptionViewHolder
                val item = dataList[headerHolder.layoutPosition].data

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    headerHolder.binding.ivIcon.setImageResource(item.img)
                }
                headerHolder.binding.tvTitle.text = item.title
                headerHolder.binding.tvDescirption.text = item.description

                headerHolder.binding.hoabelView.setOnClickListener {
                    profileItemInterface.onClickItem(headerHolder.layoutPosition)
                }

            }
            VIEW_FOOTER -> {
                val listHolder = holder as ProfileFooterViewHolder
                listHolder.binding.Logoutbtn.setOnClickListener {
                    profileFooterInterface.onLogoutClickItem(holder.layoutPosition)
                }
                ("App Version:" + BuildConfig.VERSION_NAME).also {
                    listHolder.binding.version.text = it
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ProfileOptionViewHolder(var binding: ProfileOptionViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ProfileFooterViewHolder(var binding: ProfileFooterBinding) :
        RecyclerView.ViewHolder(binding.root)


    interface ProfileItemInterface {
        fun onClickItem(position: Int)
    }

    interface ProfileFooterInterface {
        fun onLogoutClickItem(position: Int)
    }
}
