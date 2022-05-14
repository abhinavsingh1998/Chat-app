package com.emproto.hoabl.feature.home.views.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.emproto.core.BaseFragment
import com.emproto.core.Database.TableModel.SearchModel

import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.databinding.FragmentSearchResultBinding
import com.emproto.hoabl.feature.home.adapters.SearchResultAdapter
import com.emproto.hoabl.feature.investment.adapters.CategoryListAdapter
import com.emproto.hoabl.feature.portfolio.adapters.DocumentsAdapter
import com.emproto.hoabl.feature.portfolio.adapters.ProjectFaqAdapter

class SearchResultFragment : BaseFragment() {

    lateinit var fragmentSearchResultBinding: FragmentSearchResultBinding

    /*lateinit var homeViewModel:HomeViewModel
    @Inject
    lateinit var homeFactory: HomeFactory*/

    lateinit var searchResultAdapter: SearchResultAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    lateinit var faqAdapter: ProjectFaqAdapter
    lateinit var projectListAdapter: CategoryListAdapter
    lateinit var documentAdapter: DocumentsAdapter

    companion object {
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
        savedInstanceState: Bundle?,
    ): View? {
        fragmentSearchResultBinding = FragmentSearchResultBinding.inflate(layoutInflater)

        initView()
        initObserver()
        return fragmentSearchResultBinding.root
    }

    private fun initObserver() {

    }

    private fun setrecentAdapter(t: List<SearchModel>) {

    }

    private fun initView() {
        (requireActivity() as HomeActivity).hideBottomNavigation()

        fragmentSearchResultBinding.projectList.layoutManager =
            LinearLayoutManager(requireContext())
        projectListAdapter = CategoryListAdapter(requireContext(), emptyList(), null)
        fragmentSearchResultBinding.projectList.adapter = projectListAdapter

        fragmentSearchResultBinding.documentsList.layoutManager =
            LinearLayoutManager(requireContext())
        documentAdapter = DocumentsAdapter(emptyList())
        fragmentSearchResultBinding.documentsList.adapter = documentAdapter

        fragmentSearchResultBinding.faqsList.layoutManager = LinearLayoutManager(requireContext())
        faqAdapter = ProjectFaqAdapter(emptyList())
        fragmentSearchResultBinding.faqsList.adapter = faqAdapter

    }


}