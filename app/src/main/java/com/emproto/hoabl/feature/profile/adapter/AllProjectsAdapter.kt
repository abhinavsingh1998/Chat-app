package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.CorporatePhilosphyAboutUsViewBinding
import com.emproto.hoabl.databinding.ProjectTabLayoutBinding
import com.emproto.hoabl.feature.home.adapters.InsightsAdapter
import com.emproto.networklayer.response.profile.DataXXX

class AllProjectsAdapter(
    private val context: Context,
    private val list: List<DataXXX>,
    val itemIntrface: AllprojectsInterface
): RecyclerView.Adapter<AllProjectsAdapter.MyViewHolder>(){

    var selectedItemPos = 0
    var lastItemSelectedPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProjectsAdapter.MyViewHolder {
        val view = ProjectTabLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllProjectsAdapter.MyViewHolder, position: Int) {
        val currentItem= list.get(position)

        holder.binding.projectName.text= currentItem?.launchName


        Glide.with(context).load(currentItem.projectCoverImages.chatPageMedia.value.url)
            .into(holder.binding.projectImg)

        if(position == selectedItemPos) {
            holder.binding.tabBar.isVisible= true


        }
        else {

            holder.binding.tabBar.isVisible= false

        }

//        if(position==0){
//            holder.binding.rootView.performClick()
//        }

        holder.binding.rootView.setOnClickListener {

            lastItemSelectedPos = selectedItemPos
            selectedItemPos = position
            if(lastItemSelectedPos == -1)
                lastItemSelectedPos = selectedItemPos
            else {
                notifyItemChanged(lastItemSelectedPos)
                lastItemSelectedPos = selectedItemPos
            }
            notifyItemChanged(selectedItemPos)

            holder.binding.tabBar.isVisible= true
            itemIntrface.onClickItem(holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
    inner class MyViewHolder(val binding: ProjectTabLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface AllprojectsInterface {
        fun onClickItem(position: Int)
    }


    public fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}