package com.emproto.hoabl.feature.home.login

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ActivityProfileBinding
import com.emproto.hoabl.feature.profile.ProfileSecondFragment


class ProfileActivity : AppCompatActivity() {
    private lateinit var Binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        initView()
    }
    private fun initView() {
        addFragment(ProfileSecondFragment(), true)
    }


    fun addFragment(fragment: Fragment, showAnimation: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (!showAnimation) {
            fragmentTransaction.add(R.id.fragmentContainerProfile,
                fragment,
                fragment.javaClass.name)
                .addToBackStack(fragment.javaClass.name).commit()
        } else {
            fragmentTransaction.add(R.id.fragmentContainerProfile2,
                fragment,
                fragment.javaClass.name)
                .addToBackStack(fragment.javaClass.name).commit()
        }
    }
    fun replaceFragment(fragment: Fragment, showAnimation: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (!showAnimation) {
            fragmentTransaction.replace(
                R.id.fragmentContainerProfile,
                fragment,
                fragment.javaClass.name
            ).addToBackStack(fragment.javaClass.name).commit()
        } else {
            fragmentTransaction.replace(R.id.fragmentContainerProfile2, fragment, fragment.javaClass.name)
                .addToBackStack(fragment.javaClass.name).commit()
        }
    }
}
//    private fun initView() {
//        addFragment(ProfileSecondFragment(), true)
//
//        Log.d("Fragment Added", "fragmment added")
//    }



//class ProfileActivity:AppCompatActivity() {
//    private lateinit var tvDatePicker: EditText
//    private lateinit var button: Button
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
//        tvDatePicker = findViewById(R.id.tvDatePicker)
//
//
//
//
//        val myCalender = Calendar.getInstance()
//        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
//            myCalender.set(Calendar.YEAR, year)
//            myCalender.set(Calendar.MONTH, month)
//            myCalender.set(Calendar.DAY_OF_MONTH, dayofMonth)
//            updateLable(myCalender)
//        }
//
//        tvDatePicker.setOnFocusChangeListener(object :View.OnFocusChangeListener{
//            override fun onFocusChange(p0: View?, p1: Boolean) {
//                if (p1) {
//                    DatePickerDialog(this@ProfileActivity,
//                        datePicker,
//                        myCalender.get(Calendar.YEAR),
//                        myCalender.get(Calendar.MONTH),
//                        myCalender.get(Calendar.DAY_OF_MONTH)).show()
//                }
//            }})
//        tvDatePicker.setOnClickListener {
//            DatePickerDialog(this,
//                datePicker,
//                myCalender.get(Calendar.YEAR),
//                myCalender.get(Calendar.MONTH),
//                myCalender.get(Calendar.DAY_OF_MONTH)).show()
//        }
//        val button = findViewById<Button>(R.id.save_and_update)
//        save_and_update.setOnClickListener{
////            val intent = Intent(this, AccountDetails::class.java)
//            startActivity(intent)
//        }

//            object : View.OnFocusChangeListener() {
//            override fun onFocusChange(v: View?, hasFocus: Boolean) {
//                if (hasFocus) {
//                    DatePickerDialog(this@ProfileActivity,
//                        datePicker,
//                        myCalender.get(Calendar.YEAR),
//                        myCalender.get(Calendar.MONTH),
//                        myCalender.get(Calendar.DAY_OF_MONTH)).show()
//                }
//            }
//        })
//        tvDatePicker.setOnClickListener {
//            DatePickerDialog(this,
//                datePicker,
//                myCalender.get(Calendar.YEAR),
//                myCalender.get(Calendar.MONTH),
//                myCalender.get(Calendar.DAY_OF_MONTH)).show()
//        }
//    }
//
//
//    private fun updateLable(myCalendar: Calendar) {
//        val myFormat = "dd-MM-yyyy"
//        val sdf = SimpleDateFormat(myFormat, Locale.UK)
//        tvDatePicker.setText(sdf.format(myCalendar.time))
//    }
//}

