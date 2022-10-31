package com.emproto.hoabl.feature.promises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.databinding.FragmentHoabelPromisesBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.promises.adapter.HoablPromiseAdapter
import com.emproto.hoabl.feature.promises.data.PromisesData
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.networklayer.enum.ModuleEnum
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.promises.Data
import javax.inject.Inject


class HoablPromises : BaseFragment() {

    @Inject
    lateinit var homeFactory: HomeFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentHoabelPromisesBinding

    val bundle = Bundle()

    @Inject
    lateinit var appPreference: AppPreference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoabelPromisesBinding.inflate(inflater, container, false)
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]

        (requireActivity() as HomeActivity).showHeader()
        (requireActivity() as HomeActivity).hideBackArrow()
        (requireActivity() as HomeActivity).showBottomNavigation()

        initViews()
        fetchPromises(false)

        if (appPreference.isFacilityCard()) {
            (requireActivity() as HomeActivity).hideBottomNavigation()
            (requireActivity() as HomeActivity).showBackArrow()
        }

        return binding.root
    }

    private fun fetchPromises(refresh: Boolean) {
        homeViewModel.getPromises(ModuleEnum.PROMISES.value, refresh)
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                        binding.loader.show()
                        binding.listPromises.hide()

                    }
                    Status.SUCCESS -> {
                        binding.swipeRefresh.isRefreshing = false
                        binding.loader.hide()
                        binding.listPromises.show()
                        showPromises(it.data!!.data)

                    }
                    Status.ERROR -> {
                        binding.loader.hide()
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                    }
                }
            }

    }

    private fun showPromises(promisesData: Data) {
        val list = ArrayList<PromisesData>()
        eventTrackingPromiseCard()

        list.add(
            PromisesData(
                HoablPromiseAdapter.TYPE_HEADER,
                "",
                promisesData.page.promiseSection,
                emptyList()
            )
        )
        list.add(
            PromisesData(
                HoablPromiseAdapter.TYPE_LIST,
                "",
                promisesData.page.promiseSection,
                promisesData.homePagesOrPromises
            )
        )
        list.add(
            PromisesData(
                HoablPromiseAdapter.TYPE_DISCLAIMER,
                "",
                promisesData.page.promiseSection,
                emptyList()
            )
        )

        binding.listPromises.layoutManager =
            LinearLayoutManager(requireActivity())
        binding.listPromises.adapter = HoablPromiseAdapter(
            requireContext(),
            list,
            object : HoablPromiseAdapter.PromisedItemInterface {
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

    private fun eventTrackingPromiseCard() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.PROMISECARD)
    }

    private fun initViews() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            fetchPromises(true)
        }
    }


}


