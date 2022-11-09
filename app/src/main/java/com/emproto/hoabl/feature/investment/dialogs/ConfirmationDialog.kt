package com.emproto.hoabl.feature.investment.dialogs

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginEnd
import androidx.fragment.app.DialogFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ApplyConfirmationDialogBinding
import com.emproto.hoabl.utils.ItemClickListener
import com.emproto.hoabl.viewmodels.InvestmentViewModel

class ConfirmationDialog(
    private val investmentViewModel: InvestmentViewModel,
    private val itemClickListener: ItemClickListener,
) : DialogFragment(), View.OnClickListener {

    lateinit var binding: ApplyConfirmationDialogBinding
    private var constraint= ConstraintSet()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ApplyConfirmationDialogBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.all_corner_radius_white_bg)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        investmentViewModel.getSku().observe(viewLifecycleOwner) {
            it.let { data ->
                binding.apply {

                    if (data.name?.length!! >10){
                        tvItemLandSkusName.setEms(5)
                        tvItemLandSkusName.ellipsize= TextUtils.TruncateAt.END

                    }else{
                    }
                    tvItemLandSkusName.text = data.name

                    val amount = it.priceRange?.from!!.toDouble() / 100000
                    val convertedFromAmount = String.format("%.0f", amount)
                    val amountTo = it.priceRange?.to!!.toDouble() / 100000
                    val convertedToAmount = String.format("%.0f", amountTo)
                    val itemLandSkus = "${data.areaRange?.from} - ${data.areaRange?.to} Sqft"
                    tvItemLandSkusArea.text = itemLandSkus
                    val itemLandSkusPrice = "₹${convertedFromAmount}L - ${convertedToAmount}L"
                    tvItemLandSkusPrice.text = itemLandSkusPrice
                    tvItemLandSkusDescription.text = data.shortDescription
                }
                binding.tvYesText.setOnClickListener { view ->
                    dialog?.dismiss()
                    data.id?.let { it1 -> itemClickListener.onItemClicked(
                        view,
                        it1,
                        "Yes") }
                }
                binding.tvNoText.setOnClickListener(this)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_no_text -> {
                dialog?.dismiss()
            }
        }
    }

}