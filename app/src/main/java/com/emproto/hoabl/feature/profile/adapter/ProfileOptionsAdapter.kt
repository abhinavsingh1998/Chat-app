package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ProfileOptionViewBinding
import com.emproto.hoabl.feature.profile.fragments.profile.ProfileFragment
import com.emproto.hoabl.feature.profile.data.ProfileModel

class ProfileOptionsAdapter(
    var context: Context,
    val dataList: ArrayList<ProfileModel>,
    val helpItemInterface: ProfileFragment
) : RecyclerView.Adapter<ProfileOptionsAdapter.ProfileViewModel>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewModel {
        val view = ProfileOptionViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewModel(view)

    }

    override fun onBindViewHolder(header_holder: ProfileViewModel, position: Int) {
        val item = dataList[header_holder.layoutPosition].data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            header_holder.binding.ivIcon.setImageResource(item.img)
        }
        header_holder.binding.tvTitle.text = item.title
        header_holder.binding.tvDescirption.text = item.description


        header_holder.binding.hoabelView.setOnClickListener {
            helpItemInterface.onClickItem(header_holder.layoutPosition)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ProfileViewModel(var binding: ProfileOptionViewBinding) :
        RecyclerView.ViewHolder(binding.root)


    interface HelpItemInterface {
        fun onClickItem(position: Int)
    }

}
