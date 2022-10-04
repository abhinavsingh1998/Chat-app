package com.emproto.hoabl.utils

import android.view.View
import com.emproto.hoabl.model.MediaViewItem

interface MediaItemClickListener {
    fun onItemClicked(view: View, position: Int, item: MediaViewItem)
}