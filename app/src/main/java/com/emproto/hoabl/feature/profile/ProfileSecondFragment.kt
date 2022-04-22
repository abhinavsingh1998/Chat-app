package com.emproto.hoabl.feature.profile

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isVisible
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.databinding.FragmentProfileSecondBinding
import java.text.SimpleDateFormat

import java.util.*


class ProfileSecondFragment : Fragment() {
    lateinit var binding: FragmentProfileSecondBinding
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
            updateLable(myCalender)
        }
        binding.tvDatePicker.setOnFocusChangeListener(object :View.OnFocusChangeListener{
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p1) {
                    context?.let {
                        DatePickerDialog(it,
                            datePicker,
                            myCalender.get(Calendar.YEAR),
                            myCalender.get(Calendar.MONTH),
                            myCalender.get(Calendar.DAY_OF_MONTH)).show()
                    }
                }
            }})
        binding.tvDatePicker.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(it1,
                    datePicker,
                    myCalender.get(Calendar.YEAR),
                    myCalender.get(Calendar.MONTH),
                    myCalender.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        initClickListener()
        return binding.root
    }
    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.tvDatePicker.setText(sdf.format(myCalendar.time))
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







