package com.emproto.hoabl.feature.investment.adapters

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.emproto.hoabl.R
import com.emproto.hoabl.feature.home.views.HomeActivity
import com.emproto.hoabl.model.RecyclerViewFaqItem
import com.emproto.hoabl.utils.Extensions.hideKeyboard
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.networklayer.response.investment.CgData
import com.google.android.material.textview.MaterialTextView

class FaqDetailAdapter(
    private val activity: Activity,
    private val context: Context,
    private val list: List<RecyclerViewFaqItem>,
    private val faqData: List<CgData>,
    private val faqId: Int = 0,
    private val itemClickListener: ItemClickListener,
    private val searchText: String = "",
    private val projectName: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    private lateinit var categoryAdapter: PopularCategoryAdapter
    private lateinit var faqAdapter: FaqAdapter
    private var isItemsPresent = false

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].viewType) {
            VIEW_TYPE_ONE -> {
                (holder as TopViewHolder).bind(position)
            }
            VIEW_TYPE_TWO -> {
                (holder as CategoryViewHolder).bind(position, list[position].data)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class TopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvPopCategory = itemView.findViewById<RecyclerView>(R.id.rv_pop_category)
        val search = itemView.findViewById<EditText>(R.id.et_search)
        val close = itemView.findViewById<ImageView>(R.id.iv_close_image)
        val title = itemView.findViewById<MaterialTextView>(R.id.tv_faq_title)
        fun bind(position: Int) {
            //Binding data
            when(projectName){
                "" -> title.text ="FAQs"
                else -> title.text ="${projectName.toString()} - FAQs"
            }

            val list = arrayListOf<String>()
            for (item in faqData) { list.add(item.name) }
            categoryAdapter = PopularCategoryAdapter(context, list, itemClickListener)
            rvPopCategory.adapter = categoryAdapter

            //setup search bar when coming to this page
            search.setText(searchText)
            if (searchText.isNotEmpty()) { close.visibility = View.VISIBLE }
            if (searchText != "") { search.requestFocus() }

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
                    (activity as HomeActivity).activityHomeActivity.loader.show()
                    Handler().postDelayed({
                        (activity as HomeActivity).activityHomeActivity.loader.hide()
                        itemClickListener.onItemClicked(search, position, s.toString())
                    }, 2000)
                }
            })

            //Tick button handled from keyboard
            search.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    activity.hideKeyboard()
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
        val tvCategoryTitle = itemView.findViewById<MaterialTextView>(R.id.tv_category_title)
        val rvFaq = itemView.findViewById<RecyclerView>(R.id.rv_faq)
        fun bind(position: Int, data: CgData) {
            tvCategoryTitle.text = data.name
            val sortedList = data.faqs.sortedBy { it.priority }
            faqAdapter = FaqAdapter(sortedList, context, faqId,itemClickListener)
            rvFaq.adapter = faqAdapter
        }
    }

}