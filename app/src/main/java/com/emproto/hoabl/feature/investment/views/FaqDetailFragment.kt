package com.emproto.hoabl.feature.investment.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FaqDetailFragmentBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.FaqDetailAdapter
import com.emproto.hoabl.model.RecyclerViewFaqItem
import com.emproto.hoabl.model.RecyclerViewItem
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.investment.CgData
import javax.inject.Inject

class FaqDetailFragment:BaseFragment() {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding:FaqDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FaqDetailFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInitialization()
        callApi()
    }

    private fun setUpInitialization() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory).get(InvestmentViewModel::class.java)
    }

    private fun callApi() {
        investmentViewModel.getProjectId().observe(viewLifecycleOwner, Observer {
            callFaqApi(it)
        })
    }

    private fun callFaqApi(projectId: Int) {
        Log.d("Faq",projectId.toString())
        investmentViewModel.getInvestmentsFaq(projectId).observe(viewLifecycleOwner, Observer {
            Log.d("Faq",it.data.toString())
            when(it.status){
                Status.LOADING -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.show()
                }
                Status.SUCCESS -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    it.data?.data?.let {  data ->
                        when{
                            data.isNullOrEmpty() -> Toast.makeText(this.requireContext(), "No data available", Toast.LENGTH_SHORT).show()
                            else -> setUpRecyclerView(data)
                        }
                    }
                }
                Status.ERROR -> {
                    (requireActivity() as HomeActivity).activityHomeActivity.loader.hide()
                    (requireActivity() as HomeActivity).showErrorToast(
                        it.message!!
                    )
                }
            }
        })
    }

    private fun setUpRecyclerView(data: List<CgData>) {
        val list = ArrayList<RecyclerViewFaqItem>()
        list.add(RecyclerViewFaqItem(1,data[0]))
        for(item in data){
            list.add(RecyclerViewFaqItem(2,item))
        }
        val adapter = FaqDetailAdapter(this.requireContext(),list,data)
        binding.rvFaq.adapter = adapter
    }
}