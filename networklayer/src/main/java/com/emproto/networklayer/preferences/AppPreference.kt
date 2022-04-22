package com.emproto.networklayer.preferences

interface AppPreference {
    fun saveLogin(loggedIn: Boolean)
    fun isUserLoggedIn():Boolean
    fun setToken(token: String)
    fun getToken(): String
}