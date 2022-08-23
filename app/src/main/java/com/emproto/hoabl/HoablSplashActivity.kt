package com.emproto.hoabl

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.os.ConfigurationCompat
import com.emproto.core.BaseActivity
import com.emproto.hoabl.databinding.ActivityHoablSplashBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.investment.views.mediagallery.PhotosFragment.Companion.TAG
import com.emproto.hoabl.feature.login.IntroSliderActivity
import com.emproto.networklayer.preferences.AppPreference
import com.google.android.gms.location.*
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class HoablSplashActivity : BaseActivity() {

    lateinit var mBinding: ActivityHoablSplashBinding

    @Inject
    lateinit var appPreference: AppPreference


    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e.,
// how often you should receive updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient
// has a new Location
    private lateinit var locationCallback: LocationCallback

    // This will store current location info
    private var currentLocation: Location? = null

    lateinit var location : Location

    lateinit var geocoder:Geocoder

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as HomeComponentProvider).homeComponent().inject(this)

        mBinding = ActivityHoablSplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest().apply {
            // Sets the desired interval for
            // active location updates.
            // This interval is inexact.
            interval = TimeUnit.SECONDS.toMillis(60)

            // Sets the fastest rate for active location updates.
            // This interval is exact, and your application will never
            // receive updates more frequently than this value
            fastestInterval = TimeUnit.SECONDS.toMillis(30)

            // Sets the maximum time when batched location
            // updates are delivered. Updates may be
            // delivered sooner than this interval
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                locationResult?.lastLocation?.let {
                    locationResult.locations[0]
//                    currentLocation = locationByGps
//                    val latitude = currentLocation.latitude
//                    val longitude = currentLocation.longitude
                    // use latitude and longitude as per your need
                } ?: {
                    Log.d(TAG, "Location information isn't available.")
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())


        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
        }
        location = locationManager
            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
        geocoder = Geocoder(this)

        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.applicationContext.resources.configuration.locales[0].country.toString()
        } else {
            "No data"
        }

        val tm = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCodeValue = tm.networkCountryIso
        val sim = tm.simCountryIso

        val loclae = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0)

        val tz = TimeZone.getDefault()
        val timeZoneId: String = tz.id

        Handler().postDelayed(Runnable { // Show white background behind floating label
//            startActivity(Intent(this, HomeActivity::class.java))
//            finish()
            if (appPreference.isUserLoggedIn()) {

                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, IntroSliderActivity::class.java))
                finish()
            }
        }, 1000)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                try {
                    val addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        10
                    )
                    val address = addresses.get(0)
                    Log.d("CountryCode","Data= ${address.countryCode}")

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                //not granted
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 10)
            val address = addresses.get(0)
            Log.d("CountryCode","Data= ${address.countryCode}")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}

