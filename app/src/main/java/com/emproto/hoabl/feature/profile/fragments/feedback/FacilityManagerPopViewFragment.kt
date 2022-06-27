package com.emproto.hoabl.feature.profile.fragments.feedback

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentFacilityManagerPopBinding
import com.emproto.hoabl.feature.home.views.HomeActivity

class FacilityManagerPopViewFragment:DialogFragment() {
    lateinit var binding: FragmentFacilityManagerPopBinding
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
       var dialog = Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        var window = dialog.getWindow();
        window?.setBackgroundDrawableResource(android.R.color.transparent);
        window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.fragment_facility_manager_pop);

        FragmentFacilityManagerPopBinding.inflate(inflater, container, false)
        binding = FragmentFacilityManagerPopBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            false
        initClickListener()
        return binding.root

    }

    private fun initClickListener() {
        binding.actionOk.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
               requireActivity().supportFragmentManager.popBackStack()
            }
        })

    }
}