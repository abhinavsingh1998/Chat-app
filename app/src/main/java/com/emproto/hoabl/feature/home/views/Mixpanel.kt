package com.emproto.hoabl.feature.home.views

import android.content.Context
import com.emproto.hoabl.R
import com.mixpanel.android.mpmetrics.MixpanelAPI
import javax.inject.Inject

class Mixpanel @Inject constructor(val context: Context) {
    val mixpanelAPI = MixpanelAPI.getInstance(context, context.getString(R.string.MIXPANEL_KEY))

    fun identifyFunction(identify:String, track:String){
        mixpanelAPI.identify(identify)
        mixpanelAPI.track(track)
    }

}