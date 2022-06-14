package com.emproto.hoabl.feature.profile

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentAboutUsBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.profile.adapter.AboutUSAdapter
import com.emproto.hoabl.feature.profile.adapter.CorporatePhilosphyAdapter
import com.emproto.hoabl.feature.profile.adapter.GraphOptionsAdapter
import com.emproto.hoabl.feature.profile.adapter.ProductAdapter
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.resourceManagment.ProflieResponse
import com.github.mikephil.charting.components.XAxis
import javax.inject.Inject


class AboutUsFragment : Fragment() , GraphOptionsAdapter.GraphItemClicks {

    @Inject
    lateinit var factory: ProfileFactory
    lateinit var profileViewModel: ProfileViewModel

    lateinit var binding: FragmentAboutUsBinding
    lateinit var adapter: AboutUSAdapter
    lateinit var ivarrow: ImageView
    lateinit var philosophyAdapter:CorporatePhilosphyAdapter
    lateinit var productAdapter: ProductAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

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
        initObserver()
        initClickListener()
        return binding.root
    }

    private fun initView() {
        /* binding.tvRating.text = data.generalInfoEscalationGraph.estimatedAppreciation.toString()
         binding.tvXAxisLabel.text = data.generalInfoEscalationGraph.yAxisDisplayName
         binding.tvYAxisLabel.text = data.generalInfoEscalationGraph.xAxisDisplayName
         val graphData = data.generalInfoEscalationGraph.dataPoints.points
         val linevalues = ArrayList<Entry>()
         for (item in graphData) {
             linevalues.add(Entry(item.year.toFloat(), item.value.toFloat()))
         }
         val linedataset = LineDataSet(linevalues, "First")
         //We add features to our chart
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             linedataset.color = requireContext().getColor(R.color.app_color)
         }

         linedataset.valueTextSize = 12F
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             linedataset.fillColor = requireContext().getColor(R.color.light_app_color)
         }
         linedataset.mode = LineDataSet.Mode.HORIZONTAL_BEZIER;

         //We connect our data to the UI Screen
         val data = LineData(linedataset)
 */
        //binding.ivPriceTrendsGraph.setDrawBorders(false);
        //binding.ivPriceTrendsGraph.setDrawGridBackground(false);
        binding.ivPriceTrendsGraph.getDescription().setEnabled(false);
        binding.ivPriceTrendsGraph.getLegend().setEnabled(false);
        binding.ivPriceTrendsGraph.getAxisLeft().setDrawGridLines(false);
        //binding.ivPriceTrendsGraph.getAxisLeft().setDrawLabels(false);
        //binding.ivPriceTrendsGraph.getAxisLeft().setDrawAxisLine(false);
        binding.ivPriceTrendsGraph.getXAxis().setDrawGridLines(false);
        binding.ivPriceTrendsGraph.getXAxis().position = XAxis.XAxisPosition.BOTTOM;
        //binding.ivPriceTrendsGraph.getXAxis().setDrawAxisLine(false);
        binding.ivPriceTrendsGraph.getAxisRight().setDrawGridLines(false);
        binding.ivPriceTrendsGraph.getAxisRight().setDrawLabels(false);
        binding.ivPriceTrendsGraph.getAxisRight().setDrawAxisLine(false);
        //binding.ivPriceTrendsGraph.axisLeft.isEnabled = false
        //binding.ivPriceTrendsGraph.axisRight.isEnabled = false
        //  binding.ivPriceTrendsGraph.data = data
        binding.ivPriceTrendsGraph.animateXY(2000, 2000)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fun initData(): ArrayList<RecyclerViewItem> {
            val dataList: ArrayList<RecyclerViewItem> = ArrayList<RecyclerViewItem>()
            dataList.add(RecyclerViewItem(AboutUSAdapter.VIEW_CORPORATE_PHILOSOPHY))
            dataList.add(RecyclerViewItem(AboutUSAdapter.VIEW_LIFESTYLE))
            dataList.add(RecyclerViewItem(AboutUSAdapter.VIEW_PRODUCT_CATEGORY))

            return dataList
        }

        val adapter = AboutUSAdapter(requireContext(), initData())
        binding.aboutUsRv.adapter = adapter

        val data = ArrayList<String>()
        data.add("Tomorrow Land")
        data.add("Isle of Bliss")
        data.add("The Rush")
        data.add("South Havana")
        val graphOptionsAdapter = GraphOptionsAdapter(requireContext(), data, this)
        binding.recyclerViewGraphOptions.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.recyclerViewGraphOptions.adapter = graphOptionsAdapter
    }

    private fun initClickListener() {
        binding.backAction.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                requireActivity().supportFragmentManager.popBackStack()
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


    private fun initObserver(){

        profileViewModel.getAboutHoabl(5005).observe(viewLifecycleOwner, object :Observer<BaseResponse<ProflieResponse>>{
            override fun onChanged(it: BaseResponse<ProflieResponse>?) {
                when (it?.status){

                    Status.LOADING ->{

                    }

                    Status.SUCCESS ->{
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
                    }
                }
            }

        })
    }

    public fun showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }
}



