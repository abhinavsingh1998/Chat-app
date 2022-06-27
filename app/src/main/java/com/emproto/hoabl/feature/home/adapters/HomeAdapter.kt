package com.emproto.hoabl.feature.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.databinding.LastFewPlotsLayoutBinding
import com.emproto.hoabl.databinding.NewInvestmentTopLayoutBinding
import com.emproto.hoabl.databinding.TrendingProjectsLayoutBinding
import com.emproto.hoabl.feature.investment.adapters.InvestmentViewPagerAdapter
import com.emproto.hoabl.feature.investment.adapters.NewInvestmentAdapter
import com.emproto.hoabl.feature.promises.data.PromisesData
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.home.Data
import com.emproto.networklayer.response.home.FacilityManagement
import com.emproto.networklayer.response.home.HomeResponse
import com.emproto.networklayer.response.home.Page

class HomeAdapter(){
//    var context: Context,
//    val data: Data,
//    val list: List<RecyclerViewItem>,
//    val itemClickListener: ItemClickListener
//    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

//    companion object {
//        const val NEW_PROJECT = 1
//        const val INCOMPLETED_PAYMENT = 2
//        const val LATEST_UPDATES = 3
//        const val PROMISES = 4
//        const val  FACILITY_MANAGMENT= 5
//        const val INSIGHTS = 6
//        const val TESTIMONIAS=7
//        const val SHARE_APP = 8
//    }

//    private lateinit var investmentAdapter: InvestmentCardAdapter
//    private lateinit var pendingPaymentsAdapter: PendingPaymentsAdapter
//    private lateinit var latestUpdateAdapter: LatestUpdateAdapter
//    private lateinit var projectPromisesAdapter: ProjectPromisesAdapter
//    private lateinit var insightsAdapter: InsightsAdapter
//    private lateinit var testimonialAdapter: TestimonialAdapter


//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when(viewType){
//            NEW_PROJECT.inves -> { NewLaunchViewHolder(
//                NewInvestmentTopLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)) }
//            NewInvestmentAdapter.TYPE_LAST_PLOTS -> { LastFewPlotsViewHolder(
//                LastFewPlotsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))}
//            else -> { TrendingProjectsViewHolder(
//                TrendingProjectsLayoutBinding.inflate(
//                    LayoutInflater.from(parent.context),parent,false)) }
//        }
//    }

}