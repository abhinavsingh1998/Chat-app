package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.IssueListBinding


class IssueListAdapter(context: Context, list: ArrayList<String>, listner:OnItemClicked) :RecyclerView.Adapter<IssueListAdapter.MyViewHolder>() {

    var list: List<String> = list
    var mcontext: Context = context
    lateinit var binding: IssueListBinding
    val listner:OnItemClicked= listner

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.issue_list, parent, false)
        val viewHolder= MyViewHolder(view)
        view.setOnClickListener{

            listner.onItemClicked(list[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.issue_txt.setText(list.get(position))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: View) :
        RecyclerView.ViewHolder(binding) {
        var radioBtn: ImageView= itemView.findViewById(R.id.radio_btn)
        var issue_txt: TextView= itemView.findViewById(R.id.issue_txt)

    }

    interface OnItemClicked{

        fun onItemClicked(s: String) {

        }
    }

}
