package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.CustomViewpagerImageLayoutBinding
import com.emproto.hoabl.databinding.SeeAllLayoutBinding
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.preferences.AppPreference
import javax.inject.Inject

class ProjectDetailViewPagerAdapter(
    private val context: Context,
    private val list: ArrayList<RecyclerViewItem>,
    private val imageList: List<String>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @Inject
    lateinit var appPreference: AppPreference

    companion object {
        const val IMAGE = 1
        const val SEE_ALL = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IMAGE -> {
                ImageViewHolder(
                    CustomViewpagerImageLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                SeeAllViewHolder(
                    SeeAllLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    private inner class SeeAllViewHolder(private val binding: SeeAllLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.clSeeAll.setOnClickListener {
                itemClickListener.onItemClicked(it, position, position.toString())
            }
        }
    }


    private inner class ImageViewHolder(private val binding: CustomViewpagerImageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val element = imageList[position]
            Glide.with(context)
                .load(element)
                .into(binding.image)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].viewType) {
            IMAGE -> {
                (holder as ImageViewHolder).bind(position)
            }
            SEE_ALL -> {
                (holder as SeeAllViewHolder).bind(position)
            }
        }
    }

    override fun getItemCount(): Int = imageList.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

}