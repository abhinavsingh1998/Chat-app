package com.emproto.hoabl.feature.home.login

import android.os.Bundle
import com.emproto.core.BaseActivity
import com.emproto.hoabl.databinding.ActivitySignupBinding

class SignUpActivity : BaseActivity() {

    private lateinit var activitySignupBinding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignupBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(activitySignupBinding.root)
        initView()
        initListener()
    }

    private fun initView() {

    }

    private fun initListener() {

    }

}