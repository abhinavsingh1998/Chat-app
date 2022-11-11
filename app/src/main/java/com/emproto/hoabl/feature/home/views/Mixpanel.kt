package com.emproto.hoabl.feature.home.views

import android.content.Context
import com.emproto.hoabl.BuildConfig
import com.emproto.hoabl.R
import com.mixpanel.android.mpmetrics.MixpanelAPI
import javax.inject.Inject

class Mixpanel @Inject constructor(val context: Context) {
    companion object {
        //mixpanel events
        val DONTMISSOUT = "HoABL - Don't Miss Out Card"
        val HOME = "Global-Home"
        val SHARE = "Global - Share"
        val REFERNOW = "Global - Refer Now"
        val SEARCH = "Global - Search"
        val LOGINTERMSANDCONDITIONS = "Login - Terms and Conditions & Privacy Policy"
        val WHATSAPPCOMMUNICATION = "Login - WhatsApp Communication"
        val MASTHEAD = "Global - Master Head"
        val PRIVACYPOLICY = "Privacy Policy"
        val PROJECTCARD = "HoABL - Project Card"
        val APPLYNOW = "HoABL - Project Card - Apply Now"
        val VIEWALLPROPERTIES = "HoABL - Discover all our Investments"
        val LATESTUPDATECARD = "HoABL - Latest Update Card"
        val SEEALLUPDATES = "HoABL - See All Latest Updates"
        val SEEALLPROMISES = "HoABL - See All Promises"
        val PROMISESCARD = "HoABL - Promises Card"
        val INSIGHTSCARD = "Insights HoABL - Insights Card"
        val SEEALLINSIGHTS = "HoABL - See All Insights"
        val TESTIMONIALSEEALL = "HoABL - See All Testominals"
        val REFERYOURFRIEND = "Global - Refer Your friend"
        val PORTFOLIO = "Promises - Portfolio"
        val PROMISESTERMSANDCONDITIONS = "Promises - Terms and Conditions"
        val PROMISECARD = "Promises - Promises Card"
        val INVESTMENT = "Investment"
        val NEWLAUNCHCARD = "Investment Home - New Launch Card"
        val INVESTMENTAPPLYNOW = "Investment Home - New Launch Card - Apply Now"
        val INVESTMENTDONTMISSOUT1 = "Investment Home - Don’t Miss Out"

        //        val COLLECTION1CARD="Collection 1 Card"
//        Investment Project - See All Promises
//        val INVESTMENTNEWLAUNCHES="Investment New Launches Apply Now"
        val MEDIAGALLERYSECTIONSELECTION = "Media Gallery - Section Selection"
        val WISHLIST = "Investment Project - Wishlist"
        val INVESTMENTSHARE = "Investment Project - Share"
        val SEEALLIMAGESVIDEOS = "Investment Project - See All Images/Video"
        val INVESTMENTNEWLAUNCHAPPLYNOW = "Investment Project - Apply Now"
        val RERA = "Investment Project - Rera (Tool tip)"
        val WHYINVESTCARD = "Investment Project - Why Invest Card"
        val VIEWONMAP = "Investment Project - View on Map"
        val KEYPILLARSCARD = "Investment Project - Key Pillars Card"
        val VIDEOIMAGECARD = "Investment Project - Video/Image Card"
        val SEEALLVIDEOIMAGE = "Investment Project -  See All Video/Image"
        val INVESTMENTDONTMISSOUT2 = "Investment Don't Miss Out2"
        val INVESTEMENTPROMISESCARD = "Investment Promises card"
        val INVESTEMENTSEEALLPROMISES = "Investment Project - See All Promises"
        val FAQCARDDETAILEDPAGE = "Investment Project - FAQ Card - Detailed page"
        val FAQCARD = "Investment Project - FAQ Card"
        val HAVEANYQUESTIONCARD = "Investment Project - Have Any Question ? Card"
        val STILLNOTCONVINCED = "Investment Project - Still Not Convinced"
        val SEEALLTESTIMONIALS = "Investment Project - See All Testominals"
        val MAPSEARCHDISTANCEFROM = "Map - Around the Project Card"
        val SIMILARINVESTMENTSCARD = "Investment Project -  Similar Investments Card"
        val SIMILARINVESTMENTSCARDAPPLYNOW ="Investment Project -  Similar Investments Card - Apply Now"
        val LANDSKUAPPLYNOW = "Investment Project - SKU Card - Apply Now"
        val FACILITYMANAGEMENT = "Profile Home - Facility Management"
        val MYACCOUNT = "Profile Home - My Account"
        val PAYMENTHISTORYSEEALL = "My Account - See All Payment History"
        val SECURITYANDSETTINGS = "Profile Home - Security & Settings"
        val SENDPUSHNOTIFICATIONS = "Security & Settings -  Send Push Notifications"
        val READSECURITYTIPS = "Security & Settings -  Read Security Tips"
        val HELPCENTER = "Profile Home - Help Center"
        val FAQS = "Help Center - FAQs"
        val PROFILEPRIVACYPOLICY = "Help Center - Privacy Policy"
        val PROFILEABOUTUS = "Help Center - About Us"
        val FEEDBACK = "Help Center - Feedback"
        val RATEUS = "Help Center - Rate Us"
        val EXPLORENEWINVESTMENT = "Portfolio Home - Explore New investment"
        val MANAGEINVESTMENT = "Portfolio Home - Manage Investment"
        val VIEWBOOKINGJOURNEY = "Portfolio Investment Details - View Booking Journey"
        val VIEWTIMELINE = "Portfolio Investment Details - View Timeline"
        val LATESTMEDIAGALLERY = "Portfolio Investment Details - Latest Media Gallery"
        val PORTFOLIOFAQS = "Portfolio Investment Details - Read All FAQs"
        val SKUCARD = "SKU Investment Project - SKU Card"
        val PROJECTAMENITITIESSEEALL = "Investment Project - See All Project Amenitites"
        val PROJECTAMENITOTIESCARD = "Investment Project - Project Amenitites Card"
        val LOCATIONINFRASEEALL = "Investment Project - See All Location Infra"
        val LOCATIONINFRA = "Investment Project - Location Infra Card"
        val OppDoc = "Opportunity doc - Tourism Around View More"
        val OPPDOCPROJECTAMENITITIES = "Opp Doc - Project Amenitites - View More"
        val OPPDOCAPPLYNOW = "Opportunity doc - Apply Now"
        val FAQREADALL = "Investment Project - See All FAQs"
        val SEEALLSKUCARD="Investment Project - See All Inventory Bucket"
        val INVESTMENTSEEALLPROMISES="Investment Project - See All Promises"
        val LANDSKUSTILLNOTCONVINCED="Land SKU - Still Not Convinced"


    }

    val mixpanelAPI = MixpanelAPI.getInstance(context, context.getString(R.string.MIXPANEL_KEY))

    fun identifyFunction(identify: String, track: String) {
        if (BuildConfig.BUILD_TYPE == "release") {
            mixpanelAPI.identify(identify)
            mixpanelAPI.track(track)
        }
    }
}