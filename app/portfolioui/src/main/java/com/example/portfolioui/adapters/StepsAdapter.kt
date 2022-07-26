package com.example.portfolioui.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Utility
import com.example.portfolioui.R
import com.example.portfolioui.databinding.ItemTimelineInprogressBinding
import com.example.portfolioui.databinding.ItemTimelineStepCompletedBinding
import com.example.portfolioui.databinding.ItemTimelineStepDisabledBinding
import com.example.portfolioui.models.StepsModel
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon

class StepsAdapter(
    var context: Context,
    private val dataList: ArrayList<StepsModel>,
    val itemInterface: TimelineAdapter.TimelineInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_INSTART = 0
        const val TYPE_INPROGRESS = 1
        const val TYPE_COMPLETED = 2

    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {

            TYPE_INSTART -> {
                val view = ItemTimelineStepDisabledBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return InStartHolder(view)
            }

            TYPE_INPROGRESS -> {
                val view = ItemTimelineInprogressBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return InProgressHolder(view)
            }

            else -> {
                val view =
                    ItemTimelineStepCompletedBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return InCompletedHolder(view)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (dataList[position].viewType) {

            TYPE_INSTART -> {

                val type1Holder = holder as InStartHolder
                val data = dataList[position].timeline
                type1Holder.binding.tvName.text = data.values.displayName


                type1Holder.binding.imageView.setOnClickListener {
                    getToolTip(data.values.toolTipDetails).showAlignBottom(type1Holder.binding.imageView)
                }

            }
            TYPE_COMPLETED -> {
                val type1Holder = holder as InCompletedHolder
                val data = dataList[position].timeline
                type1Holder.binding.tvName.text = data.values.displayName
                type1Holder.binding.textView7.text = showHTMLText(
                    String.format(
                        context.getString(R.string.tv_receipt),
                        "View Details"
                    )
                )
                type1Holder.binding.imageView.setOnClickListener {
                    getToolTip(data.values.toolTipDetails).showAlignBottom(type1Holder.binding.imageView)
                }
                type1Holder.binding.textView7.setOnClickListener {
                    itemInterface.onClickVDetails(
                        data.values.medias.key,
                        data.values.medias.value.url
                    )

                }
            }
            TYPE_INPROGRESS -> {
                val type1Holder = holder as InProgressHolder
                val data = dataList[position].timeline
                type1Holder.binding.tvName.text = data.values.displayName
                type1Holder.binding.tvPercentage.text =
                    "${Utility.convertTo(data.values.percentage)}%"

                type1Holder.binding.textView7.text = showHTMLText(
                    String.format(
                        context.getString(R.string.tv_receipt),
                        "View Details"
                    )
                )

                type1Holder.binding.imageView.setOnClickListener {
                    getToolTip(data.values.toolTipDetails).showAlignBottom(type1Holder.binding.imageView)
                }
            }
        }
    }

    fun getToolTip(text: String): Balloon {
        val balloon = createBalloon(context) {
            setArrowSize(6)
            setWidth(BalloonSizeSpec.WRAP)
            setTextSize(12F)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setText(text)
            setTextColorResource(R.color.white)
            setBackgroundColorResource(R.color.black)
            setPadding(5)
            setTextTypeface(ResourcesCompat.getFont(context, R.font.jost_medium)!!)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner)
        }
        return balloon
    }


    override fun getItemCount() = dataList.size

    inner class InStartHolder(var binding: ItemTimelineStepDisabledBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class InProgressHolder(var binding: ItemTimelineInprogressBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class InCompletedHolder(var binding: ItemTimelineStepCompletedBinding) :
        RecyclerView.ViewHolder(binding.root)


    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }

}
