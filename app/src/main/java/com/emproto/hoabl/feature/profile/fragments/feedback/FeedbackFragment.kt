package com.emproto.hoabl.feature.profile.fragments.feedback

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentFeedbackBinding
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.request.profile.FeedBackRequest
import com.emproto.networklayer.response.BaseResponse
import com.emproto.networklayer.response.enums.Status
import com.emproto.networklayer.response.profile.FeedBackResponse
import javax.inject.Inject


class FeedbackFragment : BaseFragment(), View.OnClickListener {

    @Inject
    lateinit var factory: ProfileFactory
    lateinit var profileViewModel: ProfileViewModel
    lateinit var feedBackRequest: FeedBackRequest
    lateinit var description: String

    var ratings = 0
    lateinit var binding: FragmentFeedbackBinding
    val bundle = Bundle()
    var list = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            false

        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel =
            ViewModelProvider(requireActivity(), factory)[ProfileViewModel::class.java]


        initView()

        return binding.root

    }

    private fun initView() {

        initClickListener()
        catagories()
    }

    private fun initClickListener() {

        ratingsStars()
        binding.backAction.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        })
        description = ""

        binding.experienceTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                description = p0.toString()

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                description = p0.toString()
                if (p0.toString().length == 250) {
                    binding.maxDesc.text = "250/250 Characters"
                    binding.maxDesc.setTextColor(resources.getColor(R.color.text_red_color))
                    binding.experienceTv.setTextColor(resources.getColor(R.color.text_red_color))

                } else {
                    binding.maxDesc.text = "0/250 Characters"
                    binding.maxDesc.setTextColor(resources.getColor(R.color.text_light_grey_color))
                    binding.experienceTv.setTextColor(resources.getColor(R.color.land_skus_text_black_color))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNullOrEmpty()) {
                    description = p0.toString()

                }

            }

        })

        binding.shareYourFeedback.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (ratings != 0 || description.isNotEmpty() || list.isNotEmpty()) {
                    feedBackRequest = FeedBackRequest(ratings, list, description)
                    initObserver()
                } else {
                    (requireActivity() as HomeActivity).showErrorToast(
                        "Fill atleast one field"
                    )
                }
            }
        })
    }

    private fun catagories() {
        binding.homeCheckbox.setOnClickListener(this)
        binding.checkboxInvest.setOnClickListener(this)
        binding.checkboxPortfolio.setOnClickListener(this)
        binding.checkboxPromises.setOnClickListener(this)
        binding.checkboxProfile.setOnClickListener(this)
        binding.checkboxOther.setOnClickListener(this)

    }


    @SuppressLint("ResourceAsColor")
    private fun ratingsStars() {
        val boldface = ResourcesCompat.getFont(requireContext(), R.font.jost_medium)
        val lightface = ResourcesCompat.getFont(requireContext(), R.font.jost_light)
        binding.ivRating1.setOnClickListener(View.OnClickListener {
            ratings = 1
            binding.ivRating1.setImageDrawable(resources.getDrawable(R.drawable.selected1))
            binding.veryPoorTxt.setTextColor(resources.getColor(R.color.black))
            binding.veryPoorTxt.typeface = boldface

            binding.ivRating2.setImageDrawable(resources.getDrawable(R.drawable.unselected2))
            binding.badTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.badTxt.typeface = lightface

            binding.ivRating3.setImageDrawable(resources.getDrawable(R.drawable.unselected3))
            binding.okTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.okTxt.typeface = lightface

            binding.ivRating4.setImageDrawable(resources.getDrawable(R.drawable.unselected4))
            binding.goodTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.goodTxt.typeface = lightface

            binding.ivRating5.setImageDrawable(resources.getDrawable(R.drawable.unselected5))
            binding.excelentTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.excelentTxt.typeface = lightface
        })

        binding.ivRating2.setOnClickListener(View.OnClickListener {
            ratings = 2
            binding.ivRating2.setImageDrawable(resources.getDrawable(R.drawable.selected2))
            binding.badTxt.setTextColor(resources.getColor(R.color.black))
            binding.badTxt.typeface = boldface

            binding.ivRating1.setImageDrawable(resources.getDrawable(R.drawable.unselected1))
            binding.veryPoorTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.veryPoorTxt.typeface = lightface

            binding.ivRating3.setImageDrawable(resources.getDrawable(R.drawable.unselected3))
            binding.okTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.okTxt.typeface = lightface

            binding.ivRating4.setImageDrawable(resources.getDrawable(R.drawable.unselected4))
            binding.goodTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.goodTxt.typeface = lightface

            binding.ivRating5.setImageDrawable(resources.getDrawable(R.drawable.unselected5))
            binding.excelentTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.excelentTxt.typeface = lightface

        })

        binding.ivRating3.setOnClickListener(View.OnClickListener {
            ratings = 3
            binding.ivRating3.setImageDrawable(resources.getDrawable(R.drawable.selected3))
            binding.okTxt.setTextColor(resources.getColor(R.color.black))
            binding.okTxt.typeface = boldface


            binding.ivRating1.setImageDrawable(resources.getDrawable(R.drawable.unselected1))
            binding.veryPoorTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.veryPoorTxt.typeface = lightface

            binding.ivRating2.setImageDrawable(resources.getDrawable(R.drawable.unselected2))
            binding.badTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.badTxt.typeface = lightface

            binding.ivRating4.setImageDrawable(resources.getDrawable(R.drawable.unselected4))
            binding.goodTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.goodTxt.typeface = lightface

            binding.ivRating5.setImageDrawable(resources.getDrawable(R.drawable.unselected5))
            binding.excelentTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.excelentTxt.typeface = lightface
        })

        binding.ivRating4.setOnClickListener(View.OnClickListener {
            ratings = 4
            binding.ivRating4.setImageDrawable(resources.getDrawable(R.drawable.selected4))
            binding.goodTxt.setTextColor(resources.getColor(R.color.black))
            binding.goodTxt.typeface = boldface

            binding.ivRating1.setImageDrawable(resources.getDrawable(R.drawable.unselected1))
            binding.veryPoorTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.veryPoorTxt.typeface = lightface

            binding.ivRating2.setImageDrawable(resources.getDrawable(R.drawable.unselected2))
            binding.badTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.badTxt.typeface = lightface

            binding.ivRating3.setImageDrawable(resources.getDrawable(R.drawable.unselected3))
            binding.okTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.okTxt.typeface = lightface

            binding.ivRating5.setImageDrawable(resources.getDrawable(R.drawable.unselected5))
            binding.excelentTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.excelentTxt.typeface = lightface

        })

        binding.ivRating5.setOnClickListener(View.OnClickListener {
            ratings = 5
            binding.ivRating1.setImageDrawable(resources.getDrawable(R.drawable.unselected1))
            binding.veryPoorTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.veryPoorTxt.typeface = lightface

            binding.ivRating2.setImageDrawable(resources.getDrawable(R.drawable.unselected2))
            binding.badTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.badTxt.typeface = lightface

            binding.ivRating3.setImageDrawable(resources.getDrawable(R.drawable.unselected3))
            binding.okTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.okTxt.typeface = lightface

            binding.ivRating4.setImageDrawable(resources.getDrawable(R.drawable.unselected4))
            binding.goodTxt.setTextColor(resources.getColor(R.color.category_location_ash_color))
            binding.goodTxt.typeface = lightface

            binding.ivRating5.setImageDrawable(resources.getDrawable(R.drawable.selected5))
            binding.excelentTxt.setTextColor(resources.getColor(R.color.black))
            binding.excelentTxt.typeface = boldface
        })
    }

    private fun initObserver() {
        profileViewModel.submitFeedback(feedBackRequest)
            .observe(viewLifecycleOwner, object : Observer<BaseResponse<FeedBackResponse>> {
                override fun onChanged(it: BaseResponse<FeedBackResponse>?) {
                    when (it!!.status) {
                        Status.ERROR -> {
                            binding.progressBar.isVisible = false
                            binding.shareYourFeedback.isVisible = true
                            binding.shareYourFeedback.text = "Share your feedback"
                            (requireActivity() as HomeActivity).showErrorToast(
                                it.message!!
                            )
                        }
                        Status.SUCCESS -> {
                            binding.progressBar.isVisible = false
                            binding.shareYourFeedback.isVisible = true
                            val dialog = FeedBackSubmittedPopup()
                            dialog.isCancelable = false
                            dialog.show(childFragmentManager, "submitted")

                        }
                        Status.LOADING -> {
                            binding.shareYourFeedback.isVisible = false
                            binding.progressBar.isVisible = true
                        }
                    }
                }
            })

    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.home_checkbox -> {
                if (binding.homeCheckbox.isChecked) {
                    list.add("5001")
                } else {
                    list.remove("5001")
                }
            }
            R.id.checkbox_invest->{
                if (binding.checkboxInvest.isChecked) {
                    list.add("5002")
                } else {
                    list.remove("5002")
                }
            }
            R.id.checkbox_portfolio->{
                if (binding.checkboxPortfolio.isChecked) {
                    list.add("5003")
                } else {
                    list.remove("5003")
                }
            }
            R.id.checkbox_promises->{
                if (binding.checkboxPromises.isChecked) {
                    list.add("5004")
                } else {
                    list.remove("5004")
                }
            }
            R.id.checkbox_profile->{
                if (binding.checkboxProfile.isChecked) {
                    list.add("5005")

                } else {
                    list.remove("5005")
                }
            }
            R.id.checkbox_other->{
                if (binding.checkboxOther.isChecked) {
                    list.add("5006")
                } else {
                    list.remove("5006")
                }
            }
        }
    }


}