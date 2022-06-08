package com.emproto.hoabl.utils

import android.view.View

interface SimilarInvItemClickListener {
    fun onItemClicked(view: View, position:Int, item:String)
}