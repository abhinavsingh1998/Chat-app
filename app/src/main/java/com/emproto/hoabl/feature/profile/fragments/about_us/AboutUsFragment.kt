package com.emproto.hoabl.feature.profile.fragments.about_us

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentAboutUsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.profile.adapter.*
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.resourceManagment.AboutUs
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import javax.inject.Inject


class AboutUsFragment : Fragment(), GraphOptionsAdapter.GraphItemClicks {

    @Inject
    lateinit var factory: ProfileFactory
    lateinit var profileViewModel: ProfileViewModel

    lateinit var binding: FragmentAboutUsBinding
    private lateinit var philosophyAdapter: CorporatePhilosophyAdapter
    private lateinit var productAdapter: ProductAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var projectAdapter: AllProjectsAdapter
    private lateinit var statsOverViewAdapter: StatsOverViewAboutUsAdapter
    private var selectedItemPos = 0
    private val snapHelper = LinearSnapHelper()

    private var graphType = ""
    private var xAxisList = ArrayList<String>()

    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).hideBottomNavigation()
        (requireActivity() as HomeActivity).hideHeader()

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel =
            ViewModelProvider(requireActivity(), factory)[ProfileViewModel::class.java]

        initView()
        initObserver(refresh = false)
        initClickListener()
        return binding.root
    }

    private fun initView() {
        binding.refreshLayout.setOnRefreshListener {
            binding.loader.show()
            initObserver(refresh = true)
            binding.refreshLayout.isRefreshing = false

        }
    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
        binding.invest.setOnClickListener { (requireActivity() as HomeActivity).navigate(R.id.navigation_investment) }

        binding.tvQuery.setOnClickListener {
            val bundle = Bundle()
            val chatsFragment = ChatsFragment()
            chatsFragment.arguments = bundle
            (requireActivity() as HomeActivity).replaceFragment(
                chatsFragment.javaClass,
                "",
                true,
                bundle,
                null,
                0,
                true
            )
        }
    }

    override fun graphItemClicked(position: Int, itemView: View, line: View) {
        if (view?.isSelected == true) {
            line.visibility = View.VISIBLE
            line.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.app_color))
        } else {
            line.visibility = View.GONE
        }
    }

    private fun initObserver(refresh: Boolean) {
        profileViewModel.getAboutHoabl(5005).observe(
            viewLifecycleOwner
        ) {
            when (it?.status) {
                Status.LOADING -> {
                    binding.rootView.isVisible = false
                    binding.loader.show()
                }
                Status.SUCCESS -> {
                    binding.rootView.isVisible = true
                    binding.loader.hide()
                    val commonData = it.data?.data?.page?.aboutUs
                    val url = commonData?.foundersVision?.media?.value?.url
                    setDataAboutHoabl(commonData, url)
                    setRecyclerView(commonData)
                }
                else -> {}
            }
        }

        profileViewModel.getAllProjects(refresh).observe(
            viewLifecycleOwner
        ) {
            when (it?.status) {
                Status.LOADING -> {
                    binding.rootView.isVisible = false
                    binding.loader.show()
                }
                Status.SUCCESS -> {
                    binding.rootView.isVisible = true
                    binding.loader.hide()

                    for (i in 0 until it?.data?.data?.size!!) {
                        if (it?.data?.data?.get(i)?.isEscalationGraphActive!!) {
                            selectedItemPos = i
                            break
                        }
                    }
                    projectAdapter = AllProjectsAdapter(
                        requireActivity(),
                        it?.data?.data!!,
                        selectedItemPos,
                        object : AllProjectsAdapter.AllProjectsInterface {
                            override fun onClickItem(position: Int) {
                                val currentData = it?.data?.data!![position]
                                binding.tvXAxisLabel.text =
                                    currentData.generalInfoEscalationGraph.yAxisDisplayName
                                binding.tvYAxisLabel.text =
                                    currentData.generalInfoEscalationGraph.xAxisDisplayName
                                val graphData =
                                    currentData.generalInfoEscalationGraph.dataPoints.points
                                val lineValues = ArrayList<Entry>()
                                when (currentData.generalInfoEscalationGraph.dataPoints.dataPointType) {
                                    Constants.YEARLY -> {
                                        graphType = Constants.YEARLY
                                        for (item in graphData) {
                                            lineValues.add(
                                                Entry(
                                                    item.year.toFloat(),
                                                    item.value.toFloat()
                                                )
                                            )
                                        }
                                    }
                                    Constants.HALF_YEARLY -> {
                                        graphType = Constants.HALF_YEARLY
                                        for (i in 0 until currentData.generalInfoEscalationGraph.dataPoints.points.size) {
                                            val fmString =
                                                currentData.generalInfoEscalationGraph.dataPoints.points[i].halfYear.toString()
                                                    .substring(0, 3)
                                            val yearString =
                                                currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(
                                                    2,
                                                    4
                                                )
                                            val str = "$fmString-$yearString"
                                            xAxisList.add(str)
                                        }
                                        for ((index, item) in graphData.withIndex()) {
                                            lineValues.add(
                                                Entry(
                                                    index.toFloat(),
                                                    item.value.toFloat()
                                                )
                                            )
                                        }
                                    }
                                    Constants.QUATERLY -> {
                                        graphType = Constants.QUATERLY
                                        for (i in 0 until currentData.generalInfoEscalationGraph.dataPoints.points.size) {
                                            val fmString =
                                                currentData.generalInfoEscalationGraph.dataPoints.points[i].quater.toString()
                                                    .substring(0, 2)
                                            val yearString =
                                                currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(
                                                    2,
                                                    4
                                                )
                                            val str = "$fmString-$yearString"
                                            xAxisList.add(str)
                                        }
                                        for ((index, item) in graphData.withIndex()) {
                                            lineValues.add(
                                                Entry(
                                                    index.toFloat(),
                                                    item.value.toFloat()
                                                )
                                            )
                                        }
                                    }
                                    Constants.MONTHLY -> {
                                        graphType = Constants.MONTHLY
                                        for (i in 0 until currentData.generalInfoEscalationGraph.dataPoints.points.size) {
                                            val fmString =
                                                currentData.generalInfoEscalationGraph.dataPoints.points[i].month.toString()
                                                    .substring(0, 3)
                                            val yearString =
                                                currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(
                                                    2,
                                                    4
                                                )
                                            val str = "$fmString-$yearString"
                                            xAxisList.add(str)
                                        }
                                        for ((index, item) in graphData.withIndex()) {
                                            lineValues.add(
                                                Entry(
                                                    index.toFloat(),
                                                    item.value.toFloat()
                                                )
                                            )
                                        }
                                    }
                                }

                                val lineDataSet = LineDataSet(lineValues, "")
                                //We add features to our chart
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    lineDataSet.color =
                                        ContextCompat.getColor(context!!, R.color.green)
                                }

                                lineDataSet.valueTextSize = 12F
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    lineDataSet.fillColor =
                                        ContextCompat.getColor(context!!, R.color.green)
                                }
                                lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                                lineDataSet.setDrawCircles(false)
                                lineDataSet.setDrawValues(false)
                                val data = LineData(lineDataSet)

                                binding.ivPriceTrendsGraph.description.isEnabled = false
                                binding.ivPriceTrendsGraph.legend.isEnabled = false
                                binding.ivPriceTrendsGraph.axisLeft.setDrawGridLines(false)
                                binding.ivPriceTrendsGraph.setTouchEnabled(false)
                                binding.ivPriceTrendsGraph.setPinchZoom(false)
                                binding.ivPriceTrendsGraph.isDoubleTapToZoomEnabled = false
                                binding.ivPriceTrendsGraph.xAxis.setDrawGridLines(false)
                                binding.ivPriceTrendsGraph.xAxis.position =
                                    XAxis.XAxisPosition.BOTTOM
                                binding.ivPriceTrendsGraph.axisRight.setDrawGridLines(false)
                                binding.ivPriceTrendsGraph.axisRight.setDrawLabels(false)
                                binding.ivPriceTrendsGraph.axisRight.setDrawAxisLine(false)
                                binding.ivPriceTrendsGraph.xAxis.granularity = 1f
                                binding.ivPriceTrendsGraph.axisLeft.granularity = 1f
                                binding.ivPriceTrendsGraph.xAxis.valueFormatter = XAxisFormatter()
                                binding.ivPriceTrendsGraph.data = data
                                binding.ivPriceTrendsGraph.extraBottomOffset
                                binding.ivPriceTrendsGraph.animateXY(2000, 2000)
                            }
                        }
                    )

                    val currentData = it?.data?.data!![selectedItemPos]
                    binding.tvXAxisLabel.text =
                        currentData.generalInfoEscalationGraph.yAxisDisplayName
                    binding.tvYAxisLabel.text =
                        currentData.generalInfoEscalationGraph.xAxisDisplayName
                    val graphData = currentData.generalInfoEscalationGraph.dataPoints.points
                    val lineValues = ArrayList<Entry>()
                    when (currentData.generalInfoEscalationGraph.dataPoints.dataPointType) {
                        Constants.YEARLY -> {
                            graphType = Constants.YEARLY
                            for (item in graphData) {
                                lineValues.add(Entry(item.year.toFloat(), item.value.toFloat()))
                            }
                        }
                        Constants.HALF_YEARLY -> {
                            graphType = Constants.HALF_YEARLY
                            for (i in 0 until currentData.generalInfoEscalationGraph.dataPoints.points.size) {
                                val fmString =
                                    currentData.generalInfoEscalationGraph.dataPoints.points[i].halfYear.toString()
                                        .substring(0, 3)
                                val yearString =
                                    currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(
                                        2,
                                        4
                                    )
                                val str = "$fmString-$yearString"
                                xAxisList.add(str)
                            }
                            for ((index, item) in graphData.withIndex()) {
                                lineValues.add(Entry(index.toFloat(), item.value.toFloat()))
                            }
                        }
                        Constants.QUATERLY -> {
                            graphType = Constants.QUATERLY
                            for (i in 0 until currentData.generalInfoEscalationGraph.dataPoints.points.size) {
                                val fmString =
                                    currentData.generalInfoEscalationGraph.dataPoints.points[i].quater.toString()
                                        .substring(0, 2)
                                val yearString =
                                    currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(
                                        2,
                                        4
                                    )
                                val str = "$fmString-$yearString"
                                xAxisList.add(str)
                            }
                            for ((index, item) in graphData.withIndex()) {
                                lineValues.add(Entry(index.toFloat(), item.value.toFloat()))
                            }
                        }
                        Constants.MONTHLY -> {
                            graphType = Constants.MONTHLY
                            for (i in 0 until currentData.generalInfoEscalationGraph.dataPoints.points.size) {
                                val fmString =
                                    currentData.generalInfoEscalationGraph.dataPoints.points[i].month.toString()
                                        .substring(0, 3)
                                val yearString =
                                    currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(
                                        2,
                                        4
                                    )
                                val str = "$fmString-$yearString"
                                xAxisList.add(str)
                            }
                            for ((index, item) in graphData.withIndex()) {
                                lineValues.add(Entry(index.toFloat(), item.value.toFloat()))
                            }
                        }
                    }

                    val lineDataSet = LineDataSet(lineValues, "")
                    //We add features to our chart
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        lineDataSet.color = requireContext().getColor(R.color.green)
                    }

                    lineDataSet.valueTextSize = 12F
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        lineDataSet.fillColor = requireContext().getColor(R.color.green)
                    }
                    lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    lineDataSet.setDrawCircles(false)
                    lineDataSet.setDrawValues(false)
                    val data = LineData(lineDataSet)

                    binding.ivPriceTrendsGraph.description.isEnabled = false
                    binding.ivPriceTrendsGraph.legend.isEnabled = false
                    binding.ivPriceTrendsGraph.axisLeft.setDrawGridLines(false)
                    binding.ivPriceTrendsGraph.setTouchEnabled(false)
                    binding.ivPriceTrendsGraph.setPinchZoom(false)
                    binding.ivPriceTrendsGraph.isDoubleTapToZoomEnabled = false
                    binding.ivPriceTrendsGraph.xAxis.setDrawGridLines(false)
                    binding.ivPriceTrendsGraph.xAxis.position = XAxis.XAxisPosition.BOTTOM
                    binding.ivPriceTrendsGraph.axisRight.setDrawGridLines(false)
                    binding.ivPriceTrendsGraph.axisRight.setDrawLabels(false)
                    binding.ivPriceTrendsGraph.axisRight.setDrawAxisLine(false)
                    binding.ivPriceTrendsGraph.xAxis.granularity = 1f
                    binding.ivPriceTrendsGraph.axisLeft.granularity = 1f
                    binding.ivPriceTrendsGraph.xAxis.valueFormatter = XAxisFormatter()
                    binding.ivPriceTrendsGraph.data = data
                    binding.ivPriceTrendsGraph.extraBottomOffset
                    binding.ivPriceTrendsGraph.animateXY(2000, 2000)
                    linearLayoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                    binding.recyclerViewGraphOptions.layoutManager = linearLayoutManager
                    binding.recyclerViewGraphOptions.adapter = projectAdapter
                    if (binding.recyclerViewGraphOptions.onFlingListener == null) {
                        snapHelper.attachToRecyclerView(binding.recyclerViewGraphOptions)
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                null -> TODO()
            }
        }

    }

    private fun setDataAboutHoabl(commonData: AboutUs?, url: String?) {
        binding.nameTv.text = commonData?.foundersVision?.founderName
        binding.tvHeading.text = commonData?.foundersVision?.sectionHeading
        binding.fullDescriptionTv.text = showHTMLText(commonData?.foundersVision?.description)
        binding.tvAboutHoabel.text = commonData?.aboutHoabl?.sectionHeading
        binding.ttvAboutHoabel.text = showHTMLText(commonData?.aboutHoabl?.description)
        binding.corporatePhillosophy.text = commonData?.corporatePhilosophy?.sectionHeading
        binding.statsHeaderTxt.text = commonData?.statsOverview?.sectionHeading
        setVisibilities(commonData)
        Glide.with(requireContext()).load(url).into(binding.aboutusView)
    }

    private fun setVisibilities(commonData: AboutUs?) {
        if (commonData?.isAboutHoablActive == false) {
            binding.ttvAboutHoabel.isVisible = false
            binding.tvAboutHoabel.isVisible = false
        }
        if (commonData?.isFoundersVisionActive == false) {
            binding.tvHeading.isVisible = false
            binding.aboutusView.isVisible = false
            binding.nameTv.isVisible = false
            binding.fullDescriptionTv.isVisible = false
        }
        if (commonData?.isCorporatePhilosophyActive == false) {
            binding.corporatePhillosophy.isVisible = false
            binding.aboutUsRv.isVisible = false
        }
        if (commonData?.isProductCategoryActive == false) {
            binding.tvProductCategory.isVisible = false
            binding.productcategoryRv.isVisible = false
        }
        if (commonData?.isStatsOverviewActive == false) {
            binding.statsHeaderTxt.isVisible = false
            binding.statsItem.isVisible = false
        }

    }

    private fun setRecyclerView(commonData: AboutUs?) {
        //CorporatePhilosophyAdapter
        philosophyAdapter = CorporatePhilosophyAdapter(
            requireActivity(),
            commonData?.corporatePhilosophy!!.detailedInformation,
        )
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.aboutUsRv.layoutManager = linearLayoutManager
        binding.aboutUsRv.adapter = philosophyAdapter

        //ProductAdapter
        ProductAdapter(
            requireActivity(),
            commonData?.productCategory!!.detailedInformation
        ).also { it1 -> productAdapter = it1 }
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.productcategoryRv.layoutManager = linearLayoutManager
        binding.productcategoryRv.adapter = productAdapter

        //StatsOverViewAboutUsAdapter
        StatsOverViewAboutUsAdapter(
            requireActivity(),
            commonData?.statsOverview?.detailedInformation
        ).also { it1 -> statsOverViewAdapter = it1 }
        gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.statsItem.layoutManager = gridLayoutManager
        binding.statsItem.adapter = statsOverViewAdapter
        binding.statsItem.setItemViewCacheSize(10)
        binding.statsItem.setHasFixedSize(true)
    }

    inner class XAxisFormatter : IAxisValueFormatter {
        override fun getFormattedValue(p0: Float, p1: AxisBase?): String {
            return when (graphType) {
                Constants.QUATERLY -> returnFormattedValue(p0)
                Constants.MONTHLY -> returnFormattedValue(p0)
                Constants.HALF_YEARLY -> returnFormattedValue(p0)
                else -> {
                    String.format("%.0f", p0.toDouble())
                }
            }
        }
    }

    private fun returnFormattedValue(floatValue: Float): String {
        return when {
            floatValue.toInt() < 10 -> xAxisList[floatValue.toInt()]
            else -> {
                String.format("%.0f", floatValue.toDouble())
            }
        }
    }

    private fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}



