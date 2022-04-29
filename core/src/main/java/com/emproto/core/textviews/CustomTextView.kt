package com.emproto.core.textviews

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.emproto.core.R


class CustomTextView : androidx.appcompat.widget.AppCompatTextView {
    var contex = context!!.applicationContext


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.contex = context
        style(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.contex = context
        style(context, attrs)
    }

    private fun style(context: Context, attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)
        val cf = typeArray.getString(R.styleable.CustomTextView_fontName)
        var fontName = 0
        fontName = when (cf) {
            "jost_bold" -> R.font.jost_bold
            "jost_medium" -> R.font.jost_medium
            "jost_regular" -> R.font.jost_regular
            "jost_light" -> R.font.jost_light
            "sheldon" -> R.font.sheldon
            "sheldon_italic" -> R.font.sheldon_italic
            "montserrat_regular" -> R.font.montserrat_regular
            "dinpro" ->R.font.dinpro
            "dinpro_light" -> R.font.dinpro_light
            "dinpro_medium" -> R.font.dinpro_medium
            "shelldon" -> R.font.shelldon
            "helvetica_bold" -> R.font.helvetica_bold
            "montserrat_bold" -> R.font.montserrat_bold
            "montserrat_medium" -> R.font.montserrat_medium
            "montserrat_regular" -> R.font.montserrat_regular
            "sign_ainter_house_cript" -> R.font.sign_ainter_house_cript
            "shelldon" -> R.font.shelldon
            "jost_italic" -> R.font.jost_italic
            else -> R.font.jost_regular
        }

        val typeFace = ResourcesCompat.getFont(context, fontName)
        typeface = typeFace
        typeArray.recycle()
    }

}