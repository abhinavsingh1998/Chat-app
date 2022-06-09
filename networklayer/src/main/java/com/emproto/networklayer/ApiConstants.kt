package com.emproto.networklayer

import com.emproto.networklayer.response.home.InsightsMedia
import com.emproto.networklayer.response.home.LatestUpdates

object ApiConstants {
    const val COMMON_MSG = "Something Went Wrong"
    val BASE_URL_DEV =
        "http://hoabl-backend-dev-306342355.ap-south-1.elb.amazonaws.com/"

    const val GENERATE_OTP = "hoabl-customer/generate_otp"
    const val VALIDATE_OTP = "hoabl-customer/verify_otp"
    const val SET_NAME = "hoabl-customer/set_name"
    const val TROUBLE_SIGNING = "hoabl-customer/case"

    const val HOME = "hoabl-admin/get-page"
    const val LatestUpdates="hoabl-admin/marketing-updates"
    const val INSIGHTS="hoabl-admin/insights"
    const val TESTIMONIALS= "hoabl-admin/testimonials"
    const val PROMISES = "hoabl-admin/get-page"
    const val INVESTMENT = "hoabl-admin/get-page"
    const val INVESTMENT_PROJECT_DETAIL = "hoabl-admin/project-contents/{id}"
    const val INVESTMENT_ALL_PROJECT = "hoabl-admin/project-contents"
    const val INVESTMENT_PROMISES = "hoabl-admin/promises"
    const val PORTFOLIO_DASHBOARD = "hoabl-customer/investment-summary"
    const val EDITPROFILE = "hoabl-customer/profile"
    const val UPLOADPROFILEPICTURE = "hoabel-customer/profile/picture"
    const val COUNTRY = "hoabel-customer/profile/countries"
    const val INVESTMENT_DETAILS = "hoabl-customer/investment-details"
    const val DOC_FILTER = "hoabl-customer/doc-filter"
    const val TERMS_CONDITION = "hoabl-admin/get-page"
    const val INVESTMENT_PROJECT_FAQ = "hoabl-admin/faqs/project-contents/{projectContentId}"
    const val WATCHLIST = "hoabl-customer/watchlist"
    const val GET_PROFILE = "hoabl-customer/profile/get"
    const val PROJECT_TIMELINE = "hoabl-admin/project-contents/{id}"
    const val FACILITY_MANAGMENT = "hoabl-customer/facility/authenticate"
    const val REFER_NOW = "hoabl-customer/addReferral"
    const val STATES="hoabl-customer/profile/states/{countryIsoCode}"
    const val CITIES="hoabl-customer/profile/cities"
    const val DELETE_WATCHLIST = "hoabl-customer/watchlist/{id}"
    const val PROJECT_INVENTORIES = "hoabl-customer/inventories/{id}"
    const val ADD_INVENTORY = "hoabl-customer/inventories"
    const val VIDEO_CALL = "hoabl-customer/case"
    const val DOCUMENT_DOWNLOAD = "hoabl-customer/doc-download"
    const val BOOKING_JOURNEY = "hoabl-customer/booking-journey"
    const val CHATS_LIST = "hoabl-customer/chats"
    const val CHATS_INITIATE = "hoabl-customer/chat/initiate"
    const val PROJECT_MEDIA_GALLERIES = "hoabl-admin/project-contents/{projectContentId}/media-galleries"
}
