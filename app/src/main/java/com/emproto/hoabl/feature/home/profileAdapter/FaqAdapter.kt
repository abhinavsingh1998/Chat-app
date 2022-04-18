package com.emproto.hoabl.feature.home.profileAdapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.ItemFaqBinding

class FaqAdapter(private val list:List<String>):RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {
    private lateinit var onItemClickListener : View.OnClickListener
    inner class FaqViewHolder(var binding: ItemFaqBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = ItemFaqBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.binding.ivFaqCardDropDown.setOnClickListener{
            holder.binding.ivFaqCardDropDown.visibility = View.INVISIBLE
            holder.binding.tvFaqAnswer.visibility = View.VISIBLE
            holder.binding.ivFaqCardUpArrow.visibility = View.VISIBLE
        }
        holder.binding.ivFaqCardUpArrow.setOnClickListener{
            holder.binding.ivFaqCardDropDown.visibility = View.VISIBLE
            holder.binding.tvFaqAnswer.visibility = View.GONE
            holder.binding.ivFaqCardUpArrow.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = list.size
    fun setItemClickListener(clickListener: View.OnClickListener) {
        onItemClickListener = clickListener
    }
}