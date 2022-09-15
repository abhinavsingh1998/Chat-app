package com.emproto.networklayer.preferences

import android.content.Context
import javax.inject.Inject

class AppPreferenceImp @Inject constructor(context: Context) : AppPreference {

    companion object {
        const val LOGGED_IN = "login"
        const val TOKEN = "token"
        const val Number = "number"
        const val PIN_VERIFICATION = "pin"
        const val PIN_AUTH = "pin_auth"
        const val FACILITY_CARD = "facility_card"
        const val NOTIFICATION_TOKEN = "notification_token"
        const val OFFER_ID = "offer_id"
        const val OFFER_CARD_URL = "offer_url"
        const val CUSTOMER_TYPE = "customer_type"
        const val PROMISES_COUNT = "promises_count"
        const val USERTYPE= "user_type"
        const val FM_URL="fm_url"
    }

    private var preference = context.getSharedPreferences("hoabl-pref", Context.MODE_PRIVATE)
    private var editor = preference.edit()
    override fun saveLogin(loggedIn: Boolean) {
        saveBoolean(LOGGED_IN, loggedIn)
    }

    override fun isUserLoggedIn(): Boolean {
        return getBoolean(LOGGED_IN, false)
    }

    override fun setToken(token: String) {
        saveString(TOKEN, token)
    }

    override fun getToken(): String {
        return getString(TOKEN, "")
    }

    private fun saveString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String = ""): String {
        return preference.getString(key, defaultValue) ?: defaultValue
    }

    private fun saveInt(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    private fun getInt(key: String, defaultValue: Int = 0): Int {
        return preference.getInt(key, defaultValue) ?: defaultValue
    }

    private fun saveBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preference.getBoolean(key, defaultValue)
    }

    override fun setNotificationToken(token: String) {
        saveString(NOTIFICATION_TOKEN, token)
    }

    override fun getNotificationToken(): String {
        return getString(NOTIFICATION_TOKEN, "")
    }

    override fun getMobilenum(): String {
        return getString(Number, "")
    }

    override fun setMobilenum(number: String) {
        saveString(Number, number)
    }

    override fun savePinDialogStatus(status: Boolean) {
        saveBoolean(PIN_VERIFICATION, status)
    }

    override fun isPinDialogShown(): Boolean {
        return getBoolean(PIN_VERIFICATION, false)
    }

    override fun activatePin(status: Boolean) {
        saveBoolean(PIN_AUTH, status)
    }

    override fun getPinActivationStatus(): Boolean {
        return getBoolean(PIN_AUTH, false)
    }

    override fun setFacilityCard(status: Boolean) {
        saveBoolean(FACILITY_CARD, status)
    }

    override fun isFacilityCard(): Boolean {
        return getBoolean(FACILITY_CARD, false)
    }

    override fun setCustomerType(customerType: String) {
        saveString(CUSTOMER_TYPE, "")
    }

    override fun setPromisesCount(count: Int) {
        saveInt(PROMISES_COUNT, count)
    }

    override fun saveUserType(type: Int) {
        saveInt(USERTYPE, type)
    }

    override fun getUserType(): Int {
        return getInt(USERTYPE,0 )
    }

    override fun setFmUrl(url: String) {
        saveString(FM_URL, url)
    }

    override fun getFmUrl(): String {
        return getString(FM_URL,"")
    }


    override fun saveOfferId(project: Int) {
        saveInt(OFFER_ID, project)
    }

    override fun getOfferId(): Int {
        return getInt(OFFER_ID, 0)
    }

    override fun saveOfferUrl(url: String) {
        saveString(OFFER_CARD_URL, url)
    }

    override fun getOfferUrl(): String {
        return getString(OFFER_CARD_URL, "")
    }

    override fun getCustomerType(): String {
        return getString(CUSTOMER_TYPE, "")
    }

    override fun getPromisesCount(): Int {
        return getInt(PROMISES_COUNT)
    }



}