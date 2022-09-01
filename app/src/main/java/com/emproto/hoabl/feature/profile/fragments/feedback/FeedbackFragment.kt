package com.emproto.hoabl.feature.profile.fragments.feedback

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentFeedbackBinding
import com.emproto.hoabl.databinding.FragmentFeedbackBinding.*
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

    var lightface: Typeface? = null
    var boldface: Typeface? = null

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
        binding = inflate(inflater, container, false)
        (requireActivity() as HomeActivity).hideBottomNavigation()
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel =
            ViewModelProvider(requireActivity(), factory)[ProfileViewModel::class.java]

        initClickListener()
        categories()
        return binding.root
    }

    private fun initClickListener() {
        ratingsStars()
        binding.backAction.setOnClickListener(this)
        binding.shareYourFeedback.setOnClickListener(this)

        description = ""
        binding.experienceTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                description = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                description = p0.toString()
                if (p0.toString().length == 250) {
                    binding.editIssuesLayout.boxStrokeColor =
                        resources.getColor(R.color.text_red_color)
                    binding.txtcount.isVisible = true
                    binding.maxDesc.isVisible = false
                    binding.experienceTv.setTextColor(resources.getColorStateList(R.color.text_red_color))
                } else {
                    binding.editIssuesLayout.boxStrokeColor = resources.getColor(R.color.app_color)
                    binding.txtcount.isVisible = false
                    binding.maxDesc.isVisible = true
                    binding.maxDesc.text = "${description.length}/250"+" Characters"
                    binding.experienceTv.setTextColor(resources.getColorStateList(R.color.text_color))
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNullOrEmpty()) {
                    description = p0.toString()
                }

            }

        })
    }

    private fun categories() {
        binding.homeCheckbox.setOnClickListener(this)
        binding.checkboxInvest.setOnClickListener(this)
        binding.checkboxPortfolio.setOnClickListener(this)
        binding.checkboxPromises.setOnClickListener(this)
        binding.checkboxProfile.setOnClickListener(this)
        binding.checkboxOther.setOnClickListener(this)
    }

    @SuppressLint("ResourceAsColor")
    private fun ratingsStars() {
        boldface = ResourcesCompat.getFont(requireContext(), R.font.jost_medium)
        lightface = ResourcesCompat.getFont(requireContext(), R.font.jost_light)
        binding.ivRating1.setOnClickListener(this)
        binding.ivRating2.setOnClickListener(this)
        binding.ivRating3.setOnClickListener(this)
        binding.ivRating4.setOnClickListener(this)
        binding.ivRating5.setOnClickListener(this)
    }

    private fun selected5() {
        binding.ivRating5.setImageDrawable(resources.getDrawable(R.drawable.selected5))
        selectedFontAndColor(binding.excelentTxt)
    }

    private fun selected4() {
        binding.ivRating4.setImageDrawable(resources.getDrawable(R.drawable.selected4))
        selectedFontAndColor(binding.goodTxt)
    }

    private fun selected2() {
        binding.ivRating2.setImageDrawable(resources.getDrawable(R.drawable.selected2))
        selectedFontAndColor(binding.badTxt)
    }

    private fun selected3() {
        binding.ivRating3.setImageDrawable(resources.getDrawable(R.drawable.selected3))
        selectedFontAndColor(binding.okTxt)
    }

    private fun selected1() {
        binding.ivRating1.setImageDrawable(resources.getDrawable(R.drawable.selected1))
        selectedFontAndColor(binding.veryPoorTxt)
    }

    private fun selectedFontAndColor(selectedText: TextView) {
        selectedText.setTextColor(resources.getColor(R.color.black))
        selectedText.typeface = boldface
    }

    private fun unselected5() {
        binding.ivRating5.setImageDrawable(resources.getDrawable(R.drawable.unselected5))
        unSelectedFontAndColor(binding.excelentTxt)
    }

    private fun unselected4() {
        binding.ivRating4.setImageDrawable(resources.getDrawable(R.drawable.unselected4))
        unSelectedFontAndColor(binding.goodTxt)
    }

    private fun unselected3() {
        binding.ivRating3.setImageDrawable(resources.getDrawable(R.drawable.unselected3))
        unSelectedFontAndColor(binding.okTxt)
    }

    private fun unselected2() {
        binding.ivRating2.setImageDrawable(resources.getDrawable(R.drawable.unselected2))
        unSelectedFontAndColor(binding.badTxt)
    }

    private fun unselected1() {
        binding.ivRating1.setImageDrawable(resources.getDrawable(R.drawable.unselected1))
        unSelectedFontAndColor(binding.veryPoorTxt)
    }

    private fun unSelectedFontAndColor(unSelectedText: TextView) {
        unSelectedText.setTextColor(resources.getColor(R.color.category_location_ash_color))
        unSelectedText.typeface = lightface
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
            R.id.back_action -> {
                requireActivity().supportFragmentManager.popBackStack()
                hideSoftKeyboard()
            }
            R.id.home_checkbox -> {
                if (binding.homeCheckbox.isChecked) {
                    list.add("5001")
                } else {
                    list.remove("5001")
                }
            }
            R.id.checkbox_invest -> {
                if (binding.checkboxInvest.isChecked) {
                    list.add("5002")
                } else {
                    list.remove("5002")
                }
            }
            R.id.checkbox_portfolio -> {
                if (binding.checkboxPortfolio.isChecked) {
                    list.add("5003")
                } else {
                    list.remove("5003")
                }
            }
            R.id.checkbox_promises -> {
                if (binding.checkboxPromises.isChecked) {
                    list.add("5004")
                } else {
                    list.remove("5004")
                }
            }
            R.id.checkbox_profile -> {
                if (binding.checkboxProfile.isChecked) {
                    list.add("5005")

                } else {
                    list.remove("5005")
                }
            }
            R.id.checkbox_other -> {
                if (binding.checkboxOther.isChecked) {
                    list.add("5006")
                } else {
                    list.remove("5006")
                }
            }

            R.id.iv_rating_1 -> {
                ratings = 1
                selected1()
                unselected2()
                unselected3()
                unselected4()
                unselected5()
            }
            R.id.iv_rating_2 -> {
                ratings = 2
                unselected1()
                selected2()
                unselected3()
                unselected4()
                unselected5()
            }

            R.id.iv_rating_3 -> {
                ratings = 3
                unselected1()
                unselected2()
                selected3()
                unselected4()
                unselected5()
            }
            R.id.iv_rating_4 -> {
                ratings = 4
                unselected1()
                unselected2()
                unselected3()
                selected4()
                unselected5()
            }
            R.id.iv_rating_5 -> {
                ratings = 5
                unselected1()
                unselected2()
                unselected3()
                unselected4()
                selected5()
            }
            R.id.share_your_feedback -> {
                if (ratings != 0 || (description.isNotEmpty()&& description.trim().isNotEmpty())  || list.isNotEmpty()
                ) {
                    feedBackRequest = FeedBackRequest(ratings, list, description)
                    initObserver()
                } else {
                    (requireActivity() as HomeActivity).showErrorToast(
                        "Fill atleast one field"
                    )
                }

            }

        }
    }
}