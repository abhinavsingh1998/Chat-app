package com.emproto.hoabl.feature.home.views.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.core.Database.TableModel.SearchModel
import com.emproto.hoabl.HomeActivity
import com.emproto.hoabl.databinding.FragmentSearchResultBinding
import com.emproto.hoabl.feature.home.adapters.SearchResultAdapter

class SearchResultFragment : BaseFragment() {

    lateinit var fragmentSearchResultBinding: FragmentSearchResultBinding

    /*lateinit var homeViewModel:HomeViewModel
    @Inject
    lateinit var homeFactory: HomeFactory*/

    lateinit var searchResultAdapter: SearchResultAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    companion object{
        fun newInstance(): SearchResultFragment {
            val fragment = SearchResultFragment()
            /*val bundle = Bundle()
            fragment.arguments = bundle*/
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSearchResultBinding= FragmentSearchResultBinding.inflate(layoutInflater)
       // homeViewModel=ViewModelProvider(this,homeFactory).get(HomeViewModel::class.java)

        initView()
        initObserver()
        initClickListener()
        return fragmentSearchResultBinding.root
    }

    private fun initObserver() {
//        homeViewModel.getRecordsObserver().observe(viewLifecycleOwner,object : Observer<List<SearchModel>> {
//            override fun onChanged(t: List<SearchModel>?) {
//                if (t?.size!! >0) {
//                    fragmentSearchResultBinding.tvRecentSearch.isVisible=true
//                    fragmentSearchResultBinding.recyclerviewRecent.isVisible=true
//                    setrecentAdapter(t!!)
//                }
//            }
//        })
    }

    private fun setrecentAdapter(t: List<SearchModel>) {
        searchResultAdapter= SearchResultAdapter(requireContext(),t)
        gridLayoutManager= GridLayoutManager(requireContext(),3)
        fragmentSearchResultBinding.recyclerviewRecent.layoutManager=gridLayoutManager
        fragmentSearchResultBinding.recyclerviewRecent.adapter=searchResultAdapter
    }

    private fun initView() {

    }

    private fun initClickListener() {
        (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()){
                    val searchModel=SearchModel(searchName = p0.toString())
                  //  homeViewModel.insertRecord(searchModel)
                    (requireActivity() as HomeActivity).activityHomeActivity.searchLayout.search.setText(p0.toString())
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }

}