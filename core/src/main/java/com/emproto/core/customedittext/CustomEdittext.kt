package com.emproto.core.customedittext

import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.emproto.core.R

class CustomEdittext : ConstraintLayout {

    private lateinit var view: View
    private lateinit var dropdownView: RelativeLayout
    private lateinit var countryCodeText: TextView
    private lateinit var editText: EditText
    private lateinit var hintTextView: TextView


    private var hintText = ""
    private var placeholderText = ""
    private var value = ""

    var onValueChangeListner: OnValueChangedListener? = null
    private lateinit var textWatcher: TextWatcher


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }


    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs)
    }

    fun init() {
        view = inflate(context, R.layout.custom_edittext, this)
        dropdownView = view.findViewById(R.id.dropdown_view)
        countryCodeText = view.findViewById(R.id.country_code)
        editText = view.findViewById(R.id.et_phonenumber)
        hintTextView = view.findViewById(R.id.hint_text)

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (onValueChangeListner != null) {
                    onValueChangeListner!!.onValueChanged(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
        editText.addTextChangedListener(textWatcher)
    }

    private fun init(attrs: AttributeSet?) {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CustomEdittext, 0, 0)
        hintText = attributes.getString(R.styleable.CustomEdittext_hintText)!!
        placeholderText = attributes.getString(R.styleable.CustomEdittext_placeholdertext)!!

        view = inflate(context, R.layout.custom_edittext, this)
        dropdownView = view.findViewById(R.id.dropdown_view)
        countryCodeText = view.findViewById(R.id.country_code)
        editText = view.findViewById(R.id.et_phonenumber)
        hintTextView = view.findViewById(R.id.hint_text)

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (onValueChangeListner != null) {
                    onValueChangeListner!!.onValueChanged(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
        editText.addTextChangedListener(textWatcher)
        editText.onFocusChangeListener = OnFocusChangeListener { p0, hasFocus ->
            if (hasFocus) {
                Handler().postDelayed(Runnable { // Show white background behind floating label
                    hintTextView.visibility = View.VISIBLE
                    editText.hint = placeholderText
                }, 100)
            } else {
                // Required to show/hide white background behind floating label during focus change
                if (editText.text.toString()
                        .isNotEmpty()
                )
                    hintTextView.visibility = View.VISIBLE
                else {
                    hintTextView.visibility = View.INVISIBLE
                    editText.hint =
                        hintText
                }

            }
        }

        refreshView()
    }

    private fun refreshView() {
        editText.setHint(hintText)
        hintTextView.setText(hintText)
    }

    public fun onValueChangeListner(onValueChangedListener: OnValueChangedListener) {
        this.onValueChangeListner = onValueChangeListner
    }


}