package com.emproto.hoabl.feature.profileui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.emproto.hoabl.R

class FacilityManagerPopViewFragment:DialogFragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container:ViewGroup?,
        savedInstanceState:Bundle?
    ):View?{
        var rootView:View= inflater.inflate(R.layout.fragment_facility_manager_pop,container,false)
        return  rootView
    }

}