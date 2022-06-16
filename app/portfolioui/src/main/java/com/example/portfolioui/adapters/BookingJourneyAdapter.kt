package com.example.portfolioui.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Utility
import com.emproto.networklayer.response.bookingjourney.*
import com.example.portfolioui.R
import com.example.portfolioui.databinding.*
import com.example.portfolioui.models.BookingModel
import com.example.portfolioui.models.BookingStepsModel
import com.example.portfolioui.models.StepsModel

class BookingJourneyAdapter(
    var context: Context,
    val dataList: ArrayList<BookingModel>,
    val itemInterface: TimelineInterface
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0

        const val TRANSACTION = 1
        const val DOCUMENTATION = 2
        const val PAYMENTS = 3
        const val OWNERSHIP = 4
        const val POSSESSION = 5
        const val FACILITY = 6

        const val TYPE_DISCLAIMER = 7

        //constant for screen
        const val VIEW_DETAILS = "View Details"
        const val VIEW_RECEIPT = "View Receipt"
        const val VIEW_ALLOTMENT_LETTER = "View Allotment Letter"
        const val VIEW = "View"
        const val VIEW_POA = "View POA Agreement"

        const val PATH_ERROR = "No Path Found For Document!!"


    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_HEADER -> {
                val view = ItemBookingHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HeaderHolder(view)
            }

            OWNERSHIP -> {
                val view =
                    ItemBookingJourneyOwnershipBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return OwnershipHolder(view)
            }

            POSSESSION -> {
                val view =
                    ItemBookingJourneyOwnershipBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return OwnershipHolder(view)
            }
            FACILITY -> {
                val view = ItemFacilityBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                return FacilityHolder(view)
            }

            else -> {
                val view =
                    ItemBookingJourneyBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return StepsListHolder(view)
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (dataList[position].viewType) {
            TYPE_HEADER -> {
                val header_holder = holder as HeaderHolder
                val data = dataList[header_holder.layoutPosition].data as Investment
                header_holder.binding.tvOwner.text = data.owners
                header_holder.binding.tvId.text = "Hoabl/${data.inventoryId}"
                header_holder.binding.tvProjectName.text = data.extraDetails.launchName
                header_holder.binding.tvLocation.text =
                    data.extraDetails.address.city + "," + data.extraDetails.address.state
                header_holder.binding.tvProgress.text = data.bookingStatus


            }
            TRANSACTION -> {
                val listHolder = holder as StepsListHolder
                val data = dataList[listHolder.adapterPosition].data as Transaction
                listHolder.binding.textHeader.text = "TRANSACTION"
                listHolder.binding.stepsList.layoutManager = LinearLayoutManager(context)
                val list = buildTransactionData(data)
                listHolder.binding.stepsList.adapter =
                    BookingStepsAdapter(context, list.first, itemInterface)
                if (list.second) {
                    listHolder.binding.headerIndicator.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                }
            }
            DOCUMENTATION -> {
                val listHolder = holder as StepsListHolder
                val data = dataList[listHolder.adapterPosition].data as Documentation
                listHolder.binding.textHeader.text = "DOCUMENTATION"
                listHolder.binding.stepsList.layoutManager = LinearLayoutManager(context)
                val list = buildDocumentationData(data)
                listHolder.binding.stepsList.adapter =
                    BookingStepsAdapter(context, list.first, itemInterface)
                if (list.second) {
                    listHolder.binding.headerIndicator.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                }
            }
            PAYMENTS -> {
                val listHolder = holder as StepsListHolder
                val data = dataList[listHolder.adapterPosition].data as List<Payment>
                val list = buildPaymentData(data)
                listHolder.binding.textHeader.text = "PAYMENTS"
                listHolder.binding.stepsList.layoutManager = LinearLayoutManager(context)
                listHolder.binding.stepsList.adapter =
                    BookingStepsAdapter(
                        context,
                        list.first,
                        itemInterface,
                        BookingStepsAdapter.SECTION_PAYMENT
                    )
                if (list.second) {
                    listHolder.binding.headerIndicator.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                    listHolder.binding.tvViewall.text =
                        showHTMLText(
                            String.format(
                                context.getString(R.string.tv_receipt),
                                "VIEW RECEIPTS"
                            )
                        )
                    listHolder.binding.tvViewall.setOnClickListener {
                        itemInterface.onClickAllReceipt()
                    }

                }
            }
            OWNERSHIP -> {
                val listHolder = holder as OwnershipHolder
                val list = dataList[listHolder.adapterPosition].data as Ownership
                listHolder.binding.textHeader.text = "OWNERSHIP"

                listHolder.binding.textHint.text =
                    showHTMLText(
                        String.format(
                            context.getString(R.string.tv_receipt),
                            "View"
                        )
                    )
                listHolder.binding.textHint2.text = showHTMLText(
                    String.format(
                        context.getString(R.string.tv_receipt),
                        "View"
                    )
                )

                if (list.documents.DOC != null) {
                    listHolder.binding.headerIndicator.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                    listHolder.binding.headerIndicator2.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                    listHolder.binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_in_progress))

                    //changing text color
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        listHolder.binding.tvFirst.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHeader.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHeader2.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHint.setTextColor(context.getColor(R.color.app_color))
                        listHolder.binding.textHint.setOnClickListener {
                            if (list.documents.DOC.path != null)
                                itemInterface.onClickViewDocument(list.documents.DOC.path!!)
                            else itemInterface.loadError(PATH_ERROR)
                        }
                    }


                }
                if (list.documents.SEVEN != null) {
                    listHolder.binding.headerIndicator.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                    listHolder.binding.headerIndicator2.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                    listHolder.binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_in_progress))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        listHolder.binding.tvSecond.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHeader.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHeader2.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHint2.setTextColor(context.getColor(R.color.app_color))
                        listHolder.binding.textHint2.setOnClickListener {
                            if (list.documents.SEVEN.path != null)
                                itemInterface.onClickViewDocument(list.documents.SEVEN.path!!)
                            else itemInterface.loadError(PATH_ERROR)

                        }

                    }


                }


            }
            POSSESSION -> {
                val listHolder = holder as OwnershipHolder
                val list = dataList[listHolder.adapterPosition].data as Possession
                listHolder.binding.textHeader.text = "Possession"
                listHolder.binding.tvFirst.text = "Handover Completed"
                listHolder.binding.tvSecond.text = "Customer Guidelines"
                listHolder.binding.textHint.text =
                    showHTMLText(
                        String.format(
                            context.getString(R.string.tv_receipt),
                            "View Details"
                        )
                    )
                listHolder.binding.textHint2.text = showHTMLText(
                    String.format(
                        context.getString(R.string.tv_receipt),
                        "View"
                    )
                )

                if (list.handover.handoverDate != null && Utility.compareDates(list.handover.handoverDate)) {
                    listHolder.binding.headerIndicator.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                    listHolder.binding.headerIndicator2.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                    listHolder.binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_in_progress))

                    //changing text color
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        listHolder.binding.tvFirst.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHeader.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHeader2.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHint.setTextColor(context.getColor(R.color.app_color))
                        listHolder.binding.textHint.setOnClickListener {
                            itemInterface.onClickHandoverDetails(list.handover.handoverDate)
                        }
                    }


                }
                if (list.handover.guidelines != null) {
                    listHolder.binding.headerIndicator.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                    listHolder.binding.headerIndicator2.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                    listHolder.binding.ivSecond.setImageDrawable(context.getDrawable(R.drawable.ic_in_progress))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        listHolder.binding.tvSecond.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHeader.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHeader2.setTextColor(context.getColor(R.color.text_color))
                        listHolder.binding.textHint2.setTextColor(context.getColor(R.color.app_color))
                        listHolder.binding.textHint2.setOnClickListener {
                            if (list.handover.guidelines.path != null) {
                                itemInterface.onClickViewDocument(list.handover.guidelines.path!!)
                            } else {
                                itemInterface.loadError(PATH_ERROR)
                            }

                        }
                    }


                }

            }
            FACILITY -> {
                val listHolder = holder as FacilityHolder
                val list = dataList[listHolder.adapterPosition].data as Facility
                listHolder.binding.textHeader.text = "Land Management"

                if (list.isFacilityVisible) {
                    listHolder.binding.headerIndicator.background =
                        context.getDrawable(R.drawable.ic_in_progress)
                    listHolder.binding.ivFirst.setImageDrawable(context.getDrawable(R.drawable.ic_in_progress))
                    listHolder.binding.tvFirst.setTextColor(context.getColor(R.color.text_color))
                    listHolder.binding.textHeader.setTextColor(context.getColor(R.color.text_color))

                } else {

                }


            }

        }
    }


    override fun getItemCount() = dataList.size

    inner class HeaderHolder(var binding: ItemBookingHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class StepsListHolder(var binding: ItemBookingJourneyBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class OwnershipHolder(var binding: ItemBookingJourneyOwnershipBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class FacilityHolder(var binding: ItemFacilityBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface TimelineInterface {
        fun onClickItem(position: Int)
        fun viewDetails(position: Int, data: String)
        fun onClickPendingCardDetails(payment: Payment)
        fun onClickViewDocument(path: String)
        fun onClickHandoverDetails(date: String)
        fun onClickRegistrationDetails(date: String, number: String)
        fun onClickAllReceipt()
        fun loadError(message: String)

    }

    private fun buildTransactionData(data: Transaction): Pair<List<BookingStepsModel>, Boolean> {
        val list = ArrayList<BookingStepsModel>()
        var anyInProgress = false
        if (data.application.isApplicationDone) {
            anyInProgress = true
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_COMPLETED,
                    "Application",
                    data.application.milestoneName ?: "",
                    ""
                )
            )
        } else {
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_INPROGRESS,
                    "Application",
                    data.application.milestoneName ?: "",
                    ""
                )
            )
        }
        if (Utility.compareDates(data.allotment.allotmentDate)) {
            anyInProgress = true
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_COMPLETED,
                    "Allotment",
                    "Plot Alloted",
                    VIEW_ALLOTMENT_LETTER,
                    data.allotment.allotmentLetter,
                    data.allotment.allotmentLetter == null
                )
            )
        } else {
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_INPROGRESS,
                    "Allotment",
                    "Plot Alloted",
                    VIEW_ALLOTMENT_LETTER
                )
            )
        }
        return Pair(list, anyInProgress)

    }

    private fun buildDocumentationData(data: Documentation): Pair<List<BookingStepsModel>, Boolean> {
        val list = ArrayList<BookingStepsModel>()
        var anyInProgress = false

        if (data.POA.isPOARequired) {
            if (data.POA.isPOAAlloted) {
                anyInProgress = true
                list.add(
                    BookingStepsModel(
                        BookingStepsAdapter.TYPE_COMPLETED, "Power of Attorney", "POA Assigned",
                        VIEW_POA,
                        data.POA.poaLetter
                    )
                )
            } else {
                list.add(
                    BookingStepsModel(
                        BookingStepsAdapter.TYPE_INPROGRESS, "Power of Attorney", "POA Assigned",
                        VIEW_POA
                    )
                )
            }
        }

        if (data.AFS.isAfsVisible) {
            if (data.AFS.afsLetter != null) {
                anyInProgress = true
                list.add(
                    BookingStepsModel(
                        BookingStepsAdapter.TYPE_COMPLETED, "Agreement for Sale", "Completed",
                        VIEW, data.AFS.afsLetter
                    )
                )
            } else {
                list.add(
                    BookingStepsModel(
                        BookingStepsAdapter.TYPE_INPROGRESS, "Agreement for Sale", "",
                        VIEW
                    )
                )
            }
        }


        if (data.Registration.isRegistrationScheduled &&
            data.Registration.registrationDate != null &&
            Utility.compareDates(
                data.Registration.registrationDate
            )
        ) {
            anyInProgress = true
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_COMPLETED, "Registration", "Completed",
                    VIEW_DETAILS, data.Registration
                )
            )
        } else {
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_INPROGRESS, "Registration", "",
                    VIEW_DETAILS
                )
            )
        }

        return Pair(list, anyInProgress)
    }

    private fun buildPaymentData(data: List<Payment>): Pair<List<BookingStepsModel>, Boolean> {
        val list = ArrayList<BookingStepsModel>()
        var anyInProgress = false

        for (item in data) {
            if (item.targetDate != null && !item.isPaymentDone && Utility.compareDates(item.targetDate)) {
                list.add(
                    BookingStepsModel(
                        BookingStepsAdapter.TYPE_INPROGRESS,
                        item.paymentMilestone,
                        "Payment Pending",
                        "View Details", item
                    )
                )

            } else {
                anyInProgress = true
                list.add(
                    BookingStepsModel(
                        BookingStepsAdapter.TYPE_COMPLETED,
                        item.paymentMilestone,
                        "Payment Completed",
                        "View Details", item
                    )
                )
            }
        }
        return Pair(list, anyInProgress)
    }

    fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }

}
