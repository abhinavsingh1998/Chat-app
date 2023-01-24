package com.emproto.hoabl.feature.portfolio.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.DocumentsBottomSheetBinding
import com.emproto.hoabl.databinding.FragmentReceiptBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.portfolio.adapters.AllReceiptsBookingJourneyAdapter
import com.emproto.hoabl.feature.portfolio.adapters.ReceiptListAdapter
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.bookingjourney.Data
import com.emproto.networklayer.response.bookingjourney.Payment
import com.emproto.networklayer.response.bookingjourney.PaymentReceipt
import com.emproto.networklayer.response.enums.Status
import com.example.portfolioui.adapters.BookingJourneyAdapter
import com.example.portfolioui.databinding.DialogHandoverDetailsBinding
import com.example.portfolioui.databinding.DialogPendingPaymentBinding
import com.example.portfolioui.databinding.DialogRegistrationDetailsBinding
import com.example.portfolioui.databinding.FragmentBookingjourneyBinding
import com.example.portfolioui.models.BookingModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BookingJourneyFragment : BaseFragment(),
    AllReceiptsBookingJourneyAdapter.OnAllDocumentLabelClickListener {

    private lateinit var allPaymentReceiptList: ArrayList<PaymentReceipt>
    private var investedId: Int = 0
    private var customerGuidelineUrl: String = ""
    lateinit var mBinding: FragmentBookingjourneyBinding

    private val permissionRequest: MutableList<String> = ArrayList()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadPermissionGranted = false
    private var isWritePermissionGranted = false
    private var base64Data: String = ""

    lateinit var dialogRegistrationDetailsBinding: DialogRegistrationDetailsBinding
    lateinit var registrationDialog: CustomDialog
    private lateinit var documentBinding: DocumentsBottomSheetBinding
    private lateinit var docsBottomSheet: BottomSheetDialog

    lateinit var dialogPendingPayment: DialogPendingPaymentBinding
    lateinit var pendingPaymentDialog: CustomDialog

    lateinit var dialogHandoverDetailsBinding: DialogHandoverDetailsBinding
    lateinit var handoverDialog: CustomDialog

    lateinit var allReceiptDialog: FragmentReceiptBinding
    lateinit var bottomSheetDialog: BottomSheetDialog

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioViewModel: PortfolioViewModel

    @Inject
    lateinit var appPreference: AppPreference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentBookingjourneyBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        portfolioViewModel =
            ViewModelProvider(requireActivity(), portfolioFactory)[PortfolioViewModel::class.java]
        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).hideBottomNavigation()



        documentBinding = DocumentsBottomSheetBinding.inflate(layoutInflater)
        docsBottomSheet = BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)
        docsBottomSheet.setContentView(documentBinding.root)
        documentBinding.ivDocsClose.setOnClickListener {
            docsBottomSheet.dismiss()
        }
        initView()
        eventTrackingViewBookingJourney()
        arguments?.let {
            investedId = it.getInt(ARG_PARAM1)
            customerGuidelineUrl = it.getString(ARG_PARAM2) ?: ""
        }
        getBookingJourneyData(investedId)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allPaymentReceiptList = portfolioViewModel.getAllPaymentReceipt()

    }

    private fun eventTrackingViewBookingJourney() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.VIEWBOOKINGJOURNEY
        )
    }

    private fun initView() {
        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).hideBottomNavigation()

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadPermissionGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                        ?: isReadPermissionGranted
                isWritePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    ?: isWritePermissionGranted

                if (isReadPermissionGranted && isWritePermissionGranted) {
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
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.setContentView(allReceiptDialog.root)
        allReceiptDialog.actionClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        allReceiptDialog.tvViewAllReceipts.setOnClickListener {
            portfolioViewModel.savePaymentReceipt(allPaymentReceiptList)

            portfolioViewModel.savePaymentReceipt(allPaymentReceiptList)
            docsBottomSheet.show()
            documentBinding.rvDocsItemRecycler.layoutManager =
                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            documentBinding.rvDocsItemRecycler.adapter =
                AllReceiptsBookingJourneyAdapter(context, allPaymentReceiptList, this)

        }
    }

    private fun getBookingJourneyData(investedId: Int) {
        portfolioViewModel.getBookingJourney(investedId)
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                        mBinding.loader.show()
                    }
                    Status.SUCCESS -> {
                        mBinding.loader.hide()
                        loadBookingJourneyData(it.data!!.data)
                        if (it.data!!.data.bookingJourney.paymentReceipts != null && it.data!!.data.bookingJourney.paymentReceipts is List<PaymentReceipt>) {
                            allPaymentReceiptList =
                                it.data!!.data.bookingJourney.paymentReceipts as java.util.ArrayList<PaymentReceipt>
                        }

//
                    }
                    Status.ERROR -> {
                        mBinding.loader.hide()
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                    }
                }
            }
    }

    private fun getPaymentReceiptData(data: Data) {
        if (data.bookingJourney.paymentReceipts != null && data.bookingJourney.paymentReceipts is List<PaymentReceipt>) {
            allPaymentReceiptList =
                data.bookingJourney.paymentReceipts as java.util.ArrayList<PaymentReceipt>
            portfolioViewModel.savePaymentReceipt(allPaymentReceiptList)
        }

    }

    private fun loadBookingJourneyData(data1: Data) {
        val data = data1.bookingJourney
        val bookingList = ArrayList<BookingModel>()


        val headerData = portfolioViewModel.getBookingHeader()
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
        bookingList.add(BookingModel(BookingJourneyAdapter.TYPE_DISCLAIMER))
        mBinding.bookingjourneyList.layoutManager = LinearLayoutManager(requireContext())
        mBinding.bookingjourneyList.adapter =
            BookingJourneyAdapter(
                requireContext(),
                bookingList,
                customerGuidelineUrl,
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
                        "₹ ${Utility.convertTo(payment.pendingAmount)}".also {
                            dialogPendingPayment.tvPendingAmount.text = it
                        }

//                        if (payment.paidAmount == 0.0) {
//                            dialogPendingPayment.tvPaidAmount.visibility = View.GONE
//                            dialogPendingPayment.textView14.visibility = View.GONE
//                        } else {
                        "₹ ${Utility.convertTo(payment.paidAmount)}".also {
                            dialogPendingPayment.tvPaidAmount.text = it
                        }
                        //}
                        dialogPendingPayment.tvMilestoneName.text = payment.paymentMilestone
                        if (payment.targetDate != null)
                            "Due date: ${Utility.parseDateFromUtc(payment.targetDate)}".also {
                                dialogPendingPayment.tvDueDate.text = it
                            }
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

                    override fun onclickCustomerGuidline(url: String) {
                        val intent = Intent()
                        intent.setDataAndType(Uri.parse(url), "application/pdf");
                        startActivity(intent)
                    }

                }
            )
        mBinding.bookingjourneyList.setItemViewCacheSize(10)
        mBinding.bookingjourneyList.setHasFixedSize(true)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Int, param2: String?) =
            BookingJourneyFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun requestPermission(base64: String) {
        isReadPermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isWritePermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (!isReadPermissionGranted || !isWritePermissionGranted) {
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
                file
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(path, Constants.APPLICATION_PDF)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.e(Constants.ERROR_OPEN_PDF, it) }
            }
        } else {
            (requireActivity() as HomeActivity).showErrorToast(Constants.SOMETHING_WENT_WRONG)
        }
    }

    fun getDocumentData(path: String) {
        portfolioViewModel.downloadDocument(path)
            .observe(
                viewLifecycleOwner
            ) {
                when (it.status) {
                    Status.LOADING -> {
                        mBinding.loader.show()
                    }
                    Status.SUCCESS -> {
                        mBinding.loader.hide()
                        requestPermission(it.data!!.data)
                    }
                    Status.ERROR -> {
                        mBinding.loader.hide()
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                    }
                }
            }
    }

    fun manageMyLand() {
        (requireActivity() as HomeActivity).navigate(R.id.navigation_promises)
    }

    override fun onViewClick(
        paymentReceiptList: ArrayList<PaymentReceipt>,
        view: View,
        position: Int,
        name: String,
        path: String?
    ) {
        docsBottomSheet.dismiss()
        openDocumentScreen(name, path.toString())
    }

    private fun openDocumentScreen(name: String, path: String) {
        val strings = name.split(".")
        if (strings.size > 1) {
            if (strings[1] == Constants.PNG_SMALL || strings[1] == Constants.JPG_SMALL) {
                //open image loading screen
                openDocument(name, path)
            } else if (strings[1] == Constants.PDF) {
                getDocumentData(path)
            } else {
                getDocumentData(path)
            }
        }
    }


    private fun openDocument(name: String, path: String) {
        (requireActivity() as HomeActivity).addFragment(
            DocViewerFragment.newInstance(true, name, path),
            true
        )
    }

}