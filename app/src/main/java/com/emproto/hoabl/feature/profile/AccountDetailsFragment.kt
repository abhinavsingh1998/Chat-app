package com.emproto.hoabl.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.emproto.hoabl.feature.home.views.HomeActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.FragmentAccountDetailsBinding
import com.emproto.hoabl.databinding.FragmentProfileMainBinding
import com.emproto.hoabl.feature.profile.adapter.AccountDetailsFragmentAdapter
import com.emproto.hoabl.feature.profile.data.AccountDetailsData

class AccountDetailsFragment : Fragment() {
    lateinit var binding: FragmentAccountDetailsBinding
    lateinit var adapter: AccountDetailsFragmentAdapter
    private var titleList = mutableListOf<String>()
    private var docsList = mutableListOf<String>()
    private var imageList = mutableListOf<String>()

    lateinit var account_details_recycler_view: RecyclerView
    lateinit var imgArrow: ImageView
    val bundle = Bundle()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        FragmentProfileMainBinding.inflate(inflater, container, false)
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        binding.accountDetailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val detailAdapter = AccountDetailsFragmentAdapter(initData())
        binding.accountDetailsRecyclerView.adapter = detailAdapter
        (requireActivity() as HomeActivity).activityHomeActivity.includeNavigation.bottomNavigation.isVisible =
            true
        initClickListener()

        return binding.root
    }

    private fun initClickListener() {
        binding.imgArrow.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val profileFragment = ProfileFragment()
                (requireActivity() as HomeActivity).replaceFragment(
                    profileFragment.javaClass,
                    "",
                    true,
                    bundle,
                    null,
                    0,
                    false
                )
            }
        })

    }

    private fun initData(): ArrayList<AccountDetailsData> {
        val accountdetailsList: ArrayList<AccountDetailsData> = ArrayList<AccountDetailsData>()
        accountdetailsList.add(
            AccountDetailsData(
                "Isle of Bliss", " 11th March 2022",
                "See receipt", R.drawable.rupee_icon, "Payment Milestone 2:", "₹20,000"
            )
        )
        accountdetailsList.add(
            AccountDetailsData(
                "Yellow Meadow",
                "10th March 2022",
                "See receipt",
                R.drawable.rupee_icon,
                "Payment Milestone 4:",
                "₹80,000"
            )
        )
        accountdetailsList.add(
            AccountDetailsData(
                "Yellow Meadow",
                "10th March 2022",
                "See receipt",
                R.drawable.rupee_icon,
                "Payment Milestone 4:",
                "₹80,000"
            )
        )
        accountdetailsList.add(
            AccountDetailsData(
                "Yellow Meadow",
                "10th March 2022",
                "See receipt",
                R.drawable.rupee_icon,
                "Payment Milestone 4:",
                "₹80,000"
            )
        )

        return accountdetailsList
    }

}