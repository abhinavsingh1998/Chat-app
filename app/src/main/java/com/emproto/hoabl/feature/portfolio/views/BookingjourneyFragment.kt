package com.emproto.hoabl.feature.portfolio.views

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.response.bookingjourney.Data
import com.emproto.networklayer.response.bookingjourney.Payment
import com.emproto.networklayer.response.enums.Status
import com.example.portfolioui.adapters.BookingJourneyAdapter
import com.example.portfolioui.databinding.DialogHandoverDetailsBinding
import com.example.portfolioui.databinding.DialogPendingPaymentBinding
import com.example.portfolioui.databinding.DialogRegistrationDetailsBinding
import com.example.portfolioui.databinding.FragmentBookingjourneyBinding
import com.example.portfolioui.models.BookingModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Bookingjourney.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingjourneyFragment : BaseFragment() {

    private var param1: Int = 0
    private var param2: String? = null
    lateinit var mBinding: FragmentBookingjourneyBinding

    val permissionRequest: MutableList<String> = ArrayList()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    var isReadPermissonGranted: Boolean = false
    var base64Data: String = ""

    lateinit var dialogRegistrationDetailsBinding: DialogRegistrationDetailsBinding
    lateinit var registrationDialog: Dialog

    lateinit var dialogPendingPayment: DialogPendingPaymentBinding
    lateinit var pendingPaymentDialog: CustomDialog

    lateinit var dialogHandoverDetailsBinding: DialogHandoverDetailsBinding
    lateinit var handoverDialog: Dialog

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioviewmodel: PortfolioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentBookingjourneyBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        portfolioviewmodel = ViewModelProvider(
            requireActivity(),
            portfolioFactory
        )[PortfolioViewModel::class.java]
        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        initView()
        getBookingJourneyData(param1)
        return mBinding.root
    }

    private fun initView() {

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadPermissonGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissonGranted
                if (isReadPermissonGranted) {
                    openPdf(base64Data)
                }
            }

        dialogRegistrationDetailsBinding = DialogRegistrationDetailsBinding.inflate(layoutInflater)
        registrationDialog = Dialog(requireContext())
        registrationDialog.setContentView(dialogRegistrationDetailsBinding.root)

        dialogPendingPayment = DialogPendingPaymentBinding.inflate(layoutInflater)
        pendingPaymentDialog = CustomDialog(requireContext())
        pendingPaymentDialog.setContentView(dialogPendingPayment.root)

        dialogHandoverDetailsBinding = DialogHandoverDetailsBinding.inflate(layoutInflater)
        handoverDialog = Dialog(requireContext())
        handoverDialog.setContentView(dialogHandoverDetailsBinding.root)

        dialogPendingPayment.actionOkay.setOnClickListener {
            pendingPaymentDialog.dismiss()
        }
        dialogHandoverDetailsBinding.actionOkay.setOnClickListener {
            handoverDialog.dismiss()
        }
        dialogRegistrationDetailsBinding.tvActivate.setOnClickListener {
            registrationDialog.dismiss()
        }
    }

    private fun getBookingJourneyData(investedId: Int) {
        portfolioviewmodel.getBookingJourney(investedId)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> {
                        mBinding.loader.show()
                    }
                    Status.SUCCESS -> {
                        mBinding.loader.hide()
                        loadBookingJourneyData(it.data!!.data)
                    }
                    Status.ERROR -> {
                        mBinding.loader.hide()
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                    }
                }
            })
    }

    fun loadBookingJourneyData(data1: Data) {
        val data = data1.bookingJourney
        data1.investment.extraDetails = portfolioviewmodel.getprojectAddress()
        val bookingList = ArrayList<BookingModel>()
        bookingList.add(BookingModel(BookingJourneyAdapter.TYPE_HEADER, data1.investment))
        bookingList.add(BookingModel(BookingJourneyAdapter.TRANSACTION, data.transaction))
        bookingList.add(BookingModel(BookingJourneyAdapter.DOCUMENTATION, data.documentation))
        bookingList.add(BookingModel(BookingJourneyAdapter.PAYMENTS, data.payments))
        bookingList.add(BookingModel(BookingJourneyAdapter.OWNERSHIP, data.ownership))
        bookingList.add(BookingModel(BookingJourneyAdapter.POSSESSION, data.possession))
        bookingList.add(BookingModel(BookingJourneyAdapter.FACILITY, data.facility))
        mBinding.bookingjourneyList.layoutManager = LinearLayoutManager(requireContext())
        mBinding.bookingjourneyList.adapter =
            BookingJourneyAdapter(
                requireContext(),
                bookingList,
                object : BookingJourneyAdapter.TimelineInterface {
                    override fun onClickItem(position: Int) {
                        TODO("Not yet implemented")
                    }

                    override fun viewDetails(position: Int, data: String) {
                        if (data.isNotEmpty()) {
                            getDocumentData(data)
                        }
                    }

                    override fun onClickPendingCardDetails(payment: Payment) {
                        dialogPendingPayment.tvPaidAmount.text =
                            "₹ ${Utility.convertTo(payment.paidAmount)}"
                        dialogPendingPayment.tvPendingAmount.text =
                            "${Utility.convertTo(payment.pendingAmount)}"
                        dialogPendingPayment.tvMilestoneName.text = payment.paymentMilestone
                        dialogPendingPayment.tvDueDate.text =
                            "Due date: ${Utility.parseDateFromUtc(payment.targetDate)}"
                        pendingPaymentDialog.showDialog()

                    }

                    override fun onClickViewDocument(path: String) {
                        getDocumentData(path)
                    }

                    override fun onClickHandoverDetails(date: String) {
                        dialogHandoverDetailsBinding.tvHandoverDate.text =
                            Utility.parseDateFromUtc(date)
                        handoverDialog.show()
                    }

                    override fun onClickRegistrationDetails(date: String, number: String) {
//                        dialogRegistrationDetailsBinding.tvRegistrationDate.text =
//                            Utility.parseDateFromUtc(date)
//                        dialogRegistrationDetailsBinding.tvRegistrationNo.text = number
                        registrationDialog.show()
                    }

                    override fun onClickAllReceipt() {
                        (requireActivity() as HomeActivity).addFragment(
                            ReceiptFragment.newInstance(
                                "",
                                ""
                            ), false
                        )
                    }

                    override fun loadError(message: String) {
                        (requireActivity() as HomeActivity).showErrorToast(message)
                    }

                })
        mBinding.bookingjourneyList.setItemViewCacheSize(10)
        mBinding.bookingjourneyList.setHasFixedSize(true)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Bookingjourney.
         */
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            BookingjourneyFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun requestPermisson(base64: String) {
        isReadPermissonGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (!isReadPermissonGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            openPdf(base64)
        }
        if (permissionRequest.isNotEmpty()) {
            base64Data = base64
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }

    private fun openPdf(stringBase64: String) {
        val file = Utility.writeResponseBodyToDisk(stringBase64)
        val path = FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName + ".provider",
            file!!
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(path, "application/pdf")
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("Error:openPdf: ", e.localizedMessage)
        }

    }

    fun getDocumentData(path: String) {
        portfolioviewmodel.downloadDocument(path)
            .observe(viewLifecycleOwner,
                androidx.lifecycle.Observer {
                    when (it.status) {
                        Status.LOADING -> {
                            mBinding.loader.show()
                        }
                        Status.SUCCESS -> {
                            mBinding.loader.hide()
                            requestPermisson(it.data!!.data)
                        }
                        Status.ERROR -> {
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                        }
                    }
                })
    }

}