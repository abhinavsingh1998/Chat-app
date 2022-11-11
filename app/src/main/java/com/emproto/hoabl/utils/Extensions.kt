package com.emproto.hoabl.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.emproto.networklayer.response.home.HomePagesOrPromise
import com.emproto.networklayer.response.home.PageManagementOrInsight
import com.emproto.networklayer.response.home.PageManagementOrLatestUpdate
import com.emproto.networklayer.response.insights.InsightsCreatedAdmin
import com.emproto.networklayer.response.insights.InsightsModifiedAdmin
import com.emproto.networklayer.response.investment.PmData
import com.emproto.networklayer.response.marketingUpdates.Data
import com.emproto.networklayer.response.marketingUpdates.MarketingUpdateCreatedBy
import com.emproto.networklayer.response.marketingUpdates.MarketingUpdateUpdatedBy

object Extensions {

    fun saveToPreferences(activity: Activity, key: String, value: String) {
        val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getFromPreferences(activity: Activity, key: String, value: String): String {
        val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(key, "").toString()
    }

    fun PmData.toHomePagesOrPromise() =
        com.emproto.networklayer.response.promises.HomePagesOrPromise(
            createdAt = createdAt.toString(),
            createdBy = createdBy.toString(),
            crmPromiseId = crmPromiseId.toString(),
            description = description!!,
            displayMedia = displayMedia!!,
            howToApply = howToApply!!,
            id = id!!,
            isHowToApplyActive = isHowToApplyActive!!,
            isTermsAndConditionsActive = isTermsAndConditionsActive!!,
            name = name!!,
            priority = priority.toString(),
            promiseIconType = promiseIconType.toString(),
            promiseMedia = promiseMedia,
            promiseType = promiseType.toString(),
            shortDescription = shortDescription.toString(),
            status = status.toString(),
            termsAndConditions = termsAndConditions!!,
            updatedAt = updatedAt.toString(),
            updatedBy = updatedBy.toString()
        )

    fun HomePagesOrPromise.toHomePagesOrPromise() =
        com.emproto.networklayer.response.promises.HomePagesOrPromise(
            createdAt = createdAt.toString(),
            createdBy = createdBy.toString(),
            crmPromiseId = crmPromiseId.toString(),
            description = description,
            displayMedia = displayMedia,
            howToApply = howToApply,
            id = id,
            isHowToApplyActive = isHowToApplyActive,
            isTermsAndConditionsActive = isTermsAndConditionsActive,
            name = name,
            priority = priority.toString(),
            promiseIconType = promiseIconType.toString(),
            promiseMedia = promiseMedia,
            promiseType = promiseType.toString(),
            shortDescription = shortDescription.toString(),
            status = status.toString(),
            termsAndConditions = termsAndConditions,
            updatedAt = updatedAt.toString(),
            updatedBy = updatedBy.toString()
        )

    fun PageManagementOrLatestUpdate.toData() = Data(
        createdAt = createdAt,
        createdBy = createdBy,
        detailedInfo = detailedInfo,
        displayTitle = displayTitle,
        formType = formType,
        id = id,
        marketingUpdateCreatedBy = MarketingUpdateCreatedBy(firstName = "", id = 0),
        marketingUpdateUpdatedBy = MarketingUpdateUpdatedBy(firstName = "", id = 0),
        priority = 0,
        shouldDisplayDate = false,
        status = status,
        subTitle = subTitle,
        updateType = updateType,
        updatedAt = updatedAt,
        updatedBy = updatedBy
    )

    fun PageManagementOrInsight.toData() = com.emproto.networklayer.response.insights.Data(

        createdAt = createdAt,
        createdBy = createdBy,
        displayTitle = displayTitle,
        id = id,
        insightsCreatedAdmin = InsightsCreatedAdmin(
            "", "", "",
            "", 0, "", "", "", "", 0,
            false, "", "", "", 0,
            "", "", 0
        ),
        insightsMedia = insightsMedia,
        insightsModifiedAdmin = InsightsModifiedAdmin(
            "", "", "",
            "", 0, "", "", "", "", 0,
            false, "", "", "", 0,
            "", "", 0
        ),
        modifiedBy = modifiedBy,
        priority = priority,
        status = status,
        updatedAt = updatedAt

    )

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Context.showHTMLText(message: String?): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(message)
        }
    }

     fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        var startIndexOfLink = -1
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun updateDrawState(textPaint: TextPaint) {
                    // use this to change the link color
                    textPaint.color = Color.BLACK
                    // toggle below value to enable/disable
                    // the underline shown below the clickable text
                    textPaint.isUnderlineText = true
                }

                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }
            }
            startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
//      if(startIndexOfLink == -1) continue // todo if you want to verify your texts contains links text
            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

}