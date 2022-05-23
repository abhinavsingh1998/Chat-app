package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentLandSkusBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.investment.adapters.LandSkusAdapter
import com.emproto.hoabl.feature.investment.dialogs.ApplicationSubmitDialog
import com.emproto.hoabl.feature.investment.dialogs.ConfirmationDialog
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import java.util.ArrayList
import javax.inject.Inject

class LandSkusFragment:BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    private lateinit var binding: FragmentLandSkusBinding
    private lateinit var landSkusAdapter: LandSkusAdapter

    val onLandSkusItemClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.btn_apply_now -> {
                    val confirmationDialog = ConfirmationDialog(this)
                    confirmationDialog.show(this.parentFragmentManager,"ConfirmationDialog")
                }
                R.id.cl_not_convinced -> {
                    val applicationSubmitDialog = ApplicationSubmitDialog("Video Call request sent successfully.","Our sales person will reach out to you soon!",false)
                    applicationSubmitDialog.show(parentFragmentManager,"ApplicationSubmitDialog")
                }
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentLandSkusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpRecyclerview()
    }

    private fun setUpUI() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory).get(InvestmentViewModel::class.java)
        (activity as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
    }

    private fun setUpRecyclerview() {
        binding.clOuterLayout.visibility = View.GONE
        (requireActivity() as HomeActivity).activityHomeActivity.loader.visibility = View.VISIBLE
        investmentViewModel.getSkus().observe(viewLifecycleOwner, Observer {
                val list = ArrayList<RecyclerViewItem>()
                list.add(RecyclerViewItem(1))
                list.add(RecyclerViewItem(3))

                landSkusAdapter = LandSkusAdapter(this,list,it,itemClickListener)
                binding.rvLandSkus.adapter = landSkusAdapter
                landSkusAdapter.setItemClickListener(onLandSkusItemClickListener)
        })
        Handler(Looper.getMainLooper()).postDelayed({
            binding.clOuterLayout.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).activityHomeActivity.loader.visibility = View.GONE
        }, 1000)
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClicked(view: View, position: Int, item: String) {
            val confirmationDialog = ConfirmationDialog(this@LandSkusFragment)
            confirmationDialog.show(this@LandSkusFragment.parentFragmentManager,"ConfirmationDialog")
        }
    }
}