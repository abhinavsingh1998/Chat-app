package com.example.portfolioui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.networklayer.response.bookingjourney.*
import com.example.portfolioui.databinding.*
import com.example.portfolioui.models.BookingModel
import com.example.portfolioui.models.BookingStepsModel
import com.example.portfolioui.models.StepsModel

class BookingJourneyAdapter(
    var context: Context,
    val dataList: ArrayList<BookingModel>,
    val itemInterface: TimelineInterface?
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (dataList[position].viewType) {
            TYPE_HEADER -> {
                val header_holder = holder as HeaderHolder
            }
            TRANSACTION -> {
                val listHolder = holder as StepsListHolder
                val data = dataList[listHolder.adapterPosition].data as Transaction
                listHolder.binding.textHeader.text = "TRANSACTION"
                listHolder.binding.stepsList.layoutManager = LinearLayoutManager(context)
                val list = buildTransactionData(data)
                listHolder.binding.stepsList.adapter =
                    BookingStepsAdapter(context, list, itemInterface)
            }
            DOCUMENTATION -> {
                val listHolder = holder as StepsListHolder
                val data = dataList[listHolder.adapterPosition].data as Documentation
                listHolder.binding.textHeader.text = "DOCUMENTATION"
                listHolder.binding.stepsList.layoutManager = LinearLayoutManager(context)
                val list = buildDocumentationData(data)
                listHolder.binding.stepsList.adapter =
                    BookingStepsAdapter(context, list, itemInterface)
            }
            PAYMENTS -> {
                val listHolder = holder as StepsListHolder
                val data = dataList[listHolder.adapterPosition].data as List<Payment>
                val list = buildPaymentData(data)
                listHolder.binding.textHeader.text = "PAYMENTS"
                listHolder.binding.stepsList.layoutManager = LinearLayoutManager(context)
                listHolder.binding.stepsList.adapter =
                    BookingStepsAdapter(context, list, itemInterface)
            }
            OWNERSHIP -> {
                val listHolder = holder as OwnershipHolder
                val list = dataList[listHolder.adapterPosition].data as Ownership
                listHolder.binding.textHeader.text = "OWNERSHIP"
//                listHolder.binding.stepsList.adapter =
//                    BookingStepsAdapter(context, list, itemInterface)
            }
            POSSESSION -> {
                val listHolder = holder as OwnershipHolder
                val list = dataList[listHolder.adapterPosition].data as Possession
                listHolder.binding.textHeader.text = "POSSESSION"
//                listHolder.binding.stepsList.adapter =
//                    BookingStepsAdapter(context, list, itemInterface)
            }
            FACILITY -> {
                val listHolder = holder as FacilityHolder
                val list = dataList[listHolder.adapterPosition].data as Facility
                listHolder.binding.textHeader.text = "FACILITY MANAGMENT"
//                listHolder.binding.stepsList.adapter =
//                    BookingStepsAdapter(context, list, itemInterface)
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

    }

    private fun buildTransactionData(data: Transaction): List<BookingStepsModel> {
        val list = ArrayList<BookingStepsModel>()
        if (data.application.isApplicationDone) {
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_COMPLETED,
                    "Application",
                    data.application.milestoneName,
                    VIEW_RECEIPT
                )
            )
        } else {
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_INPROGRESS,
                    "Application",
                    data.application.milestoneName,
                    VIEW_RECEIPT
                )
            )
        }
        if (data.allotment.receipt == null) {
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_INPROGRESS,
                    "Allotment",
                    "Plot Alloted",
                    VIEW_ALLOTMENT_LETTER
                )
            )
        } else {
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_COMPLETED,
                    "Allotment",
                    "Plot Alloted",
                    VIEW_ALLOTMENT_LETTER
                )
            )
        }
        return list

    }

    private fun buildDocumentationData(data: Documentation): List<BookingStepsModel> {
        val list = ArrayList<BookingStepsModel>()

        if (data.POA.isPOARequired) {
            if (data.POA.isPOAAlloted) {
                list.add(
                    BookingStepsModel(
                        BookingStepsAdapter.TYPE_COMPLETED, "Power of Attorney", "POA Assigned",
                        VIEW_POA
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
                list.add(
                    BookingStepsModel(
                        BookingStepsAdapter.TYPE_COMPLETED, "Agreement for Sale", "Completed",
                        VIEW
                    )
                )
            } else {
                list.add(
                    BookingStepsModel(
                        BookingStepsAdapter.TYPE_INPROGRESS, "Agreement for Sale", "Completed",
                        VIEW
                    )
                )
            }
        }


        if (data.Registration.isRegistrationScheduled) {
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_COMPLETED, "Registration", "Registration Scheduled",
                    VIEW
                )
            )
        } else {
            list.add(
                BookingStepsModel(
                    BookingStepsAdapter.TYPE_INPROGRESS, "Registration", "Registration Scheduled",
                    VIEW
                )
            )
        }





        return list
    }

    private fun buildPaymentData(data: List<Payment>): List<BookingStepsModel> {
        val list = ArrayList<BookingStepsModel>()
        for (item in data) {
            if (item.isPaymentDone) {
                list.add(
                    BookingStepsModel(
                        BookingStepsAdapter.TYPE_COMPLETED,
                        item.paymentMilestone,
                        "Payment Pending",
                        "View Details"
                    )
                )
            } else {
                list.add(
                    BookingStepsModel(
                        BookingStepsAdapter.TYPE_INPROGRESS,
                        item.paymentMilestone,
                        "Payment Pending",
                        "View Details"
                    )
                )
            }
        }
        return list
    }

}
