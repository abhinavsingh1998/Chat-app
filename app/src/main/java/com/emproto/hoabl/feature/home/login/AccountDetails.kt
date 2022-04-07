package com.emproto.hoabl.feature.home.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ActivityAccountdetailsBinding
import com.emproto.hoabl.feature.profile.Account_Details_Fragment


class AccountDetails:AppCompatActivity() {

    private lateinit var activityAccountdetailsBinding: ActivityAccountdetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAccountdetailsBinding= ActivityAccountdetailsBinding.inflate(layoutInflater)
        setContentView(activityAccountdetailsBinding.root)

        initView()
    }

    private fun initView() {
        addFragment(Account_Details_Fragment(), true)
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
//    lateinit var view_pager2: ViewPager2
//
//    private var titlesList = mutableListOf<String>()
//    private var descList = mutableListOf<Int>()
//    private var imagesList = mutableListOf<Int>()
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_accountdetails)
//        view_pager2= findViewById(R.id.view_pager2)
//        postToList()
//        view_pager2.adapter = ViewPagerAdapter(titlesList,descList,imagesList)
//        view_pager2.orientation= ViewPager2.ORIENTATION_HORIZONTAL
//        val indicator= findViewById<CircleIndicator3>(R.id.indicator)
//        indicator.setViewPager(view_pager2)
//        view_pager2.apply {
//            beginFakeDrag()
//            fakeDragBy(-10f)
//            endFakeDrag()
//        }
//
//    }
//
//    private fun addToList(title:String, description:Int, image:Int){
//        titlesList.add(title)
//        descList.add(description)
//        imagesList.add(image)
//    }
//    private fun postToList(){
//        for(i in 1..5){
//            addToList("Document Name $i",
//                R.drawable.ic_combined_shape_copy_5,
//                R.drawable.ic_google_docs_copy)
//        }
//
//    }
//
//
//}
//class AccountDetails : AppCompatActivity() {
//    private lateinit var Binding: ActivityAccountdetailsBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Binding = ActivityAccountdetailsBinding.inflate(layoutInflater)
//        setContentView(Binding.root)
//initView()
//    }
//    private fun initView() {
//        addFragment(Account_Details_Fragment(), true)
//    }
//
//
//    fun addFragment(fragment: Fragment, showAnimation: Boolean) {
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        if (!showAnimation) {
//            fragmentTransaction.add(R.id.fragmentContainerAccount,
//                fragment,
//                fragment.javaClass.name)
//                .addToBackStack(fragment.javaClass.name).commit()
//        } else {
//            fragmentTransaction.add(R.id.fragmentContainerAccount,
//                fragment,
//                fragment.javaClass.name)
//                .addToBackStack(fragment.javaClass.name).commit()
//        }
//    }
//    fun replaceFragment(fragment: Fragment, showAnimation: Boolean) {
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        if (!showAnimation) {
//            fragmentTransaction.replace(
//                R.id.fragmentContainerProfile,
//                fragment,
//                fragment.javaClass.name
//            ).addToBackStack(fragment.javaClass.name).commit()
//        } else {
//            fragmentTransaction.replace(R.id.fragmentContainerProfile, fragment, fragment.javaClass.name)
//                .addToBackStack(fragment.javaClass.name).commit()
//        }
//    }
//
//}
