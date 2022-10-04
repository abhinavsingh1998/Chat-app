package com.emproto.hoabl.utils

import android.view.View

interface MapItemClickListener {
    fun onItemClicked(view: View, position: Int, latitude: Double, longitude: Double)
}