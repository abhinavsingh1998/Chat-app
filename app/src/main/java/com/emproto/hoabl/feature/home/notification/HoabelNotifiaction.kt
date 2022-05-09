package com.emproto.hoabl.feature.home.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentHoabelPromisesBinding
import com.emproto.hoabl.databinding.FragmentNotificationBottomSheetBinding

import com.emproto.hoabl.feature.home.notification.adapter.NotificationAdapter
import com.emproto.hoabl.feature.home.notification.data.NotificationDataModel
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class HoabelNotifiaction : BaseFragment(){
lateinit var binding:FragmentNotificationBottomSheetBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    var data = ArrayList<NotificationDataModel>()
    val bundle = Bundle()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    binding= FragmentNotificationBottomSheetBinding.inflate(inflater, container,false)
    (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=false
    (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.toolbarLayout.isVisible= false

        // Inflate the layout for this fragment
        view?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val bottomSheet = (bottomSheetDialog as BottomSheetDialog).findViewById<View>(R.id.locUXView) as LinearLayout
                BottomSheetBehavior.from<View>(bottomSheet).apply {
                    peekHeight = 100
                }

                view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })

    return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (i in 1..20) {
            data.add(NotificationDataModel(R.drawable.img, "Notification Topic 1","It is a long established fact that a reader will be distracted ","1h"))
            Log.i("msg","data")
        }

        val customAdapter = NotificationAdapter(context, data)
        val recyclerview = requireView().findViewById<RecyclerView>(R.id.rv)
        recyclerview?.apply {
            adapter = customAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }



    }
}