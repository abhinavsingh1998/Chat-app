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
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentReceiptBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.portfolio.adapters.ReceiptListAdapter
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.response.bookingjourney.Data
import com.emproto.networklayer.response.bookingjourney.Payment
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.portfolio.ivdetails.InvestmentInformation
import com.example.portfolioui.adapters.BookingJourneyAdapter
import com.example.portfolioui.databinding.DialogHandoverDetailsBinding
import com.example.portfolioui.databinding.DialogPendingPaymentBinding
import com.example.portfolioui.databinding.DialogRegistrationDetailsBinding
import com.example.portfolioui.databinding.FragmentBookingjourneyBinding
import com.example.portfolioui.models.BookingModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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
    var isWritePermissonGranted: Boolean = false
    var base64Data: String = ""

    lateinit var dialogRegistrationDetailsBinding: DialogRegistrationDetailsBinding
    lateinit var registrationDialog: CustomDialog

    lateinit var dialogPendingPayment: DialogPendingPaymentBinding
    lateinit var pendingPaymentDialog: CustomDialog

    lateinit var dialogHandoverDetailsBinding: DialogHandoverDetailsBinding
    lateinit var handoverDialog: CustomDialog

    lateinit var allReceiptDialog: FragmentReceiptBinding
    lateinit var bottomSheetDialog: BottomSheetDialog

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

        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).hideBottomNavigation()

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadPermissonGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                        ?: isReadPermissonGranted
                isWritePermissonGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    ?: isWritePermissonGranted

                if (isReadPermissonGranted && isWritePermissonGranted) {
                    openPdf(base64Data)
                }
            }

        dialogRegistrationDetailsBinding = DialogRegistrationDetailsBinding.inflate(layoutInflater)
        registrationDialog = CustomDialog(requireContext())
        registrationDialog.setContentView(dialogRegistrationDetailsBinding.root)

        dialogPendingPayment = DialogPendingPaymentBinding.inflate(layoutInflater)
        pendingPaymentDialog = CustomDialog(requireContext())
        pendingPaymentDialog.setContentView(dialogPendingPayment.root)

        dialogHandoverDetailsBinding = DialogHandoverDetailsBinding.inflate(layoutInflater)
        handoverDialog = CustomDialog(requireContext())
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

        allReceiptDialog = FragmentReceiptBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        bottomSheetDialog.setContentView(allReceiptDialog.root)
        allReceiptDialog.actionClose.setOnClickListener {
            bottomSheetDialog.dismiss()
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
        val bookingList = ArrayList<BookingModel>()


        val headerData = portfolioviewmodel.getBookingHeader()
        bookingList.add(
            BookingModel(
                BookingJourneyAdapter.TYPE_HEADER,
                headerData
            )
        )

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
                        dialogPendingPayment.tvPendingAmount.text =
                            "₹ ${Utility.convertTo(payment.pendingAmount)}"

                        if (payment.pendingAmount == 0.0) {
                            dialogPendingPayment.tvPaidAmount.visibility = View.GONE
                            dialogPendingPayment.textView14.visibility = View.GONE
                        } else {
                            dialogPendingPayment.tvPaidAmount.text =
                                "₹ ${Utility.convertTo(payment.paidAmount)}"
                        }
                        dialogPendingPayment.tvMilestoneName.text = payment.paymentMilestone
                        if (payment.targetDate != null)
                            dialogPendingPayment.tvDueDate.text =
                                "Due date: ${Utility.parseDateFromUtc(payment.targetDate)}"
                        pendingPaymentDialog.show()

                    }

                    override fun onClickViewDocument(path: String) {
                        getDocumentData(path)
                    }

                    override fun onClickHandoverDetails(date: String) {
                        dialogHandoverDetailsBinding.tvHandoverDate.text =
                            Utility.parseDateFromUtcToMMYYYY(date)
                        handoverDialog.show()
                    }

                    override fun onClickRegistrationDetails(date: String, number: String) {
                        dialogRegistrationDetailsBinding.tvRegistrationDate.text =
                            Utility.parseDateFromUtc(date)
                        dialogRegistrationDetailsBinding.tvRegistrationNo.text = number
                        registrationDialog.show()
                    }

                    override fun onClickAllReceipt() {
                        if (data.paymentHistory.isEmpty()) {
                            allReceiptDialog.errorText.visibility = View.VISIBLE
                        } else {
                            allReceiptDialog.receiptList.layoutManager =
                                LinearLayoutManager(requireContext())
                            allReceiptDialog.receiptList.adapter = ReceiptListAdapter(
                                requireContext(),
                                data.paymentHistory,
                                object : ReceiptListAdapter.OnPaymentItemClickListener {
                                    override fun onAccountsPaymentItemClick(
                                        path: String
                                    ) {
                                        //download the receipt
                                        bottomSheetDialog.dismiss()
                                        getDocumentData(path)
                                    }

                                })
                        }
                        bottomSheetDialog.show()

                    }

                    override fun loadError(message: String) {
                        (requireActivity() as HomeActivity).showErrorToast(message)
                    }

                    override fun facilityManagment(plotId: String, projectId: String) {
                        manageMyLand()
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

        isWritePermissonGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (!isReadPermissonGranted || !isWritePermissonGranted) {
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
        if (file != null) {
            val path = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + Constants.DOT_PROVIDER,
                file!!
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(path, Constants.APPLICATION_PDF)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(Constants.ERROR_OPEN_PDF, e.localizedMessage)
            }
        } else {
            (requireActivity() as HomeActivity).showErrorToast(Constants.SOMETHING_WENT_WRONG)
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

    fun manageMyLand() {
        (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)

    }

}