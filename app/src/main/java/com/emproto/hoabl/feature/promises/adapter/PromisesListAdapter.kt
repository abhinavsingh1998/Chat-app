package com.emproto.hoabl.feature.promises.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.PromisesViewBinding
import com.emproto.networklayer.response.promises.HomePagesOrPromise


class PromisesListAdapter(
    val context: Context,
    private val data: List<HomePagesOrPromise>,
    val itemInterface: HoabelPromiseAdapter.PromisedItemInterface

) : RecyclerView.Adapter<PromisesListAdapter.PromisesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromisesHolder {
        val view =
            PromisesViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromisesHolder(view)
    }

    override fun onBindViewHolder(holder: PromisesHolder, position: Int) {
        val item = data[position]
        holder.binding.title.text = item.name
        holder.binding.desc.text = item.shortDescription
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.binding.image.setImageResource(R.drawable.securitylock)
        }
        if (item.displayMedia!=null){
            if (item.displayMedia?.value?.url != null) {
                Glide.with(context)
                    .load(item.displayMedia?.value?.url)
                    .into(holder.binding.image)
            }
        }


        //holder.binding.arrowImage.setImageResource(item.arrowImage)
        holder.binding.itemCard.setOnClickListener {
            itemInterface.onClickItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class PromisesHolder(var binding: PromisesViewBinding) :
        RecyclerView.ViewHolder(binding.root)

}
