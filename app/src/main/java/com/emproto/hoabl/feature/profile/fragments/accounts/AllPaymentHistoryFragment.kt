package com.emproto.hoabl.feature.profile.fragments.accounts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Constants
import com.emproto.core.Utility
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.DocumentsBottomSheetBinding
import com.emproto.hoabl.databinding.FragmentPaymentHistoryBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.portfolio.views.DocViewerFragment
import com.emproto.hoabl.feature.profile.adapter.accounts.AllPaymentHistoryAdapter
import com.emproto.hoabl.feature.profile.adapter.accounts.AllReceiptsList
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.AccountsResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class AllPaymentHistoryFragment : Fragment(),
    AllReceiptsList.OnAllDocumentLabelClickListener {

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioviewmodel: PortfolioViewModel
    lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var profileFactory: ProfileFactory
    @Inject
    lateinit var appPreference: AppPreference


    lateinit var binding: FragmentPaymentHistoryBinding
    private var isReadPermissionGranted: Boolean = false
    private var isWritePermissionGranted: Boolean = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val permissionRequest: MutableList<String> = ArrayList()
    private var recieptsList = ArrayList<AccountsResponse.Data.PaymentReceipt>()
    private var paymentreciepts = ArrayList<AccountsResponse.Data.PaymentReceipt>()
    private lateinit var documentBinding: DocumentsBottomSheetBinding
    private lateinit var docsBottomSheet: BottomSheetDialog


    private var base64Data: String = ""
    private var  pdfName:String = ""
    val bundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)

        portfolioviewmodel =
            ViewModelProvider(requireActivity(), portfolioFactory)[PortfolioViewModel::class.java]
        profileViewModel =
            ViewModelProvider(requireActivity(), profileFactory)[ProfileViewModel::class.java]
        initClickListener()
        (requireActivity() as HomeActivity).hideBottomNavigation()
        callPermissionLauncher()
        eventTrackingPaymentHistorySeeAll()
        return binding.root
    }

    private fun eventTrackingPaymentHistorySeeAll() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.PAYMENTHISTORYSEEALL)
    }

    private fun callPermissionLauncher() {
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allPaymentList = profileViewModel.getAllPayment()
        binding.rvAllPaymentHistory.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        binding.rvAllPaymentHistory.adapter = AllPaymentHistoryAdapter(
            allPaymentList
        )
        initview()
    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
    }

    private fun initview() {
        profileViewModel.getAllPaymentReceipts().observe(viewLifecycleOwner) {
            paymentreciepts = it as ArrayList<AccountsResponse.Data.PaymentReceipt>
        }

        documentBinding = DocumentsBottomSheetBinding.inflate(layoutInflater)
        docsBottomSheet = BottomSheetDialog(this.requireContext(), R.style.BottomSheetDialogTheme)
        docsBottomSheet.setContentView(documentBinding.root)
        paymentreciepts.clear()
        documentBinding.ivDocsClose.setOnClickListener {
            docsBottomSheet.dismiss()
        }

        binding.seeAllReceipt.setOnClickListener(View.OnClickListener {
            addAllreciepts()
        })
    }

    private fun openDocument() {
        (requireActivity() as HomeActivity).addFragment(
            DocViewerFragment.newInstance(true, "Test.ong"),
            true
        )
    }

    private fun openDocumentScreen(name: String, path: String) {
        val strings = name.split(".")
        pdfName=name
        if (strings[1] == Constants.PNG_SMALL || strings[1] == Constants.JPG_SMALL) {
            //open image loading screen
            openDocument()
        } else if (strings[1] == Constants.PDF) {
            getDocumentData(path)
        } else {
            getDocumentData(path)
        }
    }

    private fun getDocumentData(path: String) {
        profileViewModel.downloadDocument(path)
            .observe(
                viewLifecycleOwner
            ) {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Status.SUCCESS -> {
                        binding.progressBar.hide()
                        requestPermission(it.data!!.data)
                    }
                    Status.ERROR -> {
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                    }
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
        val file = Utility.writeResponseBodyToDisk(stringBase64, pdfName)
        if (file != null) {
            val path = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + Constants.DOT_PROVIDER,
                file!!
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(path, Constants.APPLICATION_PDF)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(Constants.ERROR_OPEN_PDF, Constants.SOMETHING_WENT_WRONG)
            }
        } else {
            (requireActivity() as HomeActivity).showErrorToast(Constants.SOMETHING_WENT_WRONG)
        }
    }

   private fun addAllreciepts() {

        recieptsList.clear()
        for (i in 0 until paymentreciepts!!.size) {

                recieptsList.add(paymentreciepts[i])

        }
        if (recieptsList.size == 0) {
            Toast.makeText(
                requireContext(),
                "Connect with your relationship manager\nfor the receipt",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            docsBottomSheet.show()
            documentBinding.rvDocsItemRecycler.layoutManager =
                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            documentBinding.rvDocsItemRecycler.adapter =
                AllReceiptsList(context, recieptsList, this)
            documentBinding.tvDocumentsText.text= "All Reciepts"
        }
    }

    override fun onViewClick(
        paymentReceiptList: ArrayList<AccountsResponse.Data.PaymentReceipt>,
        view: View,
        position: Int,
        name: String,
        path: String?
    ) {
        docsBottomSheet.dismiss()
        openDocumentScreen(name, path.toString())
    }
}