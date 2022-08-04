package com.emproto.hoabl.feature.investment.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.emproto.hoabl.R
import com.emproto.hoabl.databinding.ApplicationSubmittedDialogBinding

class ApplicationSubmitDialog(
    val title: String,
    val description: String,
    private val showToast: Boolean = true
) : DialogFragment(), View.OnClickListener {

    lateinit var binding: ApplicationSubmittedDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ApplicationSubmittedDialogBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.all_corner_radius_white_bg)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpUI()
    }

    private fun setUpUI() {
        binding.tvApplicationSubmit.text = title
        binding.tvApplicationThanks.text = description
    }

    private fun setUpClickListeners() {
        binding.tvSubmitOkay.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_submit_okay -> {
                when (showToast) {
                    true -> Toast.makeText(
                        this.requireContext(),
                        "Application submitted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> {}
                }
                dialog?.dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        // val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        val layoutParams: WindowManager.LayoutParams =
            dialog!!.window!!.attributes
        layoutParams.dimAmount = 0.9f
        dialog!!.window?.attributes = layoutParams
    }
}