package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ItemHoablPromisesBinding
import com.emproto.hoabl.feature.portfolio.adapters.PortfolioSpecificViewAdapter
import com.emproto.networklayer.response.portfolio.ivdetails.DataX
import com.emproto.networklayer.response.promises.HomePagesOrPromise

class HoABLPromisesAdapter(
    val context: Context,
    val list: List<HomePagesOrPromise>,
    val ivInterface: PortfolioSpecificViewAdapter.InvestmentScreenInterface
) :
    RecyclerView.Adapter<HoABLPromisesAdapter.MyViewHolder>() {

    lateinit var binding: ItemHoablPromisesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding =
            ItemHoablPromisesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.binding.title.text = item.name
        holder.binding.desc.text = item.shortDescription
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.binding.image.setImageResource(R.drawable.securitylock)
        }
        if (item.displayMedia != null)
            Glide.with(context)
                .load(item.displayMedia?.value?.url)
                .into(holder.binding.image)

        holder.binding.homePromisesItem.setOnClickListener {
            ivInterface.seePromisesDetails(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class MyViewHolder(binding: ItemHoablPromisesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemHoablPromisesBinding = binding

    }

}
