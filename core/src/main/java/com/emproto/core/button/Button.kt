package com.emproto.core.button

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.emproto.core.R

class Button : ConstraintLayout {

    var contex = context!!.applicationContext

    private lateinit var view: View

    /**
     * components came from xml design
     */
    private var buttonLayout: ConstraintLayout? = null
    private var textViewButton: TextView? = null

    /**
     * actual button text value
     */
    private var text: String? = null
    private var buttonType = 0
    private var typefaceButton: Typeface? = null
    private var fontStyle = 0
    private lateinit var textColor: ColorStateList


    /**
     * custom listeners
     */
    private var onButtonClickListener: OnButtonClickListener? = null

    constructor(context: Context) : super(context) {
        this.contex = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.contex = context
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.contex = context
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.contex = context
        init(attrs)
    }

    /**
     * This method has been called from dynamically added component at runtime
     * All other helper methods has been exposed to call it at runtime
     */
    private fun init() {
        view = inflate(context, R.layout.custom_button, this)
        buttonLayout = view.findViewById(R.id.buttonLayout)
        textViewButton = view.findViewById(R.id.textViewButtonText)
        refreshView()
    }

    /**
     * @param attributeSet -> this param has been sent through the overridden constructor
     * this pram contains all the attributes while creating through xml
     */
    private fun init(attributeSet: AttributeSet?) {
        val attributes =
            context?.theme?.obtainStyledAttributes(attributeSet, R.styleable.Button, 0, 0)
        view = inflate(context, R.layout.custom_button, this)
        buttonLayout = view.findViewById(R.id.buttonLayout)
        textViewButton = view.findViewById(R.id.textViewButtonText)
        text = attributes?.getString(R.styleable.Button_text)
        buttonType = attributes!!.getInteger(R.styleable.Button_buttonType, 1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            typefaceButton = attributes.getFont(R.styleable.Button_fontFamily)
        }
        fontStyle = attributes.getInteger(R.styleable.Button_android_textStyle, Typeface.NORMAL)
        textColor = attributes.getColorStateList(R.styleable.Button_buttonTextColor)!!
        refreshView()
    }

    /**
     * will be called everytime the view value got changed
     * FOR -> refreshing the view continuously
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun refreshView() {
        buttonLayout!!.background =

            if (buttonType == 0) context?.resources?.getDrawable(R.drawable.drawable_button_negative)
            else context?.resources?.getDrawable(R.drawable.drawable_button_positive)

        textViewButton!!.setTextColor(textColor)
        textViewButton!!.text = text
        //textViewButton!!.setTypeface(typefaceButton, fontStyle)
        buttonLayout!!.setOnClickListener { view: View? ->
            if (onButtonClickListener != null) {
                onButtonClickListener!!.OnButtonClicked(this@Button)
            }
        }
    }

    fun setText(text: String) {
        this.text = text
        refreshView()
    }

    fun setTypefaceButton(typefaceButton: Typeface?) {
        this.typefaceButton = typefaceButton
        refreshView()
    }

    fun setFontStyle(fontStyle: Int) {
        this.fontStyle = fontStyle
        refreshView()
    }

    fun setOnButtonClickListener(onButtonClickListener: OnButtonClickListener?) {
        this.onButtonClickListener = onButtonClickListener
    }

    fun setButtonType(buttonType: ButtonType) {
        this.buttonType = buttonType.value
        refreshView()
    }

    fun getTypefaceButton(): Typeface? {
        return typefaceButton
    }

    fun getFontStyle(): Int {
        return fontStyle
    }

    fun getButtonType(): Int {
        return buttonType
    }

    fun getOnButtonClickListener(): OnButtonClickListener? {
        return onButtonClickListener
    }
}