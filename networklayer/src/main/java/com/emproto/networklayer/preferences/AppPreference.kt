package com.emproto.networklayer.preferences

interface AppPreference {
    fun saveLogin(loggedIn: Boolean)
    fun isUserLoggedIn():Boolean
    fun setToken(token: String)
    fun getToken(): String
    fun setNotificationToken(token: String)
    fun getNotificationToken(): String
    fun getMobilenum(): String
    fun setMobilenum(number: String)
    fun savePinDialogStatus(status:Boolean)
    fun isPinDialogShown():Boolean
    fun activatePin(status: Boolean)
    fun getPinActivationStatus():Boolean
    fun setFacilityCard(status: Boolean)
    fun isFacilityCard():Boolean
}