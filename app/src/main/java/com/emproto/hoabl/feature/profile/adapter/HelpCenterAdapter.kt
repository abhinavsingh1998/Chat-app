package com.emproto.hoabl.feature.profile.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.HelpCenterFooterBinding
import com.emproto.hoabl.databinding.ItemHelpCenterBinding
import com.emproto.hoabl.feature.profile.data.HelpModel
import com.emproto.hoabl.utils.ItemClickListener


@Suppress("DEPRECATION")
class HelpCenterAdapter(
    var context: Context,
    private val dataList: ArrayList<HelpModel>,
    private val itemInterface: ItemClickListener,
    private val footerInterface: FooterInterface

) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_ITEM = 0
        const val VIEW_FOOTER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_ITEM -> {
                val view =
                    ItemHelpCenterBinding.inflate(
                        LayoutInflater.from(parent.context), parent,
                        false
                    )
                return HoablHealthViewHolder(view)
            }

            else -> {
                val view =
                    HelpCenterFooterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return HoablHealthFooterHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_ITEM -> {
                val item = dataList[holder.layoutPosition].data
                val headerHolder = holder as HelpCenterAdapter.HoablHealthViewHolder
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    headerHolder.binding.ivIcon.setImageResource(item.img)
                }
                headerHolder.binding.tvTitle.text = item.title
                headerHolder.binding.tvDescirption.text = item.description
                headerHolder.binding.hoabelView.setOnClickListener {
                    itemInterface.onItemClicked(it, position, item.title)
                }

            }
            VIEW_FOOTER -> {
                val listHolder = holder as HelpCenterAdapter.HoablHealthFooterHolder
                listHolder.binding.actionChat.setOnClickListener {
                    footerInterface.onChatClick(holder.layoutPosition)
                }

                listHolder.binding.email.setOnClickListener {
                    footerInterface.onEmailClick(holder.layoutPosition)
                }
                listHolder.binding.email.text = showHTMLText(context.getString(R.string.emailsid))
            }
        }
    }

    override fun getItemCount() = dataList.size
    inner class HoablHealthViewHolder(var binding: ItemHelpCenterBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class HoablHealthFooterHolder(var binding: HelpCenterFooterBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface FooterInterface {
        fun onChatClick(position: Int)
        fun onEmailClick(position: Int)

    }

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }

}