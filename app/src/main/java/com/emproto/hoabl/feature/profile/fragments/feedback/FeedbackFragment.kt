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
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.emproto.core.BaseFragment
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentFeedbackBinding
import com.emproto.hoabl.databinding.FragmentFeedbackBinding.inflate
import com.emproto.hoabl.di.HomeComponentProvider
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.feature.home.views.Mixpanel
import com.emproto.hoabl.viewmodels.ProfileViewModel
import com.emproto.hoabl.viewmodels.factory.ProfileFactory
import com.emproto.networklayer.preferences.AppPreference
import com.emproto.networklayer.request.profile.FeedBackRequest
import com.emproto.networklayer.response.enums.Status
import javax.inject.Inject


class FeedbackFragment : BaseFragment(), View.OnClickListener {

    private var lightface: Typeface? = null
    private var boldface: Typeface? = null

    @Inject
    lateinit var factory: ProfileFactory
    @Inject
    lateinit var appPreference:AppPreference

    lateinit var profileViewModel: ProfileViewModel
    private lateinit var feedBackRequest: FeedBackRequest
    lateinit var description: String

    private var ratings = 0
    lateinit var binding: FragmentFeedbackBinding
    val bundle = Bundle()
    var list = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = inflate(inflater, container, false)
        (requireActivity() as HomeActivity).hideBottomNavigation()
        (requireActivity().application as HomeComponentProvider).homeComponent().inject(this)
        profileViewModel =
            ViewModelProvider(requireActivity(), factory)[ProfileViewModel::class.java]
        initClickListener()
        categories()
        return binding.root
    }

    private fun eventTrackingFeedback() {
        Mixpanel(requireContext()).identifyFunction(appPreference.getMobilenum(), Mixpanel.FEEDBACK)
    }

    private fun initClickListener() {
        ratingsStars()
        binding.backAction.setOnClickListener(this)
        binding.shareYourFeedback.setOnClickListener(this)
        description = ""
        binding.experienceTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                description = p0.toString().trim()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                description = p0.toString().trim()
                if (description.length == 250) {
                    binding.editIssuesLayout.boxStrokeColor =
                        ContextCompat.getColor(context!!, R.color.text_red_color)
                    binding.txtcount.isVisible = true
                    binding.maxDesc.isVisible = false
                    binding.experienceTv.setTextColor(
                        ContextCompat.getColorStateList(
                            context!!,
                            R.color.text_red_color
                        )
                    )
                } else {
                    binding.editIssuesLayout.boxStrokeColor =
                        context?.let { ContextCompat.getColor(it, R.color.app_color) }!!
                    binding.txtcount.isVisible = false
                    binding.maxDesc.isVisible = true
                    "${description.length}/250  Characters".also { binding.maxDesc.text = it }
                    with(binding) {
                        experienceTv.setTextColor(
                            ContextCompat.getColorStateList(
                                context!!, R.color.text_color
                            )
                        )
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isEmpty()) {
                    description = p0.toString().trim()
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
        binding.ivRating5.setImageDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.selected5
            )
        })
        selectedFontAndColor(binding.excelentTxt)
    }

    private fun selected4() {
        binding.ivRating4.setImageDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.selected4
            )
        })
        selectedFontAndColor(binding.goodTxt)
    }

    private fun selected2() {
        binding.ivRating2.setImageDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.selected2
            )
        })
        selectedFontAndColor(binding.badTxt)
    }

    private fun selected3() {
        binding.ivRating3.setImageDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.selected3
            )
        })
        selectedFontAndColor(binding.okTxt)
    }

    private fun selected1() {
        binding.ivRating1.setImageDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.selected1
            )
        })
        selectedFontAndColor(binding.veryPoorTxt)
    }

    private fun selectedFontAndColor(selectedText: TextView) {
        selectedText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        selectedText.typeface = boldface
    }

    private fun unselected5() {
        binding.ivRating5.setImageDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.unselected5
            )
        })
        unSelectedFontAndColor(binding.excelentTxt)
    }

    private fun unselected4() {
        binding.ivRating4.setImageDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.unselected4
            )
        })
        unSelectedFontAndColor(binding.goodTxt)
    }

    private fun unselected3() {
        binding.ivRating3.setImageDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.unselected3
            )
        })
        unSelectedFontAndColor(binding.okTxt)
    }

    private fun unselected2() {
        binding.ivRating2.setImageDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.unselected2
            )
        })
        unSelectedFontAndColor(binding.badTxt)
    }

    private fun unselected1() {
        binding.ivRating1.setImageDrawable(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.unselected1
            )
        })
        unSelectedFontAndColor(binding.veryPoorTxt)
    }

    private fun unSelectedFontAndColor(unSelectedText: TextView) {
        unSelectedText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.category_location_ash_color
            )
        )
        unSelectedText.typeface = lightface
    }

    private fun initObserver() {
        profileViewModel.submitFeedback(feedBackRequest)
            .observe(
                viewLifecycleOwner
            ) {
                when (it!!.status) {
                    Status.ERROR -> {
                        binding.progressBar.isVisible = false
                        binding.shareYourFeedback.isVisible = true
                        binding.shareYourFeedback.text =
                            requireContext().getString(R.string.share_your_feedback)
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
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.back_action -> {
                requireActivity().supportFragmentManager.popBackStack()
                hideSoftKeyboard()
            }
            R.id.home_checkbox -> {
                if (binding.homeCheckbox.isChecked) {
                    list.add(Constants.FIVE_THOUSAND_ONE)
                } else {
                    list.remove(Constants.FIVE_THOUSAND_ONE)
                }
            }
            R.id.checkbox_invest -> {
                if (binding.checkboxInvest.isChecked) {
                    list.add(Constants.FIVE_THOUSAND_TWO)
                } else {
                    list.remove(Constants.FIVE_THOUSAND_TWO)
                }
            }
            R.id.checkbox_portfolio -> {
                if (binding.checkboxPortfolio.isChecked) {
                    list.add(Constants.FIVE_THOUSAND_FOUR)
                } else {
                    list.remove(Constants.FIVE_THOUSAND_FOUR)
                }
            }
            R.id.checkbox_promises -> {
                if (binding.checkboxPromises.isChecked) {
                    list.add(Constants.FIVE_THOUSAND_THREE)
                } else {
                    list.remove(Constants.FIVE_THOUSAND_THREE)
                }
            }
            R.id.checkbox_profile -> {
                if (binding.checkboxProfile.isChecked) {
                    list.add(Constants.FIVE_THOUSAND_FIVE)

                } else {
                    list.remove(Constants.FIVE_THOUSAND_FIVE)
                }
            }
            R.id.checkbox_other -> {
                if (binding.checkboxOther.isChecked) {
                    list.add(Constants.FIVE_THOUSAND_SIX)
                } else {
                    list.remove(Constants.FIVE_THOUSAND_SIX)
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
                eventTrackingFeedback()
                if (ratings != 0 || (description.isNotEmpty() && description.trim()
                        .isNotEmpty()) || list.isNotEmpty()
                ) {
                    feedBackRequest = FeedBackRequest(ratings, list, description)
                    initObserver()
                } else {
                    (requireActivity() as HomeActivity).showErrorToast(
                        "Fill Atleast One field"
                    )
                }

            }

        }
    }
}