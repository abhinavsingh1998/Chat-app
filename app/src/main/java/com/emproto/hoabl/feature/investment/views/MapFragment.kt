package com.emproto.hoabl.feature.investment.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentMapBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.LocationInfrastructureAdapter
import com.emproto.hoabl.model.MapLocationModel
import com.emproto.hoabl.utils.MapItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel
import com.emproto.hoabl.viewmodels.factory.InvestmentFactory
import com.emproto.networklayer.response.MapData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject


class MapFragment : BaseFragment(), OnMapReadyCallback {

    @Inject
    lateinit var investmentFactory: InvestmentFactory
    lateinit var investmentViewModel: InvestmentViewModel
    lateinit var binding: FragmentMapBinding

    private lateinit var mMap: GoogleMap
    private var data:MapLocationModel? = null
    private lateinit var adapter:LocationInfrastructureAdapter

    private var selectedPosition = -1

    private val mapItemClickListener = object : MapItemClickListener {
        override fun onItemClicked(view: View, position: Int, latitude: Double, longitude: Double) {
            when(view.id){
                R.id.cv_location_infrastructure_card -> {
                    initMarkerLocation(12.9274,77.586387,latitude,longitude)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            data = it.getSerializable("Location") as MapLocationModel
            selectedPosition = it.getInt("ItemPosition")
        }
        initMap()
        setUpUI()
        setDataFromPrevious()
    }

    private fun setDataFromPrevious() {
        if(data!= null){
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
                val originLocation = LatLng(12.9274,77.586387)
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(originLocation))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
                initMarkerLocation(data?.originLatitude!!,data?.originLongitude!!,data?.destinationLatitude!!,data?.destinationLongitude!!)
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
                mMap = googleMap
                val originLocation = LatLng(12.9274,77.586387)
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(originLocation))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
            }
        }
    }

    private fun initMarkerLocation(originLatitude:Double,originLongitude:Double,destinationLatitude:Double,destinationLongitude:Double) {
        mMap.clear()
        val originLocation = LatLng(originLatitude, originLongitude)
        mMap.addMarker(MarkerOptions()
            .position(originLocation)
            .icon(BitmapFromVector(this.requireContext(), R.drawable.ic_baseline_location_on_24)))
        val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
        mMap.addMarker(MarkerOptions()
            .position(destinationLocation)
            .icon(BitmapFromVector(this.requireContext(), R.drawable.ic_baseline_location_on_24_red)))
        val urll = getDirectionURL(originLocation, destinationLocation, resources.getString(R.string.map_api_key))
        GetDirection(urll).execute()
        val bounds = LatLngBounds.builder()
            .include(originLocation)
            .include(destinationLocation)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 15F))
    }

    private fun setUpUI() {
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility = View.GONE

        investmentViewModel.getMapLocationInfrastructure().observe(viewLifecycleOwner, Observer {
            for(i in 0..it.values.size-1){
                if(data?.destinationLatitude == it.values[i].latitude && data?.destinationLongitude == it.values[i].longitude){
                    selectedPosition = i
                }
            }
            adapter = LocationInfrastructureAdapter(this.requireContext(),it.values,mapItemClickListener,true,selectedPosition)
            binding.mapLocationBottomSheet.rvMapLocationItemRecycler.adapter = adapter
            (binding.mapLocationBottomSheet.rvMapLocationItemRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        })

        Handler().postDelayed({
            binding.cvSearch.visibility = View.VISIBLE
            binding.mapLocationBottomSheet.clMapBottomSheet.visibility = View.VISIBLE
            val anim = AnimationUtils.loadAnimation(this.requireContext(),R.anim.balloon_fade_in)
            anim.duration = 3000
            binding.mapLocationBottomSheet.clMapBottomSheet.startAnimation(anim)
            binding.cvSearch.startAnimation(anim)
                              }, 2000)

        binding.ivBackImage.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }
    }

    private fun addMarkers(googleMap: GoogleMap) {
        val bounds = LatLngBounds.builder()
        val marker = googleMap.addMarker(
            MarkerOptions()
                .title("Isle of Bliss")
                .position(LatLng(12.8987201, 77.5897014))
                .icon(BitmapFromVector(this.requireContext(), R.drawable.location_image_red))
        )
        bounds.include(LatLng(12.8987201, 77.5897014))
        marker?.tag = "Isle of Bliss"
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),50))
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(12.8987201, 77.5897014),
                16.0f
            )
        )
    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
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
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.VISIBLE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility = View.VISIBLE
    }

    private fun getDirectionURL(origin:LatLng, dest:LatLng, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(resources.getColor(android.R.color.holo_blue_dark))
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {
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
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0!!
        val originLocation = LatLng(12.927546,77.5855983)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(originLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
    }

}