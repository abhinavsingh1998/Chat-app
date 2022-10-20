package com.emproto.hoabl.feature.home.views

import android.content.Context
import com.emproto.hoabl.R
import com.mixpanel.android.mpmetrics.MixpanelAPI
import javax.inject.Inject

class Mixpanel @Inject constructor(val context: Context) {
    companion object {
        //mixpanel events
        val DONTMISSOUT = "Don't Miss Out"
        val HOME="Home"
        val SHARE="Share"
        val REFERNOW="Refer Now"
        val SEARCH="Search"
        val LOGINTERMSANDCONDITIONS="Terms & Conditions"
        val WHATSAPPCOMMUNICATION="Whatsapp communication"
        val MASTHEAD="Mast Head"
        val PRIVACYPOLICY="Privacy Policy"
        val PROJECTCARD="Project Card"
        val APPLYNOW="Home Apply Now"
        val VIEWALLPROPERTIES="View All Properties"
        val LATESTUPDATECARD ="Latest Update card"
        val SEEALLUPDATES="See All ( Updates )"
        val SEEALLPROMISES="Sea All ( Promises )"
        val PROMISESCARD="Promises card"
        val INSIGHTSCARD="Insights Card"
        val SEEALLINSIGHTS="Sea All ( Insights )"
        val TESTIMONIALSEEALL="Testimonial ( See All )"
        val REFERYOURFRIEND="Refer Your Friend"
        val PORTFOLIO="Portfolio"
        val PROMISESTERMSANDCONDITIONS="Terms and Conditions"
        val PROMISECARD="Promise Card"
        val INVESTMENT="Investment"
        val NEWLAUNCHCARD="New Launch Card"
        val INVESTMENTAPPLYNOW="Investment Apply Now"
        val INVESTMENTDONTMISSOUT1="Investment Don't Miss Out1"
//        val COLLECTION1CARD="Collection 1 Card"
//        val INVESTMENTNEWLAUNCHES="Investment New Launches Apply Now"
      val MEDIAGALLERYSECTIONSELECTION ="Media Gallery Section Selection"
        val WISHLIST="Wishlist"
        val INVESTMENTSHARE="Share"
        val SEEALLIMAGESVIDEOS ="See All ( Images/Video )"
        val INVESTMENTNEWLAUNCHAPPLYNOW="Investment Apply Now"
        val RERA="RERA ( Tool TIp )"
        val WHYINVESTCARD="Why Invest Card"
        val VIEWONMAP="View On Map"
        val KEYPILLARSCARD="Key Pillars Card"
        val VIDEOIMAGECARD="Video/ Image Card"
        val SEEALLVIDEOIMAGE="See All ( Video/Image )"
        val INVESTMENTDONTMISSOUT2="Investment Don't Miss Out2"
        val INVESTEMENTPROMISESCARD="Investment Promises card"
        val INVESTEMENTSEEALLPROMISES="Investment See All ( Promises )"
        val FAQCARDDETAILEDPAGE="FAQ Card -Detailed page"
        val FAQCARD="FAQ Card"
        val HAVEANYQUESTIONCARD="Have Any Question ? Card"
        val STILLNOTCONVINCED="Still Not Convinced"
        val SEEALLTESTIMONIALS="See All ( Testimonials )"
        val MAPSEARCHDISTANCEFROM ="Map ( Search Distance from )"
        val SIMILARINVESTMENTSCARD="Similar Investments Card"
        val SIMILARINVESTMENTSCARDAPPLYNOW="Similar Investments Card Apply Now"
        val LANDSKUAPPLYNOW="Land SKU - Apply Now"
        val FACILITYMANAGEMENT="Facility Management"
        val MYACCOUNT="My Account"
        val PAYMENTHISTORYSEEALL="Payment History ( See All ) "
        val SECURITYANDSETTINGS="Security & Settings"
        val SENDPUSHNOTIFICATIONS="Send Push Notifications"
        val READSECURITYTIPS="Read Security Tips"
        val HELPCENTER="Help Center"
        val FAQS="FAQs"
        val PROFILEPRIVACYPOLICY="Privacy Policy"
        val PROFILEABOUTUS="About Us"
        val FEEDBACK="Feedback"
        val RATEUS="Rate Us"
        val EXPLORENEWINVESTMENT="Explore New investment"


    }

    val mixpanelAPI = MixpanelAPI.getInstance(context, context.getString(R.string.MIXPANEL_KEY))

    fun identifyFunction(identify: String, track: String) {
        mixpanelAPI.identify(identify)
        mixpanelAPI.track(track)
    }

}