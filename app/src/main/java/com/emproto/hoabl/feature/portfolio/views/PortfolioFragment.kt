package com.emproto.hoabl.feature.portfolio.views

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emproto.core.BaseFragment
import com.emproto.core.Utility
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPortfolioBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.fragments.ReferralDialog
import com.emproto.hoabl.feature.investment.views.CategoryListFragment
import com.emproto.hoabl.feature.investment.views.LandSkusFragment
import com.emproto.hoabl.feature.investment.views.ProjectDetailFragment
import com.emproto.hoabl.feature.portfolio.adapters.ExistingUsersPortfolioAdapter
import com.emproto.hoabl.feature.portfolio.models.PortfolioModel
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.portfolio.dashboard.PortfolioData
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectExtraDetails
import com.emproto.networklayer.response.watchlist.Data
import com.example.portfolioui.databinding.DailogLockPermissonBinding
import com.example.portfolioui.databinding.DialogAllowPinBinding
import okhttp3.ResponseBody
import java.io.File
import java.io.Serializable
import java.util.concurrent.Executor
import javax.inject.Inject


class PortfolioFragment : BaseFragment(), View.OnClickListener,
    ExistingUsersPortfolioAdapter.ExistingUserInterface {

    companion object {
        const val mRequestCode = 300
    }

    lateinit var binding: FragmentPortfolioBinding
    lateinit var keyguardManager: KeyguardManager
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioviewmodel: PortfolioViewModel

    @Inject
    lateinit var appPreference: AppPreference
    lateinit var pinPermissonDialog: DailogLockPermissonBinding
    lateinit var pinAllowDailog: DialogAllowPinBinding


    lateinit var pinDialog: Dialog
    lateinit var pinAllowD: Dialog
    var watchList = ArrayList<Data>()

    private lateinit var adapter: ExistingUsersPortfolioAdapter
    val permissionRequest: MutableList<String> = ArrayList()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    var isReadPermissonGranted: Boolean = false
    var oneTimeValidation = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        binding = FragmentPortfolioBinding.inflate(layoutInflater)
        portfolioviewmodel = ViewModelProvider(
            requireActivity(),
            portfolioFactory
        )[PortfolioViewModel::class.java]

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadPermissonGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissonGranted
            }
        //requestPermisson()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.isVisible =
            true

        initViews()

    }

    private fun initViews() {
        (requireActivity() as HomeActivity).showBottomNavigation()
        (requireActivity() as HomeActivity).hideBackArrow()

        pinPermissonDialog = DailogLockPermissonBinding.inflate(layoutInflater)
        pinAllowDailog = DialogAllowPinBinding.inflate(layoutInflater)

        pinPermissonDialog.tvActivate.setOnClickListener {
            //activate pin
            appPreference.activatePin(true)
            appPreference.savePinDialogStatus(true)
            pinDialog.dismiss()
            setUpAuthentication()
            //setUpUI(true)
        }

        pinPermissonDialog.tvDontallow.setOnClickListener {
            //dont show dialog again
            //setUpUI(true)
            //appPreference.savePinDialogStatus(true)
            pinDialog.dismiss()
            pinAllowD.show()

        }
        pinAllowDailog.tvActivate.setOnClickListener {
            setUpUI(true)
            appPreference.savePinDialogStatus(true)
            pinAllowD.dismiss()
        }

        pinDialog = Dialog(requireContext())
        pinAllowD = Dialog(requireContext())
        pinDialog.setContentView(pinPermissonDialog.root)
        pinDialog.setCancelable(false)

        pinAllowD.setContentView(pinAllowDailog.root)
        pinAllowD.setCancelable(false)

        if (appPreference.isPinDialogShown()) {
            // if dialog is shown already and pin is activated show pin screen.
            if (appPreference.getPinActivationStatus() && !oneTimeValidation) {
                setUpInitialUI()
                setUpAuthentication()
            } else {
                //normal flow
                setUpUI(true)
            }

        } else {
            //show pin permisson dialog
            pinDialog.show()
        }
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.btnExploreNewInvestmentProject.setOnClickListener(this)
    }

    private fun setUpInitialUI() {
        //setUpUI(false)
    }

    private fun setUpAuthentication() {
        executor = ContextCompat.getMainExecutor(this.requireContext())
        //Biometric dialog
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use Pattern")
            .build()

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        setUpKeyGuardManager()
                    } else if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                        setUpUI(true)
                    } else if (errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                        (requireActivity() as HomeActivity).onBackPressed()
                    } else {
                        setUpUI(true)
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    oneTimeValidation = true
                    setUpUI(true)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            setUpKeyGuardManager()
        } else {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    fun setUpKeyGuardManager() {
        keyguardManager =
            (activity as HomeActivity).getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val intent = keyguardManager.createConfirmDeviceCredentialIntent(
                "Hi,User",
                "Verify your security PIN/Pattern"
            )
            startActivityForResult(intent, mRequestCode)
        } else {

        }
    }

    private fun setUpUI(authenticated: Boolean = false) {
        fetchUserPortfolio(false)
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = true
            fetchUserPortfolio(true)
        }
    }

    private fun fetchUserPortfolio(refresh: Boolean) {
        portfolioviewmodel.getPortfolioDashboard(refresh)
            .observe(viewLifecycleOwner, Observer { it ->
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBaar.show()
                    }
                    Status.SUCCESS -> {
                        binding.refreshLayout.isRefreshing = false
                        binding.progressBaar.hide()
                        it.data?.let {
                            //load data in listview
                            binding.financialRecycler.show()
                            observePortFolioData(it)
                        }


                    }
                    Status.ERROR -> {
                        binding.refreshLayout.isRefreshing = false
                        binding.progressBaar.hide()
                        //show error dialog
                        if (it.message == "The current user is not an investor") {
                            binding.noUserView.show()
                            binding.portfolioTopImg.visibility = View.VISIBLE
                            binding.addYouProject.visibility = View.VISIBLE
                            binding.instriction.visibility = View.VISIBLE
                            binding.btnExploreNewInvestmentProject.visibility = View.VISIBLE
                        } else {
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                        }

                    }
                }
            })
    }

    private fun observePortFolioData(portfolioData: PortfolioData) {

        portfolioData.let {
            val list = ArrayList<PortfolioModel>()
            list.add(
                PortfolioModel(
                    ExistingUsersPortfolioAdapter.TYPE_HEADER,
                    null
                )
            )
            if (it.data.summary.completed.count > 0) {
                list.add(
                    PortfolioModel(
                        ExistingUsersPortfolioAdapter.TYPE_SUMMARY_COMPLETED,
                        it.data.summary.completed
                    )
                )
            }
            if (it.data.summary.ongoing.count > 0) {
                list.add(
                    PortfolioModel(
                        ExistingUsersPortfolioAdapter.TYPE_SUMMARY_ONGOING,
                        it.data.summary.ongoing
                    )
                )
            }
            list.add(
                PortfolioModel(
                    ExistingUsersPortfolioAdapter.TYPE_COMPLETED_INVESTMENT,
                    it.data.projects.filter { it.investment.isBookingComplete }
                )
            )
            list.add(
                PortfolioModel(
                    ExistingUsersPortfolioAdapter.TYPE_ONGOING_INVESTMENT,
                    it.data.projects.filter { !it.investment.isBookingComplete }
                )
            )
            list.add(
                PortfolioModel(
                    ExistingUsersPortfolioAdapter.TYPE_NUDGE_CARD
                )
            )

            if (it.data.watchlist != null && it.data.watchlist.isNotEmpty()) {
                watchList.clear()
                watchList.addAll(it.data.watchlist)
                list.add(
                    PortfolioModel(
                        ExistingUsersPortfolioAdapter.TYPE_WATCHLIST, it.data.watchlist
                    )
                )
            }
            list.add(
                PortfolioModel(
                    ExistingUsersPortfolioAdapter.TYPE_REFER
                )
            )

            binding.financialRecycler.layoutManager = LinearLayoutManager(requireActivity())
            adapter =
                ExistingUsersPortfolioAdapter(
                    requireActivity(),
                    list,
                    this@PortfolioFragment
                )
            binding.financialRecycler.adapter = adapter
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            mRequestCode -> {
                when (resultCode) {
                    RESULT_OK -> {
                        setUpUI(true)
                    }
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_explore_new_investment_project -> {
                (requireActivity() as HomeActivity).navigate(R.id.navigation_investment)
            }
        }
    }

    override fun manageProject(crmId: Int, projectId: Int, otherDetails: ProjectExtraDetails) {
        val portfolioSpecificProjectView = PortfolioSpecificProjectView()
        val arguments = Bundle()
        arguments.putInt("IVID", crmId)
        arguments.putInt("PID", projectId)
        portfolioSpecificProjectView.arguments = arguments
        portfolioviewmodel.setprojectAddress(otherDetails)
        (requireActivity() as HomeActivity).addFragment(portfolioSpecificProjectView, false)
    }

    override fun referNow() {
        val dialog = ReferralDialog()
        dialog.isCancelable = true
        dialog.show(parentFragmentManager, "Refrral card")
    }

    override fun seeAllWatchlist() {
        val list = CategoryListFragment()
        val bundle = Bundle()
        bundle.putString("Category", "Watchlist")
        bundle.putSerializable(
            "WatchlistData",
            watchList as Serializable
        )
        list.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(list, false)
    }

    override fun investNow() {
        (requireActivity() as HomeActivity).navigate(R.id.navigation_investment)
    }

    override fun onGoingDetails() {
        (requireActivity() as HomeActivity).addFragment(
            BookingjourneyFragment.newInstance(
                23,
                ""
            ), false
        )
    }

    override fun onClickofWatchlist(projectId: Int) {
        val bundle = Bundle()
        bundle.putInt("ProjectId", projectId)
        val fragment = ProjectDetailFragment()
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(
            fragment, false
        )
    }

    override fun onClickApplyNow(projectId: Int) {
        //open sku screen
        val fragment = LandSkusFragment()
        val bundle = Bundle()
        bundle.putInt("ProjectId", projectId)
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(fragment, false)
    }

    override fun onClickShare() {
        (requireActivity() as HomeActivity).share_app()
    }

    override fun dontMissoutCard() {
        val bundle = Bundle()
        bundle.putInt("ProjectId", 9)
        val fragment = ProjectDetailFragment()
        fragment.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(
            fragment, false
        )
    }

    private fun setUpRecyclerView() {
        val list = ArrayList<PortfolioModel>()
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_HEADER))
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_SUMMARY_COMPLETED))
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_SUMMARY_ONGOING))
//        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_COMPLETED_INVESTMENT))
//        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_ONGOING_INVESTMENT))
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_NUDGE_CARD))
        //list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_WATCHLIST))
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_REFER))
        adapter = ExistingUsersPortfolioAdapter(
            requireActivity(),
            list,
            this@PortfolioFragment
        )
        binding.financialRecycler.adapter = adapter
    }

    private fun requestPermisson() {
        isReadPermissonGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (!isReadPermissonGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }


}