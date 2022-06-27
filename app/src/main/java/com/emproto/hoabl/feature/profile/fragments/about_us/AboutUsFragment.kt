package com.emproto.hoabl.feature.profile.fragments.about_us

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentAboutUsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.chat.views.fragments.ChatsFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.profile.adapter.*
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.AllProjectsResponse
import com.emproto.networklayer.response.resourceManagment.ProflieResponse
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import javax.inject.Inject


class AboutUsFragment : Fragment() , GraphOptionsAdapter.GraphItemClicks {

    @Inject
    lateinit var factory: ProfileFactory
    lateinit var profileViewModel: ProfileViewModel

    lateinit var binding: FragmentAboutUsBinding
    lateinit var ivarrow: ImageView
    lateinit var philosophyAdapter:CorporatePhilosphyAdapter
    lateinit var productAdapter: ProductAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var projectAdapter:AllProjectsAdapter
    lateinit var statsOverViewAdapter:StatsOverViewAboutUsAdapter
     var defaultPosition=0

    private var graphType = ""
    private var xaxisList = ArrayList<String>()

    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            false

        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.isVisible =
            false

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel =
            ViewModelProvider(requireActivity(), factory)[ProfileViewModel::class.java]

        initView()
        initObserver(refresh = false)
        initClickListener()
        return binding.root
    }

    private fun initView() {

        binding.refreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            binding.loader.show()
            initObserver(refresh = true)

            binding.refreshLayout.isRefreshing = false

        })
    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        })

        binding.invest.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                (requireActivity() as HomeActivity).navigate(R.id.navigation_investment)
            }

        })

        binding.tvQuery.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
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
                    false
                )
            }

        })
    }

    override fun graphItemClicked(position: Int, itemView: View, line: View) {
        if (view?.isSelected == true) {
            line.visibility = View.VISIBLE
            line.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.app_color));
        } else {
            line.visibility = View.GONE
        }
    }

    private fun initObserver(refresh:Boolean){

        profileViewModel.getAboutHoabl(5005).observe(viewLifecycleOwner, object :Observer<BaseResponse<ProflieResponse>>{
            override fun onChanged(it: BaseResponse<ProflieResponse>?) {
                when (it?.status){

                    Status.LOADING ->{
                        binding.rootView.isVisible= false
                        binding.loader.show()
                    }

                    Status.SUCCESS ->{
                        binding.rootView.isVisible= true
                        binding.loader.hide()
                        val commonData= it.data?.data?.page?.aboutUs

                     var url= commonData?.foundersVision?.media?.value?.url

                        Glide.with(requireContext()).load(url)
                            .into(binding.aboutusView)

                        binding.nameTv.text= commonData?.foundersVision?.founderName
                        binding.fullDescriptionTv.text= showHTMLText(commonData?.foundersVision?.description)

                        binding.ttvAboutHoabel.text= showHTMLText(commonData?.aboutHoabl?.description)


                        //loading Philosphy list
                        philosophyAdapter = CorporatePhilosphyAdapter(
                            requireActivity(),
                            commonData?.corporatePhilosophy!!.detailedInformation,
                        )


                        linearLayoutManager = LinearLayoutManager(
                            requireContext(),
                            RecyclerView.HORIZONTAL,
                            false
                        )
                        binding.aboutUsRv.layoutManager = linearLayoutManager
                        binding.aboutUsRv.adapter = philosophyAdapter

                        //loading product list
                        productAdapter= ProductAdapter(requireActivity(),
                        commonData?.productCategory!!.detailedInformation)

                        linearLayoutManager = LinearLayoutManager(
                            requireContext(),
                            RecyclerView.VERTICAL,
                            false
                        )
                        binding.productcategoryRv.layoutManager = linearLayoutManager
                        binding.productcategoryRv.adapter = productAdapter


                        //loading Stats list

                        statsOverViewAdapter= StatsOverViewAboutUsAdapter(requireActivity(),
                            commonData?.statsOverview?.detailedInformation)

                        gridLayoutManager = GridLayoutManager(requireContext(), 2)
                        binding.statsItem.layoutManager =  gridLayoutManager
                        binding.statsItem.adapter = statsOverViewAdapter
                        binding.statsItem.setItemViewCacheSize(10)
                        binding.statsItem.setHasFixedSize(true)
                    }
                }
            }

        })

        profileViewModel.getAllProjects(refresh).observe(viewLifecycleOwner, object : Observer<BaseResponse<AllProjectsResponse>>{
            override fun onChanged(it: BaseResponse<AllProjectsResponse>?) {

                when(it?.status) {
                    Status.LOADING ->{
                        binding.rootView.isVisible= false
                        binding.loader.show()

                    }
                    Status.SUCCESS ->{
                        binding.rootView.isVisible= true
                        binding.loader.hide()
                        projectAdapter= AllProjectsAdapter(
                            requireActivity(),
                            it?.data?.data!!,
                            object : AllProjectsAdapter.AllprojectsInterface{
                                override fun onClickItem(position: Int) {

                                    var currentData= it?.data?.data!![position]

                                        binding.tvXAxisLabel.text = currentData.generalInfoEscalationGraph.yAxisDisplayName
                                        binding.tvYAxisLabel.text = currentData.generalInfoEscalationGraph.xAxisDisplayName
                                        val graphData = currentData.generalInfoEscalationGraph.dataPoints.points
                                        val linevalues = ArrayList<Entry>()
                                        when(currentData.generalInfoEscalationGraph.dataPoints.dataPointType){
                                            "Yearly" -> {
                                                graphType = "Yearly"
                                                for(item in graphData){
                                                    linevalues.add(Entry(item.year.toFloat(),item.value.toFloat()))
                                                }
                                            }
                                            "Half Yearly" -> {
                                                graphType = "Half Yearly"
                                                for(i in 0..currentData.generalInfoEscalationGraph.dataPoints.points.size-1){
                                                    val fmString = currentData.generalInfoEscalationGraph.dataPoints.points[i].halfYear.toString().substring(0,3)
                                                    val yearString = currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(2,4)
                                                    val str = "$fmString-$yearString"
                                                    xaxisList.add(str)
                                                }
                                                var index = 0
                                                for(item in graphData){
                                                    linevalues.add(Entry(index.toFloat(),item.value.toFloat()))
                                                    index++
                                                }
                                            }
                                            "Quaterly" -> {
                                                graphType = "Quaterly"
                                                for(i in 0..currentData.generalInfoEscalationGraph.dataPoints.points.size-1){
                                                    val fmString = currentData.generalInfoEscalationGraph.dataPoints.points[i].quater.toString().substring(0,2)
                                                    val yearString = currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(2,4)
                                                    val str = "$fmString-$yearString"
                                                    xaxisList.add(str)
                                                }
                                                var index = 0
                                                for(item in graphData){
                                                    linevalues.add(Entry(index.toFloat(),item.value.toFloat()))
                                                    index++
                                                }
                                            }
                                            "Monthly" -> {
                                                graphType = "Monthly"
                                                for(i in 0..currentData.generalInfoEscalationGraph.dataPoints.points.size-1){
                                                    val fmString = currentData.generalInfoEscalationGraph.dataPoints.points[i].month.toString().substring(0,3)
                                                    val yearString = currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(2,4)
                                                    val str = "$fmString-$yearString"
                                                    xaxisList.add(str)
                                                }
                                                var index = 0
                                                for(item in graphData){
                                                    linevalues.add(Entry(index.toFloat(),item.value.toFloat()))
                                                    index++
                                                }
                                            }
                                        }

                                        val linedataset = LineDataSet(linevalues, "")
                                        //We add features to our chart
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            linedataset.color = resources.getColor(R.color.green)
                                        }

                                        linedataset.valueTextSize = 12F
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            linedataset.fillColor = resources.getColor(R.color.green)
                                        }
                                        linedataset.mode = LineDataSet.Mode.LINEAR;
                                        linedataset.setDrawCircles(false)
                                        linedataset.setDrawValues(false)
                                        val data = LineData(linedataset)

                                        binding.ivPriceTrendsGraph.getDescription().setEnabled(false);
                                        binding.ivPriceTrendsGraph.getLegend().setEnabled(false);
                                        binding.ivPriceTrendsGraph.getAxisLeft().setDrawGridLines(false);
                                        binding.ivPriceTrendsGraph.setTouchEnabled(false)
                                        binding.ivPriceTrendsGraph.setPinchZoom(false)
                                        binding.ivPriceTrendsGraph.isDoubleTapToZoomEnabled = false
                                        //binding.ivPriceTrendsGraph.getAxisLeft().setDrawLabels(false);
                                        //binding.ivPriceTrendsGraph.getAxisLeft().setDrawAxisLine(false);
                                        binding.ivPriceTrendsGraph.getXAxis().setDrawGridLines(false);
                                        binding.ivPriceTrendsGraph.getXAxis().position = XAxis.XAxisPosition.BOTTOM;
                                        //binding.ivPriceTrendsGraph.getXAxis().setDrawAxisLine(false);
                                        binding.ivPriceTrendsGraph.getAxisRight().setDrawGridLines(false);
                                        binding.ivPriceTrendsGraph.getAxisRight().setDrawLabels(false);
                                        binding.ivPriceTrendsGraph.getAxisRight().setDrawAxisLine(false);
                                        binding.ivPriceTrendsGraph.xAxis.granularity = 1f
                                        binding.ivPriceTrendsGraph.axisLeft.granularity = 1f
//
                                        binding.ivPriceTrendsGraph.getAxisLeft().valueFormatter = Xaxisformatter()
                                        binding.ivPriceTrendsGraph.xAxis.valueFormatter = Xaxisformatter()
                                        binding.ivPriceTrendsGraph.data = data
                                        binding.ivPriceTrendsGraph.extraBottomOffset
                                        binding.ivPriceTrendsGraph.animateXY(2000, 2000)
                                    }
                            }
                        )

                        var currentData= it?.data?.data!![defaultPosition]

                        binding.tvXAxisLabel.text = currentData.generalInfoEscalationGraph.yAxisDisplayName
                        binding.tvYAxisLabel.text = currentData.generalInfoEscalationGraph.xAxisDisplayName
                        val graphData = currentData.generalInfoEscalationGraph.dataPoints.points
                        val linevalues = ArrayList<Entry>()
                        when(currentData.generalInfoEscalationGraph.dataPoints.dataPointType){
                            "Yearly" -> {
                                graphType = "Yearly"
                                for(item in graphData){
                                    linevalues.add(Entry(item.year.toFloat(),item.value.toFloat()))
                                }
                            }
                            "Half Yearly" -> {
                                graphType = "Half Yearly"
                                for(i in 0..currentData.generalInfoEscalationGraph.dataPoints.points.size-1){
                                    val fmString = currentData.generalInfoEscalationGraph.dataPoints.points[i].halfYear.toString().substring(0,3)
                                    val yearString = currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(2,4)
                                    val str = "$fmString-$yearString"
                                    xaxisList.add(str)
                                }
                                var index = 0
                                for(item in graphData){
                                    linevalues.add(Entry(index.toFloat(),item.value.toFloat()))
                                    index++
                                }
                            }
                            "Quaterly" -> {
                                graphType = "Quaterly"
                                for(i in 0..currentData.generalInfoEscalationGraph.dataPoints.points.size-1){
                                    val fmString = currentData.generalInfoEscalationGraph.dataPoints.points[i].quater.toString().substring(0,2)
                                    val yearString = currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(2,4)
                                    val str = "$fmString-$yearString"
                                    xaxisList.add(str)
                                }
                                var index = 0
                                for(item in graphData){
                                    linevalues.add(Entry(index.toFloat(),item.value.toFloat()))
                                    index++
                                }
                            }
                            "Monthly" -> {
                                graphType = "Monthly"
                                for(i in 0..currentData.generalInfoEscalationGraph.dataPoints.points.size-1){
                                    val fmString = currentData.generalInfoEscalationGraph.dataPoints.points[i].month.toString().substring(0,3)
                                    val yearString = currentData.generalInfoEscalationGraph.dataPoints.points[i].year.substring(2,4)
                                    val str = "$fmString-$yearString"
                                    xaxisList.add(str)
                                }
                                var index = 0
                                for(item in graphData){
                                    linevalues.add(Entry(index.toFloat(),item.value.toFloat()))
                                    index++
                                }
                            }
                        }

                        val linedataset = LineDataSet(linevalues, "")
                        //We add features to our chart
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            linedataset.color = resources.getColor(R.color.green)
                        }

                        linedataset.valueTextSize = 12F
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            linedataset.fillColor = resources.getColor(R.color.green)
                        }
                        linedataset.mode = LineDataSet.Mode.LINEAR;
                        linedataset.setDrawCircles(false)
                        linedataset.setDrawValues(false)
                        val data = LineData(linedataset)

                        binding.ivPriceTrendsGraph.getDescription().setEnabled(false);
                        binding.ivPriceTrendsGraph.getLegend().setEnabled(false);
                        binding.ivPriceTrendsGraph.getAxisLeft().setDrawGridLines(false);
                        binding.ivPriceTrendsGraph.setTouchEnabled(false)
                        binding.ivPriceTrendsGraph.setPinchZoom(false)
                        binding.ivPriceTrendsGraph.isDoubleTapToZoomEnabled = false
                        //binding.ivPriceTrendsGraph.getAxisLeft().setDrawLabels(false);
                        //binding.ivPriceTrendsGraph.getAxisLeft().setDrawAxisLine(false);
                        binding.ivPriceTrendsGraph.getXAxis().setDrawGridLines(false);
                        binding.ivPriceTrendsGraph.getXAxis().position = XAxis.XAxisPosition.BOTTOM;
                        //binding.ivPriceTrendsGraph.getXAxis().setDrawAxisLine(false);
                        binding.ivPriceTrendsGraph.getAxisRight().setDrawGridLines(false);
                        binding.ivPriceTrendsGraph.getAxisRight().setDrawLabels(false);
                        binding.ivPriceTrendsGraph.getAxisRight().setDrawAxisLine(false);
                        binding.ivPriceTrendsGraph.xAxis.granularity = 1f
                        binding.ivPriceTrendsGraph.axisLeft.granularity = 1f
//
                        binding.ivPriceTrendsGraph.getAxisLeft().valueFormatter = Xaxisformatter()
                        binding.ivPriceTrendsGraph.xAxis.valueFormatter = Xaxisformatter()
                        binding.ivPriceTrendsGraph.data = data
                        binding.ivPriceTrendsGraph.extraBottomOffset
                        binding.ivPriceTrendsGraph.animateXY(2000, 2000)

                        linearLayoutManager = LinearLayoutManager(
                            requireContext(),
                            RecyclerView.HORIZONTAL,
                            false
                        )
                        binding.recyclerViewGraphOptions.layoutManager = linearLayoutManager
                        binding.recyclerViewGraphOptions.adapter = projectAdapter
                    }
                    Status.ERROR ->{
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

        })

    }

    inner class Xaxisformatter : IAxisValueFormatter {
        override fun getFormattedValue(p0: Float, p1: AxisBase?): String {
            return when(graphType){
                "Quaterly" -> returnFormattedValue(p0)
                "Monthly" -> returnFormattedValue(p0)
                "Half Yearly" -> returnFormattedValue(p0)
                else -> { String.format("%.0f", p0.toDouble()) }
            }
        }
    }

    private fun returnFormattedValue(floatValue:Float):String{
        return when {
            floatValue.toInt() < 10 -> xaxisList[floatValue.toInt()]
            else -> { String.format("%.0f", floatValue.toDouble()) }
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



