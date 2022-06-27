package com.emproto.hoabl.feature.portfolio.views

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.WindowManager

class CustomDialog(context: Context) : Dialog(context) {

    fun setFullWidth() {

    }

    fun showDialog(){
        this.show()
        val window = this.getWindow()
        window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        window.setDimAmount(0.9F)
    }
}