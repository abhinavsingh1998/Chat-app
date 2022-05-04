package com.emproto.hoabl.feature.portfolio.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentFinancialSummaryBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.portfolio.adapters.ExistingUsersAdapter
import com.emproto.hoabl.feature.portfolio.models.PortfolioModel
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.portfolio.PortfolioData
import javax.inject.Inject


class PortfolioExistingUsersFragment : BaseFragment() {

    private lateinit var binding: FragmentFinancialSummaryBinding
    private lateinit var adapter: ExistingUsersAdapter

    @Inject
    lateinit var portfolioFactory: PortfolioFactory
    lateinit var portfolioviewmodel: PortfolioViewModel

    val onItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.tv_manage_projects -> {
                    val portfolioSpecificProjectView = PortfolioSpecificProjectView()
                    (requireActivity() as HomeActivity).replaceFragment(
                        portfolioSpecificProjectView.javaClass,
                        "",
                        true,
                        null,
                        null,
                        0,
                        false
                    )
                }
            }
        }

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
        portfolioviewmodel.getPortfolioDashboard()
            .observe(viewLifecycleOwner, object : Observer<BaseResponse<PortfolioData>> {
                override fun onChanged(t: BaseResponse<PortfolioData>) {
                    when (t.status) {
                        Status.LOADING -> {
                            binding.loader.show()
                            binding.financialRecycler.hide()
                        }
                        Status.SUCCESS -> {
                            binding.loader.hide()
                            binding.financialRecycler.show()
                            t.data?.let {
                                val list = ArrayList<PortfolioModel>()
                                list.add(PortfolioModel(ExistingUsersAdapter.TYPE_HEADER, null))
                                list.add(
                                    PortfolioModel(
                                        ExistingUsersAdapter.TYPE_SUMMARY_COMPLETED,
                                        it.data.summary.completed
                                    )
                                )
                                list.add(
                                    PortfolioModel(
                                        ExistingUsersAdapter.TYPE_SUMMARY_ONGOING,
                                        it.data.summary.ongoing
                                    )
                                )
                                list.add(
                                    PortfolioModel(
                                        ExistingUsersAdapter.TYPE_COMPLETED_INVESTMENT,
                                        it.data.projects.filter { it.isCompleted }
                                    )
                                )
                                list.add(
                                    PortfolioModel(
                                        ExistingUsersAdapter.TYPE_ONGOING_INVESTMENT,
                                        it.data.projects.filter { !it.isCompleted }
                                    )
                                )
                                list.add(PortfolioModel(ExistingUsersAdapter.TYPE_NUDGE_CARD))
                                list.add(PortfolioModel(ExistingUsersAdapter.TYPE_WATCHLIST))
                                list.add(PortfolioModel(ExistingUsersAdapter.TYPE_REFER))

                                adapter =
                                    ExistingUsersAdapter(
                                        requireActivity(),
                                        list,
                                        onItemClickListener
                                    )
                                binding.financialRecycler.adapter = adapter
                            }


                        }
                        Status.ERROR -> {
                            binding.loader.hide()
                            (requireActivity() as HomeActivity).showErrorToast(
                                t.message!!
                            )
                        }
                    }
                }

            })
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE
    }

    private fun setUpRecyclerView() {
        val list = ArrayList<PortfolioModel>()
        list.add(PortfolioModel(ExistingUsersAdapter.TYPE_HEADER))
        list.add(PortfolioModel(ExistingUsersAdapter.TYPE_SUMMARY_COMPLETED))
        list.add(PortfolioModel(ExistingUsersAdapter.TYPE_SUMMARY_ONGOING))
        list.add(PortfolioModel(ExistingUsersAdapter.TYPE_COMPLETED_INVESTMENT))
        list.add(PortfolioModel(ExistingUsersAdapter.TYPE_ONGOING_INVESTMENT))
        list.add(PortfolioModel(ExistingUsersAdapter.TYPE_NUDGE_CARD))
        list.add(PortfolioModel(ExistingUsersAdapter.TYPE_WATCHLIST))
        list.add(PortfolioModel(ExistingUsersAdapter.TYPE_REFER))
        adapter = ExistingUsersAdapter(requireActivity(), list, onItemClickListener)
        binding.financialRecycler.adapter = adapter
    }

}