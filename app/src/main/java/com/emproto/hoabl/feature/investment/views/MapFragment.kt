package com.emproto.hoabl.feature.investment.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.core.Utility.bitmapFromVector
import com.emproto.core.Utility.decodePolyline
import com.emproto.core.Utility.getDirectionURL
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentMapBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.feature.investment.adapters.LocationInfrastructureAdapter
import com.emproto.hoabl.model.MapLocationModel
import com.emproto.hoabl.model.ProjectLocation
import com.emproto.hoabl.utils.MapItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.NetworkUtil
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.response.MapData
import com.emproto.networklayer.response.investment.ValueXXX
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Runnable
import javax.inject.Inject


class MapFragment : BaseFragment(), OnMapReadyCallback {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding: FragmentMapBinding

    @Inject
    lateinit var appPreference: AppPreference

    private lateinit var mMap: GoogleMap
    private var data: MapLocationModel? = null
    private lateinit var adapter: LocationInfrastructureAdapter

    private var selectedPosition = -1
    var dummyLatitude = 17.7667503
    var dummyLongitude = 73.1711629
    private var distanceList = ArrayList<String>()
    private var destinationList = ArrayList<ValueXXX>()
    private var durationList = ArrayList<String>()

    private lateinit var handler: Handler
    private var runnable: Runnable? = null

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val mapItemClickListener = object : MapItemClickListener {
        override fun onItemClicked(view: View, position: Int, latitude: Double, longitude: Double) {
            when (view.id) {
                R.id.cv_location_infrastructure_card -> {
                    try {
                        if (isNetworkAvailable()) {
                            eventTrackingLocationInfra()
                            eventTrackingMapSearchDistancefrom()
                            initMarkerLocation(dummyLatitude, dummyLongitude, latitude, longitude)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Network not available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            requireContext(),
                            "Error Occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun eventTrackingMapSearchDistancefrom() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.MAPSEARCHDISTANCEFROM
        )
    }

    private fun eventTrackingLocationInfra() {
        Mixpanel(requireContext()).identifyFunction(
            appPreference.getMobilenum(),
            Mixpanel.LOCATIONINFRA
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler = Handler(Looper.getMainLooper())
        arguments?.let {
            data = it.getSerializable("Location") as MapLocationModel?
            selectedPosition = it.getInt("ItemPosition", -1)
            val projectLocation = it.getSerializable(Constants.PROJECT_LOCATION) as ProjectLocation
            dummyLatitude = projectLocation.latitude.toDouble()
            dummyLongitude = projectLocation.longitude.toDouble()
        }
        setUpUI()
        enableMyLocation()
    }

    private fun setDataFromPrevious() {
        if (data != null) {
            initMapData()
        }
    }

    private fun initMapData() {
        val mapFragment = childFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            googleMap.setOnMapLoadedCallback {
                mMap = googleMap
                val originLocation = LatLng(dummyLatitude, dummyLongitude)
                mMap.let {
                    it.clear()
                    it.addMarker(MarkerOptions().position(originLocation))
                    it.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
                }
                initMarkerLocation(
                    data?.originLatitude!!,
                    data?.originLongitude!!,
                    data?.destinationLatitude!!,
                    data?.destinationLongitude!!
                )
            }
        }
    }

    private fun initMap() {
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        investmentViewModel =
            ViewModelProvider(requireActivity(), investmentFactory)[InvestmentViewModel::class.java]
        if (!Places.isInitialized()) {
            Places.initialize(this.requireContext(), NetworkUtil.decrypt())
        }
        val mapFragment = childFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            googleMap.setOnMapLoadedCallback {
                mMap = googleMap
                val originLocation = LatLng(dummyLatitude, dummyLongitude)
                mMap.let {
                    it.clear()
                    it.addMarker(MarkerOptions().position(originLocation))
                    it.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 12F))
                }
            }
        }
    }

    private fun initMarkerLocation(
        originLatitude: Double,
        originLongitude: Double,
        destinationLatitude: Double,
        destinationLongitude: Double
    ) {
        mMap.let {
            val originLocation = LatLng(originLatitude, originLongitude)
            it.clear()
            it.addMarker(
                MarkerOptions()
                    .position(originLocation)
                    .icon(
                        bitmapFromVector(
                            this.requireContext(),
                            R.drawable.ic_baseline_location_on_24
                        )
                    )
            )
            val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
            it.addMarker(
                MarkerOptions()
                    .position(destinationLocation)
                    .icon(
                        bitmapFromVector(
                            this.requireContext(),
                            R.drawable.ic_baseline_location_on_24_red
                        )
                    )
            )
            val urll = getDirectionURL(
                originLocation,
                destinationLocation,
                NetworkUtil.decrypt()!!
            )
            callDirectionApi(urll)
            val bounds = LatLngBounds.Builder()
            bounds.include(originLocation)
            bounds.include(destinationLocation)
            val b = bounds.build()
            it.animateCamera(CameraUpdateFactory.newLatLngBounds(b, 10))
        }

    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.GONE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.GONE
    }

    private fun initObserver() {
        investmentViewModel.getMapLocationInfrastructure().observe(viewLifecycleOwner) {
            for (i in 0 until it.values.size) {
                Log.d("maptag","loca= ${it.values[i].toString()}")
                if (data?.destinationLatitude == it.values[i].latitude && data?.destinationLongitude == it.values[i].longitude) {
                    selectedPosition = i
                }
                destinationList.add(it.values[i])
            }
            adapter = LocationInfrastructureAdapter(
                requireContext(),
                destinationList,
                mapItemClickListener,
                true,
                distanceList,
                selectedPosition,
                durationList
            )
            binding.mapLocationBottomSheet.rvMapLocationItemRecycler.adapter = adapter
            (binding.mapLocationBottomSheet.rvMapLocationItemRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
                false
            binding.mapLocationBottomSheet.clMapBottomSheet.visibility = View.VISIBLE

        }
        runnable = Runnable { binding.cvBackButton.visibility = View.VISIBLE }
        runnable?.let { it1 -> handler.postDelayed(it1, 2000) }


        binding.cvBackButton.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }
    }

    private fun enableMyLocation() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                initMap()
                initObserver()
                setDataFromPrevious()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                initMap()
                initObserver()
                setDataFromPrevious()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Toast.makeText(requireContext(), "Location permission required", Toast.LENGTH_SHORT)
                    .show()
            }
        }



    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
    }

    private fun callDirectionApi(url: String) {
        uiScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            withContext(Dispatchers.Main) {
                val lineoption = PolylineOptions()
                for (i in result.indices) {
                    lineoption.addAll(result[i])
                    lineoption.width(10f)
                    lineoption.color(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.text_blue_color
                        )
                    )
                    lineoption.geodesic(true)
                }
                mMap?.addPolyline(lineoption)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0!!
        val originLocation = LatLng(dummyLatitude, dummyLongitude)
        mMap?.clear()
        mMap?.addMarker(MarkerOptions().position(originLocation))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
        runnable?.let { handler.removeCallbacks(it) }

    }
}