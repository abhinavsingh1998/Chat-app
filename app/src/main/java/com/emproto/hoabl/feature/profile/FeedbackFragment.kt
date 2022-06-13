package com.emproto.hoabl.feature.profile

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
    lateinit var feedBackRequest: FeedBackRequest
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
                    binding.experienceTv.error = "You have reached the max characters limit"
                }
                else{
//                    binding.checkboxDesc.isErrorEnabled= false
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
                initObserver()
            }
        })


    }

    private fun catagories(): ArrayList<String>{

        var list = ArrayList<String>()

        binding.homeCheckbox.setOnClickListener(View.OnClickListener {
            if(binding.homeCheckbox.isChecked){
                list.add("5001")
                Toast.makeText(requireContext(), list.toString(), Toast.LENGTH_LONG).show()
            } else{
                list.remove("5001")
            }
        })

        binding.checkboxInvest.setOnClickListener(View.OnClickListener {
            if(binding.checkboxInvest.isChecked){
                list.add("5002")
                Toast.makeText(requireContext(), "hello", Toast.LENGTH_LONG).show()
            } else{
                list.remove("5002")
            }
        })

        binding.checkboxPortfolio.setOnClickListener(View.OnClickListener {
            if(binding.checkboxPortfolio.isChecked){
                list.add("5003")
                Toast.makeText(requireContext(), "hello", Toast.LENGTH_LONG).show()
            } else{
                list.remove("5003")
            }
        })

        binding.checkboxPromises.setOnClickListener(View.OnClickListener {
            if(binding.checkboxPromises.isChecked){
                list.add("5004")
                Toast.makeText(requireContext(), "hello", Toast.LENGTH_LONG).show()
            } else{
                list.remove("5004")
            }
        })

        binding.checkboxProfile.setOnClickListener(View.OnClickListener {
            if(binding.checkboxProfile.isChecked){
                list.add("5005")
                Toast.makeText(requireContext(), "hello", Toast.LENGTH_LONG).show()
            } else{
                list.remove("5005")
            }
        })

        binding.checkboxOther.setOnClickListener(View.OnClickListener {
            if(binding.checkboxOther.isChecked){
                list.add("5006")
                Toast.makeText(requireContext(), "hello", Toast.LENGTH_LONG).show()
            } else{
                list.remove("5006")
            }
        })
        return list
    }



    private fun ratingsStars() {
        binding.ivRating1.setOnClickListener(View.OnClickListener {
            ratings= 1
            feedBackRequest= FeedBackRequest(
                ratings,
                catagories(),
                description
            )
            binding.ivRating1.background= resources.getDrawable(R.drawable.emoji_sad2)
            binding.ivRating2.background= resources.getDrawable(R.drawable.sad)
            binding.ivRating3.background= resources.getDrawable(R.drawable.confused)
            binding.ivRating4.background= resources.getDrawable(R.drawable.happy)
            binding.ivRating5.background= resources.getDrawable(R.drawable.sad)
        })

        binding.ivRating2.setOnClickListener(View.OnClickListener {
            ratings= 2
            feedBackRequest= FeedBackRequest(
                ratings,
                catagories(),
                description
            )

            binding.ivRating2.background= resources.getDrawable(R.drawable.emoji_sad)
            binding.ivRating1.background= resources.getDrawable(R.drawable.sad_2)
            binding.ivRating3.background= resources.getDrawable(R.drawable.confused)
            binding.ivRating4.background= resources.getDrawable(R.drawable.happy)
            binding.ivRating5.background= resources.getDrawable(R.drawable.sad)

        })

        binding.ivRating3.setOnClickListener(View.OnClickListener {
            ratings= 3
            feedBackRequest= FeedBackRequest(
                ratings,
                catagories(),
                description
            )
            binding.ivRating3.background= resources.getDrawable(R.drawable.emoji_confused)
            binding.ivRating1.background= resources.getDrawable(R.drawable.sad_2)
            binding.ivRating4.background= resources.getDrawable(R.drawable.happy)
            binding.ivRating5.background= resources.getDrawable(R.drawable.sad)
        })

        binding.ivRating4.setOnClickListener(View.OnClickListener {
            ratings= 4
            feedBackRequest= FeedBackRequest(
                ratings,
                catagories(),
                description
            )
            binding.ivRating4.background= resources.getDrawable(R.drawable.emoji_happy)
            binding.ivRating1.background= resources.getDrawable(R.drawable.sad_2)
            binding.ivRating3.background= resources.getDrawable(R.drawable.confused)
            binding.ivRating5.background= resources.getDrawable(R.drawable.sad)
        })

        binding.ivRating5.setOnClickListener(View.OnClickListener {
            ratings= 5
            feedBackRequest= FeedBackRequest(
                ratings,
                catagories(),
                description
            )
        })
    }



    private fun initObserver(){
        profileViewModel.submitFeedback(feedBackRequest).observe(viewLifecycleOwner, object : Observer<BaseResponse<FeedBackResponse>>{
            override fun onChanged(it: BaseResponse<FeedBackResponse>?)
            {
                when (it!!.status){
                    Status.ERROR ->{
                        (requireActivity() as HomeActivity).showErrorToast(
                            it.message!!
                        )
                    }

                }
                when(it!!.status){
                    Status.SUCCESS ->{
                        Toast.makeText(requireContext(), feedBackRequest.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}