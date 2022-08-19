package com.emproto.networklayer.preferences

interface AppPreference {
    fun saveLogin(loggedIn: Boolean)
    fun isUserLoggedIn(): Boolean
    fun setToken(token: String)
    fun getToken(): String
    fun setNotificationToken(token: String)
    fun getNotificationToken(): String
    fun getMobilenum(): String
    fun setMobilenum(number: String)
    fun savePinDialogStatus(status: Boolean)
    fun isPinDialogShown(): Boolean
    fun activatePin(status: Boolean)
    fun getPinActivationStatus(): Boolean
    fun setFacilityCard(status: Boolean)
    fun isFacilityCard(): Boolean
    fun setCustomerType(customerType: String)
    fun setPromisesCount(count: Int)
    fun saveUserType(type:Int)
    fun getUserType(): Int
    fun setFmUrl(url:String)
    fun getFmUrl():String

    //for dontmisout card
    fun saveOfferId(project: Int)
    fun getOfferId(): Int
    fun saveOfferUrl(url: String)
    fun getOfferUrl(): String
    fun getCustomerType(): String

    //get promises count
    fun getPromisesCount(): Int

}