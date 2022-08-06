package com.emproto.hoabl.feature.promises

import com.emproto.hoabl.feature.promises.adapter.HoabelPromiseAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentHoabelPromisesBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.promises.data.DataModel
import com.emproto.hoabl.feature.promises.data.PromisesData
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.enum.ModuleEnum
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.promises.Data
import javax.inject.Inject


class HoablPromises : BaseFragment() {

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentHoabelPromisesBinding
    private lateinit var adapter: HoabelPromiseAdapter
    private var dataList = ArrayList<DataModel>()
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoabelPromisesBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory).get(HomeViewModel::class.java)

        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).hideBackArrow()
        (requireActivity() as HomeActivity).showBottomNavigation()

        initViews()
        fetchPromises(false)

        return binding.root
    }

    private fun fetchPromises(refresh: Boolean) {
        homeViewModel.getPromises(ModuleEnum.PROMISES.value, refresh)
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.LOADING -> {
                        binding.loader.show()
                        binding.listPromises.hide()

                    }
                    Status.SUCCESS -> {
                        binding.swipeRefresh.isRefreshing = false
                        binding.loader.hide()
                        binding.listPromises.show()
                        it.data!!.data?.let { promisesData ->
                            showPromises(promisesData)
                        }

                    }
                    Status.ERROR -> {
                        binding.loader.hide()
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                    }
                }
            })

    }

    private fun showPromises(promisesData: Data) {
        val list = ArrayList<PromisesData>()
        list.add(
            PromisesData(
                HoabelPromiseAdapter.TYPE_HEADER,
                "",
                promisesData.page.promiseSection,
                emptyList()
            )
        )
        list.add(
            PromisesData(
                HoabelPromiseAdapter.TYPE_LIST,
                "",
                promisesData.page.promiseSection,
                promisesData.homePagesOrPromises
            )
        )
        list.add(
            PromisesData(
                HoabelPromiseAdapter.TYPE_DISCLAIMER,
                "",
                promisesData.page.promiseSection,
                emptyList()
            )
        )

        binding.listPromises.layoutManager =
            LinearLayoutManager(requireActivity())
        binding.listPromises.adapter = HoabelPromiseAdapter(
            requireContext(),
            list,
            object : HoabelPromiseAdapter.PromisedItemInterface {
                override fun onClickItem(position: Int) {
                    homeViewModel.setSelectedPromise(promisesData.homePagesOrPromises[position])
                    (requireActivity() as HomeActivity).addFragment(
                        PromisesDetailsFragment(),
                        true
                    )
                }

            })
        binding.listPromises.setHasFixedSize(true)
        binding.listPromises.setItemViewCacheSize(10)
        binding.listPromises.isNestedScrollingEnabled = false
    }

    private fun initViews() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            fetchPromises(true)
        }
    }


}


