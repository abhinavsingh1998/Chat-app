package com.emproto.hoabl.feature.promises.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.PromisesItemDataBinding
import com.emproto.hoabl.databinding.PromisesItemHeaderBinding
import com.emproto.hoabl.feature.promises.data.PromisesData

class HoabelPromiseAdapter (
    var context: Context,
    val dataList: ArrayList<PromisesData>,
    val itemInterface: PromisedItemInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_LIST = 1
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewTyppe
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_HEADER -> {
                val view = PromisesItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return PromisesHeaderViewHolder(view)
            }
            TYPE_LIST -> {
                val view =
                    PromisesItemDataBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return HoablPromiseViewHolder(view)
            }
            else -> {
                val view =
                    PromisesItemDataBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return HoablPromiseViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            TYPE_HEADER -> {
                val header_holder = holder as PromisesHeaderViewHolder
            }
            TYPE_LIST -> {
                val listHolder = holder as HoablPromiseViewHolder
                listHolder.binding.promisesItems.layoutManager = GridLayoutManager(context, 2)
                listHolder.binding.promisesItems.adapter =
                    PromisesListAdapter(context, dataList[position].data, itemInterface)
            }
        }
    }


    override fun getItemCount() = dataList.size

    inner class HoablPromiseViewHolder(var binding: PromisesItemDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class PromisesHeaderViewHolder(var binding: PromisesItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface PromisedItemInterface {
        fun onClickItem(position: Int)
    }

}
