package com.example.portfolioui.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.portfolioui.R
import com.example.portfolioui.databinding.ItemBokingjourBinding
import com.example.portfolioui.databinding.ItemTimelineInprogressBinding
import com.example.portfolioui.databinding.ItemTimelineStepCompletedBinding
import com.example.portfolioui.databinding.ItemTimelineStepDisabledBinding
import com.example.portfolioui.models.BookingStepsModel
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon

class BookingStepsAdapter(
    var context: Context,
    private val dataList: List<BookingStepsModel>,
    val itemInterface: BookingJourneyAdapter.TimelineInterface?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_INSTART = 0
        const val TYPE_INPROGRESS = 1
        const val TYPE_COMPLETED = 2

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

            }
            TYPE_INPROGRESS -> {
                val type1Holder = holder as InProgressHolder
                val data = dataList[position]
                type1Holder.binding.tvTitle.text = data.text
                type1Holder.binding.tvDescription.text = data.description
                type1Holder.binding.tvLink.setTextColor(context.getColor(R.color.background_grey))
                type1Holder.binding.tvLink.text =
                    showHTMLText(
                        String.format(
                            context.getString(R.string.tv_receipt),
                            data.linkText
                        )
                    )
                type1Holder.binding.tvLink.setOnClickListener {
                    itemInterface?.viewDetails(0, "")
                }
                type1Holder.binding.ivProgressIcon.setImageDrawable(context.getDrawable(R.drawable.ic_in_progress))
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
