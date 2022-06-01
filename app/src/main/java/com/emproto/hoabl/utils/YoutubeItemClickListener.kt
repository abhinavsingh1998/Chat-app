package com.emproto.hoabl.utils

import android.view.View

interface YoutubeItemClickListener {
    fun onItemClicked(view: View, position:Int, url:String,title:String)
}