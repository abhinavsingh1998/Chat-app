package com.emproto.hoabl.feature.portfolio.views

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentPortfolioBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.fragments.ReferralDialog
import com.emproto.hoabl.feature.investment.views.CategoryListFragment
import com.emproto.hoabl.feature.portfolio.adapters.ExistingUsersPortfolioAdapter
import com.emproto.hoabl.feature.portfolio.models.PortfolioModel
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectExtraDetails
import com.emproto.networklayer.response.watchlist.Data
import com.example.portfolioui.databinding.DailogLockPermissonBinding
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


    lateinit var pinDialog: Dialog
    val list = ArrayList<PortfolioModel>()
    var watchList = ArrayList<Data>()

    private lateinit var adapter: ExistingUsersPortfolioAdapter


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
        pinPermissonDialog.tvActivate.setOnClickListener {
            //activate pin
            appPreference.activatePin(true)
            appPreference.savePinDialogStatus(true)
            pinDialog.dismiss()
            setUpAuthentication()
            setUpUI(true)
        }

        pinPermissonDialog.tvDontallow.setOnClickListener {
            //dont show dialog again
            setUpUI(true)
            appPreference.savePinDialogStatus(true)
            pinDialog.dismiss()
        }

        pinDialog = Dialog(requireContext())
        pinDialog.setContentView(pinPermissonDialog.root)
        pinDialog.setCancelable(false)

        if (appPreference.isPinDialogShown()) {
            // if dialog is shown already and pin is activated show pin screen.
            if (appPreference.getPinActivationStatus()) {
                setUpInitialUI()
                setUpClickListeners()
                setUpAuthentication()
            } else {
                //normal flow
                setUpUI(true)
            }

        } else {
            //show pin permisson dialog
            pinDialog.show()
        }
    }

    private fun setUpClickListeners() {
        binding.btnExploreNewInvestmentProject.setOnClickListener(this)
    }

    private fun setUpInitialUI() {
        setUpUI(false)
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
                        //setUpUI(true)
                    } else {
                        //setUpUI(true)

                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(context, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                    setUpUI(true)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                    setUpUI(false)
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

        portfolioviewmodel.getPortfolioDashboard().observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBaar.show()
                }
                Status.SUCCESS -> {
                    binding.progressBaar.hide()
                    it.data?.let {
                        //load data in listview
                        binding.financialRecycler.show()
                        portfolioviewmodel.setPortfolioData(it)
                        observePortFolioData()
                    }


                }
                Status.ERROR -> {
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

    private fun observePortFolioData() {
        portfolioviewmodel.getPortfolioData().observe(viewLifecycleOwner, Observer {
            it.let {
                list.clear()
                list.add(
                    PortfolioModel(
                        ExistingUsersPortfolioAdapter.TYPE_HEADER,
                        null
                    )
                )
                list.add(
                    PortfolioModel(
                        ExistingUsersPortfolioAdapter.TYPE_SUMMARY_COMPLETED,
                        it.data.summary.completed
                    )
                )
                list.add(
                    PortfolioModel(
                        ExistingUsersPortfolioAdapter.TYPE_SUMMARY_ONGOING,
                        it.data.summary.ongoing
                    )
                )
                list.add(
                    PortfolioModel(
                        ExistingUsersPortfolioAdapter.TYPE_COMPLETED_INVESTMENT,
                        it.data.projects.filter { it.investment.isCompleted }
                    )
                )
                list.add(
                    PortfolioModel(
                        ExistingUsersPortfolioAdapter.TYPE_ONGOING_INVESTMENT,
                        it.data.projects.filter { !it.investment.isCompleted }
                    )
                )
                //fetch remaining data
                adapter =
                    ExistingUsersPortfolioAdapter(
                        requireActivity(),
                        list,
                        this@PortfolioFragment
                    )
                binding.financialRecycler.adapter = adapter
                getWathclistData()
            }
        })

    }

    private fun getWathclistData() {
        portfolioviewmodel.getWatchlist().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    list.add(
                        PortfolioModel(
                            ExistingUsersPortfolioAdapter.TYPE_NUDGE_CARD
                        )
                    )
                    it.data?.let {
                        watchList.clear()
                        watchList.addAll(it.data.filter { it.project != null })
                        list.add(
                            PortfolioModel(
                                ExistingUsersPortfolioAdapter.TYPE_WATCHLIST, watchList
                            )
                        )

                    }

                    list.add(
                        PortfolioModel(
                            ExistingUsersPortfolioAdapter.TYPE_REFER
                        )
                    )
                    adapter.notifyItemRangeChanged(4, 7)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            mRequestCode -> {
                when (resultCode) {
                    RESULT_OK -> setUpUI(true)
                    else -> setUpUI(false)
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
        bundle.putSerializable("WatchlistData", watchList as Serializable)
        list.arguments = bundle
        (requireActivity() as HomeActivity).addFragment(list, false)
    }


}