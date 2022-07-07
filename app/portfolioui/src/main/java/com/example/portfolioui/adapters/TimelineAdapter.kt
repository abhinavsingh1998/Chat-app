package com.example.portfolioui.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.networklayer.response.portfolio.prtimeline.ProjectTimeline
import com.example.portfolioui.R
import com.example.portfolioui.databinding.ItemTimelineDataBinding
import com.example.portfolioui.databinding.ItemTimelineHeaderBinding
import com.example.portfolioui.databinding.ItemTimelineLandBinding
import com.example.portfolioui.databinding.ItemTimelineStepReraBinding
import com.example.portfolioui.models.StepsModel
import com.example.portfolioui.models.TimelineHeaderData
import com.example.portfolioui.models.TimelineModel
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon

class TimelineAdapter(
    var context: Context,
    val dataList: ArrayList<TimelineModel>,
    val itemInterface: TimelineInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_LIST = 1
        const val TYPE_RERA = 3
        const val TYPE_LAND = 4
        const val TYPE_DISCLAIMER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_HEADER -> {
                val view = ItemTimelineHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HeaderHolder(view)
            }
            TYPE_RERA -> {
                val view = ItemTimelineStepReraBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StepsReraViewHolder(view)

            }
            TYPE_LAND -> {
                val view = ItemTimelineLandBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StepsLandViewHolder(view)
            }

            else -> {
                val view =
                    ItemTimelineDataBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return StepsListHolder(view)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (dataList[position].viewType) {
            TYPE_HEADER -> {
                val header_holder = holder as HeaderHolder
                val headerData = dataList[position].data as TimelineHeaderData
                header_holder.binding.projectName.text = headerData.projectName
                header_holder.binding.tvAddress.text = headerData.address
            }
            TYPE_LAND -> {
                val listData = dataList[position].data as ProjectTimeline
                val langHolder = holder as StepsLandViewHolder
                if (listData.timeLines[0].sections[0].values.percentage == 100.0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        langHolder.binding.headerIndicator.background =
                            context.getDrawable(R.drawable.ic_progress_complete)
                        langHolder.binding.ivFirst.background =
                            context.getDrawable(R.drawable.ic_progress_complete)
                        langHolder.binding.getOtpButton.background =
                            context.getDrawable(R.drawable.button_bg)
                        ImageViewCompat.setImageTintList(
                            langHolder.binding.stepView,
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
                        );
                    }

                }
            }
            TYPE_RERA -> {
                val listData = dataList[position].data as ProjectTimeline
                val listHolder = holder as StepsReraViewHolder

                listHolder.binding.textView7.text = showHTMLText(
                    String.format(
                        context.getString(R.string.tv_receipt),
                        "View Details"
                    )
                )
                var reraNumber = ""
                val mSize = listData.reraDetails.reraNumbers.size
                for ((index, item) in listData.reraDetails.reraNumbers.withIndex()) {
                    reraNumber += item
                    if (index + 1 != mSize) {
                        reraNumber += "\n"
                    }
                }
                listHolder.binding.textView10.text = reraNumber
                listHolder.binding.textView7.setOnClickListener {
                    //navigate to weblink.
                    itemInterface.onClickReraDetails(listData.timeLines[0].sections[0].values.webLink)

                }
                listHolder.binding.imageView.setOnClickListener {
                    getToolTip(listData.timeLines[0].sections[0].values.toolTipDetails).showAlignBottom(
                        listHolder.binding.imageView
                    )
                }

            }
            TYPE_LIST -> {
                var isOneProgress: Boolean = false
                var isAllDisable: Boolean = false

                val listData = dataList[position].data as ProjectTimeline
                val listHolder = holder as StepsListHolder

                val mDisableCount =
                    listData.timeLines.filter { it.sections[0].values.percentage == 0.0 }

                if (mDisableCount.size == listData.timeLines.size) {
                    isAllDisable = true
                }

                listHolder.binding.textHeader.text = listData.timeLineSectionHeading
                val stepsList = ArrayList<StepsModel>()
                for (item in listData.timeLines) {
                    when (item.sections[0].values.percentage) {
                        0.0 -> {
                            stepsList.add(StepsModel(StepsAdapter.TYPE_INSTART, item))
                        }
                        in 1.0..99.99 -> {
                            isOneProgress = true
                            stepsList.add(StepsModel(StepsAdapter.TYPE_INPROGRESS, item))
                        }
                        else -> {
                            stepsList.add(StepsModel(StepsAdapter.TYPE_COMPLETED, item))
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isOneProgress) {
                        listHolder.binding.headerIndicator.background =
                            context.getDrawable(R.drawable.ic_in_progress)
                    }
                    if (isAllDisable) {
                        listHolder.binding.headerIndicator.background =
                            context.getDrawable(R.drawable.ic_inprogress_bg)
                    }
                }

                listHolder.binding.stepsList.layoutManager = LinearLayoutManager(context)
                listHolder.binding.stepsList.adapter =
                    StepsAdapter(context, stepsList, itemInterface)
            }


        }
    }


    override fun getItemCount() = dataList.size

    inner class HeaderHolder(var binding: ItemTimelineHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class StepsListHolder(var binding: ItemTimelineDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class StepsReraViewHolder(var binding: ItemTimelineStepReraBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class StepsLandViewHolder(var binding: ItemTimelineLandBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface TimelineInterface {
        fun onClickVDetails(name: String, url: String)
        fun onClickReraDetails(url: String)
    }

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
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

}
