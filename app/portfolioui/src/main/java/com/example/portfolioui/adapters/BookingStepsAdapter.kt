package com.example.portfolioui.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.emproto.networklayer.response.bookingjourney.Payment
import com.emproto.networklayer.response.bookingjourney.Registration
import com.emproto.networklayer.response.profile.AccountsResponse
import com.example.portfolioui.R
import com.example.portfolioui.databinding.ItemBokingjourBinding
import com.example.portfolioui.models.BookingStepsModel

class BookingStepsAdapter(
    var context: Context,
    private val dataList: List<BookingStepsModel>,
    val itemInterface: BookingJourneyAdapter.TimelineInterface,
    val type: Int = -1
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_INSTART = 0
        const val TYPE_INPROGRESS = 1
        const val TYPE_COMPLETED = 2

        const val SECTION_PAYMENT = 0
        const val SECTION_DOCUMENTATION = 1

    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {

            TYPE_INPROGRESS -> {
                val view = ItemBokingjourBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return InProgressHolder(view)
            }

            else -> {
                val view =
                    ItemBokingjourBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return InProgressHolder(view)
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (dataList[position].type) {

            TYPE_COMPLETED -> {
                var docData: AccountsResponse.Data.Document? = null
                if (dataList[position].data is AccountsResponse.Data.Document) {
                    docData = dataList[position].data as AccountsResponse.Data.Document
                }
                val type1Holder = holder as InProgressHolder
                val data = dataList[position]
                type1Holder.binding.tvTitle.text = data.text
                type1Holder.binding.tvDescription.text = data.description

                type1Holder.binding.tvLink.text =
                    showHTMLText(
                        String.format(
                            context.getString(R.string.tv_receipt),
                            data.linkText
                        )
                    )
                if (type == SECTION_PAYMENT) {
                    type1Holder.binding.imageView3.visibility = View.VISIBLE
                    type1Holder.binding.imageView3.setImageDrawable(context.getDrawable(R.drawable.rupee_filled))
                    type1Holder.binding.tvLink.visibility = View.GONE
                } else {
                    type1Holder.binding.imageView3.visibility = View.GONE
                    type1Holder.binding.tvLink.visibility = View.VISIBLE

                }
                if (!data.disableLink) {
                    type1Holder.binding.tvLink.setOnClickListener {
                        if (data.text == "Registration") {
                            val rData = data.data as Registration
                            itemInterface.onClickRegistrationDetails(
                                rData.registrationDate,
                                rData.registrationNumber
                            )

                        } else {
                            docData?.let {
                                it.path?.let {
                                    itemInterface.onClickViewDocument(it)
                                }
                            }
                        }
                    }
                } else {
                    type1Holder.binding.tvLink.isClickable = false
                    type1Holder.binding.tvLink.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.disable_text
                        )
                    );
                }

            }
            TYPE_INPROGRESS -> {
                val type1Holder = holder as InProgressHolder
                val data = dataList[position]
                type1Holder.binding.tvTitle.text = data.text
                type1Holder.binding.tvDescription.text = data.description
                type1Holder.binding.tvLink.text =
                    showHTMLText(
                        String.format(
                            context.getString(R.string.tv_receipt),
                            data.linkText
                        )
                    )

                type1Holder.binding.ivProgressIcon.setImageDrawable(context.getDrawable(R.drawable.ic_inprogress_bg))
                type1Holder.binding.tvTitle.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.disable_text
                    )
                );
                type1Holder.binding.tvDescription.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.disable_text
                    )
                );

                if (type == SECTION_PAYMENT) {
                    var payment: Payment? = null
                    if (dataList[position].data is Payment)
                        payment = dataList[position].data as Payment
                    type1Holder.binding.imageView3.visibility = View.VISIBLE
                    type1Holder.binding.imageView3.setImageDrawable(context.getDrawable(R.drawable.rupee_disable))
                    type1Holder.binding.tvLink.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.disable_text
                        )
                    );
                    type1Holder.binding.tvLink.setOnClickListener {
                        itemInterface.onClickPendingCardDetails(payment!!)
                    }

                } else {
                    type1Holder.binding.imageView3.visibility = View.GONE
                    type1Holder.binding.tvLink.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.disable_text
                        )
                    );

                }

            }
        }
    }


    override fun getItemCount() = dataList.size

    inner class InProgressHolder(var binding: ItemBokingjourBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }

}
