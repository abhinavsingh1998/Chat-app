package com.emproto.hoabl.feature.portfolio.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.PortfolioViewModel
import com.emproto.hoabl.viewmodels.factory.PortfolioFactory
import com.emproto.networklayer.response.enums.Status
import com.example.portfolioui.adapters.TimelineAdapter
import com.example.portfolioui.databinding.FragmentProjectTimelineBinding
import com.example.portfolioui.models.TimelineHeaderData
import com.example.portfolioui.models.TimelineModel
import javax.inject.Inject

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectTimelineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectTimelineFragment : BaseFragment() {
    private var param1: Int = 0
    private var param2: String? = null
    lateinit var mBinding: FragmentProjectTimelineBinding

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
        // Inflate the layout for this fragment
        mBinding = FragmentProjectTimelineBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        portfolioviewmodel = ViewModelProvider(
            requireActivity(),
            portfolioFactory
        )[PortfolioViewModel::class.java]
        initView()
        initObserver()
        return mBinding.root
    }

    private fun initObserver() {
        portfolioviewmodel.getProjectTimeline(param1).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    mBinding.loader.show()
                    mBinding.rootView.hide()
                }
                Status.SUCCESS -> {
                    mBinding.loader.hide()
                    mBinding.rootView.show()
                    it.data?.let {
                        val timelineList = ArrayList<TimelineModel>()
                        val timelineHeaderData = TimelineHeaderData(
                            it.data.projectContent.launchName,
                            it.data.projectContent.address.city + " , " + it.data.projectContent.address.state,
                            ""
                        )
                        timelineList.add(
                            TimelineModel(
                                TimelineAdapter.TYPE_HEADER,
                                timelineHeaderData
                            )
                        )
                        for (item in it.data.projectContent.projectTimelines) {
                            if (item.priority == 1) {
                                item.reraDetails = it.data.projectContent.reraDetails
                                timelineList.add(TimelineModel(TimelineAdapter.TYPE_RERA, item))
                            } else if (item.timeLineSectionHeading == Constants.FACILITY_MANAGEMENT_TITLE || item.timeLineSectionHeading == "Land Management" || item.priority == 5) {
                                timelineList.add(TimelineModel(TimelineAdapter.TYPE_LAND, item))
                            } else
                                timelineList.add(TimelineModel(TimelineAdapter.TYPE_LIST, item))
                        }
                        mBinding.timelineList.layoutManager = LinearLayoutManager(requireContext())
                        mBinding.timelineList.adapter =
                            TimelineAdapter(
                                requireContext(),
                                timelineList,
                                object : TimelineAdapter.TimelineInterface {
                                    override fun onClickVDetails(name: String, url: String) {
                                        //open image viewer
                                        getUrl(name,url)
                                    }

                                    override fun onClickReraDetails(url: String) {
                                        if (url != null) {
                                            (requireActivity() as HomeActivity).addFragment(
                                                FmFragment.newInstance(
                                                    url,
                                                    ""
                                                ), true
                                            )

                                        } else {
                                            (requireActivity() as HomeActivity).showErrorToast(
                                            Constants.SOMETHING_WENT_WRONG
                                            )
                                        }
                                    }

                                    override fun onClickLand() {
                                        getLandManagment()
                                    }

                                })
                        mBinding.timelineList.setHasFixedSize(true)
                        mBinding.timelineList.setItemViewCacheSize(10)
                    }

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

    private fun initView() {
        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).showBackArrow()
        (requireActivity() as HomeActivity).hideBottomNavigation()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProjectTimelineFragment.
         */
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            ProjectTimelineFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getLandManagment() {
        portfolioviewmodel.getFacilityManagment()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> {
                        mBinding.loader.show()
                    }
                    Status.SUCCESS -> {
                        mBinding.loader.hide()
                        if (it.data!!.data.web_url != null) {
                            (requireActivity() as HomeActivity).addFragment(
                                FmFragment.newInstance(
                                    it.data!!.data.web_url!!,
                                    ""
                                ), true
                            )

                        } else {
                            (requireActivity() as HomeActivity).showErrorToast(
                               Constants.SOMETHING_WENT_WRONG
                            )
                        }

                    }
                    Status.ERROR -> {
                        mBinding.loader.hide()
                        (requireActivity() as HomeActivity).showErrorToast(
                            Constants.SOMETHING_WENT_WRONG
                        )
                    }
                }
            })
    }


    private fun getUrl(category: String, url: String) {
        val categoryType = when(category){
            "Project Design & Boundary" -> "4001"
            "Infrastructure & Amenities Development" -> "4003"
            "Plot Delivery" -> ""
            else -> ""
        }
        portfolioviewmodel.getProjectTimelineMedia(
            category = category, projectContentId = param1.toString()
        ).observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.LOADING -> {
                    mBinding.loader.show()
                }
                Status.SUCCESS -> {
                    mBinding.loader.hide()
                    it.data?.let {
                        if(it.data.isNotEmpty()){
                            (requireActivity() as HomeActivity).addFragment(
                                DocViewerFragment.newInstance(false, it.data[0].name, it.data[0].mediaContent.value.url),
                                true
                            )
                        }
                    }
                }
                Status.ERROR -> {
                    mBinding.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        Constants.SOMETHING_WENT_WRONG
                    )
                }
            }
        })
    }
}