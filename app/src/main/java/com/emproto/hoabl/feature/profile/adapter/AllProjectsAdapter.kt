package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ProjectTabLayoutBinding
import com.emproto.networklayer.response.profile.DataXXX

@Suppress("DEPRECATION")
class AllProjectsAdapter(
    private val context: Context,
    private val list: List<DataXXX>,
    private var selectedItemPos: Int,
    private val itemInterface: AllProjectsInterface
) : RecyclerView.Adapter<AllProjectsAdapter.MyViewHolder>() {

    private var lastItemSelectedPos = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllProjectsAdapter.MyViewHolder {
        val view =
            ProjectTabLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllProjectsAdapter.MyViewHolder, position: Int) {
        val currentItem = list[position]

        holder.binding.rootView.visibility = View.VISIBLE
        holder.binding.projectName.text = currentItem?.launchName
        //holder.binding.tabBar.isVisible = position == selectedItemPos
        holder.binding.tabBar.isVisible = selectedItemPos == holder.adapterPosition
        holder.binding.rootView.setOnClickListener {
            if (holder.adapterPosition != selectedItemPos) {
                selectedItemPos = holder.adapterPosition
                itemInterface.onClickItem(holder.adapterPosition)
                notifyDataSetChanged()
                //holder.binding.tabBar.isVisible = true

            }
            //lastItemSelectedPos = selectedItemPos
//                selectedItemPos = position
//                lastItemSelectedPos = if (lastItemSelectedPos == -1)
//                    selectedItemPos
//                else {
//                    notifyItemChanged(lastItemSelectedPos)
//                    selectedItemPos
//                }

            Glide.with(context)
                .load(currentItem.projectCoverImages.newInvestmentPageMedia.value.url)
                .into(holder.binding.projectImg)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ProjectTabLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface AllProjectsInterface {
        fun onClickItem(position: Int)
    }


    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}