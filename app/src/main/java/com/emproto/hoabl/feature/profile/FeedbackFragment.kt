package com.emproto.hoabl.feature.profile

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentAboutUsBinding
import com.emproto.hoabl.databinding.FragmentFeedbackBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.HomeViewModel
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.HomeFactory
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.request.profile.FeedBackRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.FeedBackResponse
import com.google.android.youtube.player.internal.v
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class FeedbackFragment : BaseFragment() {

    @Inject
    lateinit var factory: ProfileFactory
    lateinit var profileViewModel: ProfileViewModel
     var feedBackRequest: FeedBackRequest? = null
    lateinit var description:String

    var ratings=0
    lateinit var binding: FragmentFeedbackBinding
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentFeedbackBinding.inflate(inflater,container,false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible=false

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel =
            ViewModelProvider(requireActivity(), factory)[ProfileViewModel::class.java]

        initView()

        return binding.root

    }

    private fun initView(){

        initClickListener()
    }

    private fun initClickListener() {

        ratingsStars()
        binding.backAction.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        })
        description=""

        binding.experienceTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                description=p0.toString()

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                description=p0.toString()
                if( p0.toString().length==250){
                    binding.maxDesc.text= "250/250 Characters"
                    binding.maxDesc.setTextColor(resources.getColor(R.color.text_red_color))
                    binding.experienceTv.setTextColor(resources.getColor(R.color.text_red_color))

                }
                else{
                    binding.maxDesc.text= "0/250 Characters"
                    binding.maxDesc.setTextColor(resources.getColor(R.color.text_light_grey_color))
                    binding.experienceTv.setTextColor(resources.getColor(R.color.land_skus_text_black_color))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNullOrEmpty()) {
                    description=p0.toString()

                }

            }

        })

        binding.shareYourFeedback.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                if(feedBackRequest!=null){
                    initObserver()
                }
            }
        })


    }

    private fun catagories(): ArrayList<String>{

        var list = ArrayList<String>()

        binding.homeCheckbox.setOnClickListener(View.OnClickListener {
            if(binding.homeCheckbox.isChecked){
                list.add("5001")
            } else{
                list.remove("5001")
            }
        })

        binding.checkboxInvest.setOnClickListener(View.OnClickListener {
            if(binding.checkboxInvest.isChecked){
                list.add("5002")
            } else{
                list.remove("5002")
            }
        })

        binding.checkboxPortfolio.setOnClickListener(View.OnClickListener {
            if(binding.checkboxPortfolio.isChecked){
                list.add("5003")
            } else{
                list.remove("5003")
            }
        })

        binding.checkboxPromises.setOnClickListener(View.OnClickListener {
            if(binding.checkboxPromises.isChecked){
                list.add("5004")
            } else{
                list.remove("5004")
            }
        })

        binding.checkboxProfile.setOnClickListener(View.OnClickListener {
            if(binding.checkboxProfile.isChecked){
                list.add("5005")

            } else{
                list.remove("5005")
            }
        })

        binding.checkboxOther.setOnClickListener(View.OnClickListener {
            if(binding.checkboxOther.isChecked){
                list.add("5006")
            } else{
                list.remove("5006")
            }
        })
        return list
    }



    @SuppressLint("ResourceAsColor")
    private fun ratingsStars() {

        val boldface = ResourcesCompat.getFont(requireContext(), R.font.jost_medium)
        val lightface = ResourcesCompat.getFont(requireContext(), R.font.jost_light)



        binding.ivRating1.setOnClickListener(View.OnClickListener {
            ratings= 1
            feedBackRequest= FeedBackRequest(
                ratings,
                catagories(),
                description
            )
            binding.ivRating1.background= resources.getDrawable(R.drawable.emoji_verysad)
            binding.veryPoorTxt.setTextColor(resources.getColor(R.color.black))
            binding.veryPoorTxt.typeface= boldface

            binding.ivRating2.background= resources.getDrawable(R.drawable.bad)
            binding.badTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.badTxt.typeface= lightface

            binding.ivRating3.background= resources.getDrawable(R.drawable.confused)
            binding.okTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.okTxt.typeface= lightface

            binding.ivRating4.background= resources.getDrawable(R.drawable.happy)
            binding.goodTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.goodTxt.typeface= lightface

            binding.ivRating5.background= resources.getDrawable(R.drawable.in_love)
            binding.excelentTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.excelentTxt.typeface= lightface
        })

        binding.ivRating2.setOnClickListener(View.OnClickListener {
            ratings= 2
            feedBackRequest= FeedBackRequest(
                ratings,
                catagories(),
                description
            )

            binding.ivRating2.background= resources.getDrawable(R.drawable.emoji_sad)
            binding.badTxt.setTextColor(resources.getColor(R.color.black))
            binding.badTxt.typeface= boldface

            binding.ivRating1.background= resources.getDrawable(R.drawable.sad_2)
            binding.veryPoorTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.veryPoorTxt.typeface= lightface

            binding.ivRating3.background= resources.getDrawable(R.drawable.confused)
            binding.okTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.okTxt.typeface= lightface

            binding.ivRating4.background= resources.getDrawable(R.drawable.happy)
            binding.goodTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.goodTxt.typeface= lightface

            binding.ivRating5.background= resources.getDrawable(R.drawable.in_love)
            binding.excelentTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.excelentTxt.typeface= lightface

        })

        binding.ivRating3.setOnClickListener(View.OnClickListener {
            ratings= 3
            feedBackRequest= FeedBackRequest(
                ratings,
                catagories(),
                description
            )
            binding.ivRating3.background= resources.getDrawable(R.drawable.emoji_confused)
            binding.okTxt.setTextColor(resources.getColor(R.color.black))
            binding.okTxt.typeface= boldface


            binding.ivRating1.background= resources.getDrawable(R.drawable.sad_2)
            binding.veryPoorTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.veryPoorTxt.typeface= lightface

            binding.ivRating2.background= resources.getDrawable(R.drawable.bad)
            binding.badTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.badTxt.typeface= lightface

            binding.ivRating4.background= resources.getDrawable(R.drawable.happy)
            binding.goodTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.goodTxt.typeface= lightface

            binding.ivRating5.background= resources.getDrawable(R.drawable.in_love)
            binding.excelentTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.excelentTxt.typeface= lightface
        })

        binding.ivRating4.setOnClickListener(View.OnClickListener {
            ratings= 4
            feedBackRequest= FeedBackRequest(
                ratings,
                catagories(),
                description
            )
            binding.ivRating4.background= resources.getDrawable(R.drawable.emoji_happy)
            binding.goodTxt.setTextColor(resources.getColor(R.color.black))
            binding.goodTxt.typeface= boldface

            binding.ivRating1.background= resources.getDrawable(R.drawable.sad_2)
            binding.veryPoorTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.veryPoorTxt.typeface= lightface

            binding.ivRating2.background= resources.getDrawable(R.drawable.bad)
            binding.badTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.badTxt.typeface= lightface

            binding.ivRating3.background= resources.getDrawable(R.drawable.confused)
            binding.okTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.okTxt.typeface= lightface

            binding.ivRating5.background= resources.getDrawable(R.drawable.in_love)
            binding.excelentTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.excelentTxt.typeface= lightface

        })

        binding.ivRating5.setOnClickListener(View.OnClickListener {
            ratings= 5
            feedBackRequest= FeedBackRequest(
                ratings,
                catagories(),
                description
            )

            binding.ivRating1.background= resources.getDrawable(R.drawable.sad_2)
            binding.veryPoorTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.veryPoorTxt.typeface= lightface

            binding.ivRating2.background= resources.getDrawable(R.drawable.bad)
            binding.badTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.badTxt.typeface= lightface

            binding.ivRating3.background= resources.getDrawable(R.drawable.confused)
            binding.okTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.okTxt.typeface= lightface

            binding.ivRating4.background= resources.getDrawable(R.drawable.happy)
            binding.goodTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.goodTxt.typeface= lightface

            binding.ivRating5.background= resources.getDrawable(R.drawable.emoji_love)
            binding.excelentTxt.setTextColor(resources.getColor(R.color.black))
            binding.excelentTxt.typeface= boldface

        })
    }



    private fun initObserver(){
        feedBackRequest?.let {
            profileViewModel.submitFeedback(it).observe(viewLifecycleOwner, object : Observer<BaseResponse<FeedBackResponse>>{
                override fun onChanged(it: BaseResponse<FeedBackResponse>?) {
                    when (it!!.status) {
                        Status.ERROR -> {
                            binding.progressBar.isVisible= false
                            binding.shareYourFeedback.isVisible= true
                            binding.shareYourFeedback.text= "Share your feedback"
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                        }

                        Status.SUCCESS ->{

                            binding.progressBar.isVisible= false
                            binding.shareYourFeedback.isVisible= true
                            binding.shareYourFeedback.text= "Submitted"
                    }

                        Status.LOADING ->{
                            binding.shareYourFeedback.isVisible= false
                            binding.progressBar.isVisible= true
                        }
                }
                }
            })
        }
    }
}