package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.BuildConfig
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.HelpCenterFooterBinding
import com.emproto.hoabl.databinding.ItemHelpCenterBinding
import com.emproto.hoabl.databinding.ProfileFooterBinding
import com.emproto.hoabl.databinding.ProfileOptionViewBinding
import com.emproto.hoabl.feature.profile.fragments.profile.ProfileFragment
import com.emproto.hoabl.feature.profile.data.ProfileModel

class ProfileOptionsAdapter(
    var context: Context,
    val dataList: ArrayList<ProfileModel>,
    val profileItemInterface: ProfileItemInterface,
    val profileFooterInterface: ProfileFooterInterface

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
               val header_holder = holder as ProfileOptionViewHolder
               val item = dataList[header_holder.layoutPosition].data

               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   header_holder.binding.ivIcon.setImageResource(item.img)
               }
               header_holder.binding.tvTitle.text = item.title
               header_holder.binding.tvDescirption.text = item.description

               header_holder.binding.hoabelView.setOnClickListener {
                   profileItemInterface.onClickItem(header_holder.layoutPosition)
               }

            }
           VIEW_FOOTER -> {
                val listHolder = holder as ProfileFooterViewHolder
                listHolder.binding.Logoutbtn.setOnClickListener {
                    profileFooterInterface.onLogoutClickItem(holder.layoutPosition)
                }
               listHolder.binding.version.text = "App Version:" + BuildConfig.VERSION_NAME
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
