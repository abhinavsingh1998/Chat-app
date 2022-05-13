package com.emproto.hoabl.feature.investment.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentMapBinding
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.adapters.LocationInfrastructureAdapter
import com.emproto.networklayer.response.investment.LocationInfrastructure
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapFragment : BaseFragment() {

    lateinit var binding: FragmentMapBinding

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
        initMap()
        setUpUI()
    }

    private fun setUpUI() {
        val mapLocationData = arguments?.getSerializable("MapLocationData") as LocationInfrastructure

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.visibility = View.GONE
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.visibility = View.GONE

        val adapter = LocationInfrastructureAdapter(this.requireContext(),mapLocationData.values)
        binding.mapLocationBottomSheet.rvMapLocationItemRecycler.adapter = adapter

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

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            googleMap.setOnMapLoadedCallback {
                addMarkers(googleMap)
            }
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

}