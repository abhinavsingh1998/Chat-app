package com.emproto.hoabl.feature.portfolio.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.hoabl.R
import com.example.portfolioui.adapters.TimelineAdapter
import com.example.portfolioui.databinding.FragmentProjectTimelineBinding
import com.example.portfolioui.models.TimelineModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectTimelineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectTimelineFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mBinding: FragmentProjectTimelineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentProjectTimelineBinding.inflate(inflater, container, false)
        initView()
        return mBinding.root
    }

    private fun initView() {
        val timelineList = ArrayList<TimelineModel>()
        timelineList.add(TimelineModel(TimelineAdapter.TYPE_HEADER))
        timelineList.add(TimelineModel(TimelineAdapter.TYPE_LIST))
        timelineList.add(TimelineModel(TimelineAdapter.TYPE_LIST))
        timelineList.add(TimelineModel(TimelineAdapter.TYPE_LIST))
        timelineList.add(TimelineModel(TimelineAdapter.TYPE_LIST))
        timelineList.add(TimelineModel(TimelineAdapter.TYPE_LIST))
        timelineList.add(TimelineModel(TimelineAdapter.TYPE_LIST))

        mBinding.timelineList.layoutManager = LinearLayoutManager(requireContext())
        mBinding.timelineList.adapter = TimelineAdapter(requireContext(), timelineList, null)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProjectTimelineFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProjectTimelineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}