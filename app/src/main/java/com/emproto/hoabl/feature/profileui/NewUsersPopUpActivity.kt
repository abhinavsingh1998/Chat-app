package com.emproto.hoabl.feature.profileui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.emproto.hoabl.R

class NewUsersPopUpActivity : AppCompatActivity() {
    lateinit var rateTextView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        rateTextView= findViewById(R.id.textview)
        rateTextView.setOnClickListener {
            var dialog= FacilityManagerPopViewFragment()
            dialog.show(supportFragmentManager,"customDialog")

        }

    }
}