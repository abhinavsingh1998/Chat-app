package com.emproto.hoabl.feature.profile

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.databinding.FragmentProfileSecondBinding

import java.util.*


class ProfileSecondFragment : Fragment() {
    lateinit var binding: FragmentProfileSecondBinding
    private lateinit var tvDatePicker: EditText
    private lateinit var button: Button
    private lateinit var arrowimage: ImageView
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileSecondBinding.inflate(inflater, container, false)

        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =false


        val myCalender = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
            myCalender.set(Calendar.YEAR, year)
            myCalender.set(Calendar.MONTH, month)
            myCalender.set(Calendar.DAY_OF_MONTH, dayofMonth)
        }
        initClickListener()
        return binding.root
    }

    private fun initClickListener() {
        binding.arrowimage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val profileFragment = ProfileFragment()
                (requireActivity() as HomeActivity).replaceFragment(profileFragment.javaClass,
                    "",
                    true,
                    bundle,
                    null,
                    0,
                    false)
            }
        })

    }
}







