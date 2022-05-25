package com.emproto.hoabl.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.emproto.networklayer.response.investment.PmData
import com.emproto.networklayer.response.promises.*
import com.emproto.networklayer.response.home.HomePagesOrPromise

object Extensions{

    fun saveToPreferences(activity:Activity,key:String,value:String){
        val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getFromPreferences(activity:Activity,key:String,value:String):String{
        val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(key,"").toString()
    }

    fun PmData.toHomePagesOrPromise() = HomePagesOrPromise(
        createdAt = createdAt.toString(),
        createdBy = createdBy.toString(),
        crmPromiseId = crmPromiseId.toString(),
        description = description.toString(),
        displayMedia= displayMedia,
        howToApply= howToApply,
        id= id,
        isHowToApplyActive= isHowToApplyActive,
        isTermsAndConditionsActive= isTermsAndConditionsActive,
        name= name,
        priority= priority.toString(),
        promiseIconType= promiseIconType,
        promiseMedia= promiseMedia,
        promiseType= promiseType.toString(),
        shortDescription= shortDescription.toString(),
        status= status,
        termsAndConditions= termsAndConditions,
        updatedAt= updatedAt,
        updatedBy= updatedBy.toString()
    )

    fun HomePagesOrPromise.toHomePagesOrPromise() = com.emproto.networklayer.response.promises.HomePagesOrPromise(
        createdAt = createdAt.toString(),
        createdBy = createdBy.toString(),
        crmPromiseId = crmPromiseId.toString(),
        description = description.toString(),
        displayMedia = displayMedia,
        howToApply = howToApply,
        id = id,
        isHowToApplyActive = isHowToApplyActive,
        isTermsAndConditionsActive = isTermsAndConditionsActive,
        name = name,
        priority = priority.toString(),
        promiseIconType = promiseIconType,
        promiseMedia = promiseMedia,
        promiseType = promiseType.toString(),
        shortDescription = shortDescription.toString(),
        status = status,
        termsAndConditions = termsAndConditions,
        updatedAt = updatedAt,
        updatedBy = updatedBy.toString()
    )
}