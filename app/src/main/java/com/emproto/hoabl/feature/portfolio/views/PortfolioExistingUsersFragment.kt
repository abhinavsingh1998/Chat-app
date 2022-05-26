package com.emproto.hoabl.feature.portfolio.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentFinancialSummaryBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.fragments.ReferralDialog
import com.emproto.hoabl.feature.investment.views.CategoryListFragment
import com.emproto.hoabl.feature.portfolio.adapters.ExistingUsersPortfolioAdapter
import com.emproto.hoabl.feature.portfolio.models.PortfolioModel
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectExtraDetails
import com.emproto.networklayer.response.watchlist.Data
import java.io.Serializable
import javax.inject.Inject


class PortfolioExistingUsersFragment : BaseFragment(),
    ExistingUsersPortfolioAdapter.ExistingUserInterface {

    private lateinit var binding: FragmentFinancialSummaryBinding
    private lateinit var adapter: ExistingUsersPortfolioAdapter

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioviewmodel: PortfolioViewModel
    val list = ArrayList<PortfolioModel>()
    var watchList = ArrayList<Data>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        binding = FragmentFinancialSummaryBinding.inflate(layoutInflater)
        portfolioviewmodel = ViewModelProvider(
            requireActivity(),
            portfolioFactory
        )[PortfolioViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        addObserver()
    }

    private fun addObserver() {

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
                        this@PortfolioExistingUsersFragment
                    )
                binding.financialRecycler.adapter = adapter
                getWathclistData()
            }
        })

    }

    private fun setUpUI() {

    }

    private fun setUpRecyclerView() {
        val list = ArrayList<PortfolioModel>()
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_HEADER))
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_SUMMARY_COMPLETED))
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_SUMMARY_ONGOING))
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_COMPLETED_INVESTMENT))
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_ONGOING_INVESTMENT))
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_NUDGE_CARD))
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_WATCHLIST))
        list.add(PortfolioModel(ExistingUsersPortfolioAdapter.TYPE_REFER))
        adapter = ExistingUsersPortfolioAdapter(
            requireActivity(),
            list,
            this@PortfolioExistingUsersFragment
        )
        binding.financialRecycler.adapter = adapter
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

    override fun investNow() {
        TODO("Not yet implemented")
    }

    override fun onGoingDetails() {
        TODO("Not yet implemented")
    }

    override fun onClickofWatchlist(projectId: Int) {
        TODO("Not yet implemented")
    }

    override fun onClickApplyNow(projectId: Int) {
        TODO("Not yet implemented")
    }

}