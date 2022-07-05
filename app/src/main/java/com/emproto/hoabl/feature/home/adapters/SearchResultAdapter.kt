package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Database.TableModel.SearchModel
import com.emproto.hoabl.databinding.ItemSearchRecentBinding

class SearchResultAdapter(mcontext: Context, mlist: List<SearchModel>) :
    RecyclerView.Adapter<SearchResultAdapter.MyViewHolder>() {
    var list: List<SearchModel>
    var mcontext: Context
    lateinit var monInsightsClickItem: OnInsightsClickItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemSearchRecentBinding =
            ItemSearchRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(binding: ItemSearchRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemSearchRecentBinding

        init {
            this.binding = binding
        }
    }


    interface OnInsightsClickItem {
        fun onclickDetails()
    }


    init {
        this.list = mlist
        this.mcontext = mcontext
    }
}