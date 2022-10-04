package com.emproto.hoabl.feature.investment.adapters

import android.os.Build
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.emproto.core.Constants
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.investment.views.FaqDetailFragment
import com.emproto.hoabl.model.RecyclerViewFaqItem
import com.emproto.hoabl.utils.Extensions.hideKeyboard
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.CgData
import com.google.android.material.textview.MaterialTextView

class FaqDetailAdapter(
    private val fragment: FaqDetailFragment,
    private val list: List<RecyclerViewFaqItem>,
    private val faqData: List<CgData>,
    private val faqId: Int = 0,
    private val itemClickListener: ItemClickListener,
    private val searchText: String = "",
    private val projectName: String,
    private val fromInvestment: Boolean,
    private val handler: Handler,
    private var runnable: Runnable? = null


) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    private lateinit var categoryAdapter: PopularCategoryAdapter
    private lateinit var faqAdapter: FaqAdapter


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_ONE -> {
                TopViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.faq_investment_fragment, parent, false)
                )
            }
            else -> {
                CategoryViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.faq_category_layout, parent, false)
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].viewType) {
            VIEW_TYPE_ONE -> {
                (holder as TopViewHolder).bind(position)
            }
            VIEW_TYPE_TWO -> {
                (holder as CategoryViewHolder).bind(list[position].data)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class TopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvPopCategory: RecyclerView = itemView.findViewById(R.id.rv_pop_category)
        val search: EditText = itemView.findViewById(R.id.et_search)
        val close: ImageView = itemView.findViewById(R.id.iv_close_image)
        val title: MaterialTextView = itemView.findViewById(R.id.tv_faq_title)
        val imageArrow: ImageView = itemView.findViewById(R.id.imgArrow)

        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(position: Int) {
            //Binding data
            val project = "$projectName - FAQs"
            when (projectName) {
                "" -> {
                    title.text = Constants.FAQS
                    title.setTextColor(title.context.getColor(R.color.app_color))
                }

                else -> title.text = project
            }

            when (fromInvestment) {
                true -> {
                    imageArrow.visibility = View.GONE
                }
                false -> {
                    imageArrow.visibility = View.VISIBLE
                }
            }

            val list = arrayListOf<String>()
            for (item in faqData) {
                if (item.numberOfFaqs != null) {
                    list.add(item.name)
                }
            }
            categoryAdapter = PopularCategoryAdapter(list, itemClickListener)
            rvPopCategory.adapter = categoryAdapter

            //setup search bar when coming to this page
            search.setText(searchText)
            if (searchText.isNotEmpty()) {
                close.visibility = View.VISIBLE
            }
            if (searchText != "") {
                search.requestFocus()
            }

            // textWatcher for search
            search.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().length > 1 && s.toString() != "") {
                        close.visibility = View.VISIBLE
                    }
                    if (s.toString().isEmpty() || s.toString() == "") {
                        runnable = Runnable {
                            itemClickListener.onItemClicked(
                                search,
                                position,
                                s.toString()
                            )
                        }
                        runnable.let { it1 -> it1?.let { handler.postDelayed(it, 500) } }
//                        Handler().postDelayed({
//                            itemClickListener.onItemClicked(search, position, s.toString())
//                        }, 500)
                    }
                }
            })

            //BackButton handled
            imageArrow.setOnClickListener {
                itemClickListener.onItemClicked(imageArrow, position, "")
            }
            title.setOnClickListener {
                itemClickListener.onItemClicked(imageArrow, position, "")
            }

            //Tick button handled from keyboard
            search.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    runnable = Runnable {
                        itemClickListener.onItemClicked(
                            search,
                            position,
                            search.text.toString()
                        )
                    }
                    runnable.let { it1 -> it1?.let { handler.postDelayed(it, 500) } }
                    fragment.hideKeyboard()
                    true
                }
                false
            }

            //Close button in search bar
            close.setOnClickListener {
                search.setText("")
                close.visibility = View.GONE
            }
        }
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategoryTitle: MaterialTextView =
            itemView.findViewById(R.id.tv_category_title)
        private val rvFaq: RecyclerView = itemView.findViewById(R.id.rv_faq)
        fun bind(data: CgData) {
            if (data.numberOfFaqs != null) {
                tvCategoryTitle.visibility = View.VISIBLE
                tvCategoryTitle.text = data.name
                val sortedList = data.faqs.sortedBy { it.priority }
                faqAdapter = FaqAdapter(sortedList, faqId, itemClickListener)
                rvFaq.adapter = faqAdapter
            } else {
                tvCategoryTitle.visibility = View.GONE
            }

        }
    }

}