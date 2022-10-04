package com.emproto.hoabl.utils

import android.view.View

interface ItemClickListener {
    fun onItemClicked(view: View, position: Int, item: String)
}