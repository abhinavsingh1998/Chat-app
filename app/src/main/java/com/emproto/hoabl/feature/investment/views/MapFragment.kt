package com.emproto.hoabl.feature.investment.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentMapBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.LocationInfrastructureAdapter
import com.emproto.hoabl.model.MapLocationModel
import com.emproto.hoabl.model.ProjectLocation
import com.emproto.hoabl.utils.MapItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
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

    private lateinit var mMap: GoogleMap
    private var data: MapLocationModel? = null
    private lateinit var adapter: LocationInfrastructureAdapter

    private var selectedPosition = -1
    var dummyLatitude = 17.7667503
    var dummyLongitude = 73.1711629
    private var distanceList = ArrayList<String>()
    private var destinationList= ArrayList<ValueXXX>()

    private lateinit var handler : Handler
    private var runnable: Runnable? = null

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val mapItemClickListener = object : MapItemClickListener {
        override fun onItemClicked(view: View, position: Int, latitude: Double, longitude: Double) {
            when (view.id) {
                R.id.cv_location_infrastructure_card -> {
                    if(isNetworkAvailable()){
                        initMarkerLocation(dummyLatitude, dummyLongitude, latitude, longitude)
                    }else{
                        Toast.makeText(requireContext(), "Network not available", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
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
            selectedPosition = it.getInt("ItemPosition",-1)
            val projectLocation = it.getSerializable(Constants.PROJECT_LOCATION) as ProjectLocation
            dummyLatitude = projectLocation.latitude.toDouble()
            dummyLongitude = projectLocation.longitude.toDouble()
        }
        setUpUI()
        enableMyLocation()
    }

    private fun calculateDistance(originLat:Double,originLong:Double,destinationList: List<ValueXXX>) {
        val originLocation = LatLng(originLat,originLong)
        for(item in destinationList){
            val destinationLocation = LatLng(item.latitude,item.longitude)
            val url = getDirectionURL(
                originLocation,
                destinationLocation,
                resources.getString(R.string.map_api_key)
            )
            callDirectionsApiForDistance(url)
        }
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
//                addMarkers(googleMap)
                mMap = googleMap
                val originLocation = LatLng(dummyLatitude, dummyLongitude)
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(originLocation))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
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
            Places.initialize(this.requireContext(), resources.getString(R.string.map_api_key))
        }
        val mapFragment = childFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            googleMap.setOnMapLoadedCallback {
//                addMarkers(googleMap)
                //Lat = 17.8612263
                //long = 73.0749419
                mMap = googleMap
                val originLocation = LatLng(dummyLatitude, dummyLongitude)
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(originLocation))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 15F))
            }
        }
    }

    private fun initMarkerLocation(
        originLatitude: Double,
        originLongitude: Double,
        destinationLatitude: Double,
        destinationLongitude: Double
    ) {
        mMap.clear()
        val originLocation = LatLng(originLatitude, originLongitude)
        mMap.addMarker(
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
        mMap.addMarker(
            MarkerOptions()
                .position(destinationLocation)
                .icon(
                    bitmapFromVector(
                        this.requireContext(),
                        R.drawable.ic_baseline_location_on_24_red
                    )
                )
        )
        val directionUrl = getDirectionURL(
            originLocation,
            destinationLocation,
            resources.getString(R.string.map_api_key)
        )
        callDirectionApi(directionUrl)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 15F))
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
                if (data?.destinationLatitude == it.values[i].latitude && data?.destinationLongitude == it.values[i].longitude) {
                    selectedPosition = i
                }
                destinationList.add(it.values[i])
            }
            calculateDistance(dummyLatitude, dummyLongitude, it.values)

        }
        runnable = Runnable {   binding.cvBackButton.visibility = View.VISIBLE }
        runnable?.let { it1 -> handler.postDelayed(it1,2000) }


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

//
//    private fun addMarkers(googleMap: GoogleMap) {
//        val bounds = LatLngBounds.builder()
//        val marker = googleMap.addMarker(
//            MarkerOptions()
//                .title("Isle of Bliss")
//                .position(LatLng(dummyLatitude, dummyLongitude))
//                .icon(BitmapFromVector(this.requireContext(), R.drawable.location_image_red))
//        )
//        bounds.include(LatLng(dummyLatitude, dummyLongitude))
//        marker?.tag = "Isle of Bliss"
////        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),50))
//        googleMap.animateCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(dummyLatitude, dummyLongitude),
//                16.0f
//            )
//        )
//    }

    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility =
            View.VISIBLE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility =
            View.VISIBLE
    }

    private fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    private fun callDirectionApi(url:String){
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

            withContext(Dispatchers.Main){
                val lineOption = PolylineOptions()
                for (i in result.indices) {
                    lineOption.addAll(result[i])
                    lineOption.width(10f)
                    lineOption.color(ContextCompat.getColor(requireContext(), R.color.text_blue_color))
                    lineOption.geodesic(true)
                }
                mMap.addPolyline(lineOption)
            }
        }
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dLat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dLng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val originLocation = LatLng(dummyLatitude, dummyLongitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(originLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 15F))
    }

    private fun callDirectionsApiForDistance(url:String){
        uiScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result = ArrayList<List<LatLng>>()
            var dis = ""
            try {
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                dis = respObj.routes[0].legs[0].distance.text
                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            withContext(Dispatchers.Main) {
                distanceList.add(dis)
                if(distanceList.size == destinationList.size){
                    adapter = LocationInfrastructureAdapter(
                        requireContext(),
                        destinationList,
                        mapItemClickListener,
                        true,
                        distanceList,
                        selectedPosition
                    )
                    binding.mapLocationBottomSheet.rvMapLocationItemRecycler.adapter = adapter
                    (binding.mapLocationBottomSheet.rvMapLocationItemRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
                        false
                    binding.mapLocationBottomSheet.clMapBottomSheet.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
        runnable?.let { handler.removeCallbacks(it) }

    }
}