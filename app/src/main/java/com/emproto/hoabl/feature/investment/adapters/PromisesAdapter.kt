package com.emproto.hoabl.feature.investment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.databinding.ItemPromisesBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.investment.PmData
import javax.inject.Inject

class PromisesAdapter(
    private val list: List<PmData>,
    private val itemClickListener: ItemClickListener,
    private val context: Context,
    val count: Int = 0
) : RecyclerView.Adapter<PromisesAdapter.MyViewHolder>() {
    @Inject
    lateinit var appPreference: AppPreference
    inner class MyViewHolder(var binding: ItemPromisesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemPromisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val element = list[position]
        holder.binding.apply {
            desc.text = element.shortDescription
            title.text = element.name
            Glide
                .with(context)
                .load(element.displayMedia?.value?.url)
                .into(image)

        }
        holder.binding.itemCard.setOnClickListener {
            itemClickListener.onItemClicked(it, position, element.id.toString())
        }
    }



    override fun getItemCount(): Int {

        return if (list.size <= count)
            list.size
        else count
    }

}