package com.emproto.hoabl.feature.promises.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.PromisesItemDataBinding
import com.emproto.hoabl.databinding.PromisesItemDisclaimerBinding
import com.emproto.hoabl.databinding.PromisesItemHeaderBinding
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.promises.data.PromisesData
import com.emproto.networklayer.preferences.AppPreference
import javax.inject.Inject

class HoablPromiseAdapter(
    var context: Context,
    private val dataList: ArrayList<PromisesData>,
    private val itemInterface: PromisedItemInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @Inject
    lateinit var appPreference: AppPreference
    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_LIST = 1
        const val TYPE_DISCLAIMER = 2
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
            TYPE_DISCLAIMER -> {
                val view =
                    PromisesItemDisclaimerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return DisclaimerViewHolder(view)
            }
            else -> {
                val view =
                    PromisesItemDisclaimerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return DisclaimerViewHolder(view)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            TYPE_HEADER -> {
                val header_holder = holder as PromisesHeaderViewHolder
                header_holder.binding.tvTitle.text =
                    dataList[holder.layoutPosition].headerData!!.aboutPromises.sectionHeading
                header_holder.binding.tvDescription.text =
                    dataList[holder.layoutPosition].headerData!!.aboutPromises.subDescription
            }
            TYPE_LIST -> {
                eventTrackingPromiseCard()
                val listHolder = holder as HoablPromiseViewHolder
                listHolder.binding.textView9.text =
                    dataList[position].headerData?.promiseSectionHeading.toString()
                listHolder.binding.promisesItems.layoutManager = GridLayoutManager(context, 2)
                listHolder.binding.promisesItems.adapter =
                    PromisesListAdapter(context, dataList[position].data, itemInterface)
                listHolder.binding.promisesItems.setItemViewCacheSize(10)
                listHolder.binding.promisesItems.setHasFixedSize(true)
            }
            TYPE_DISCLAIMER -> {
                val disclaimerViewHolder = holder as DisclaimerViewHolder
                disclaimerViewHolder.binding.tvGreetings.text =
                    dataList[holder.layoutPosition].headerData!!.disclaimer
            }
        }
    }

    private fun eventTrackingPromiseCard() {
        Mixpanel(context).identifyFunction(appPreference.getMobilenum(), Mixpanel.PROMISECARD)
    }

    override fun getItemCount() = dataList.size

    inner class HoablPromiseViewHolder(var binding: PromisesItemDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class PromisesHeaderViewHolder(var binding: PromisesItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class DisclaimerViewHolder(var binding: PromisesItemDisclaimerBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface PromisedItemInterface {
        fun onClickItem(position: Int)
    }

}
