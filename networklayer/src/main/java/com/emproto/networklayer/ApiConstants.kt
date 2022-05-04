package com.emproto.networklayer

object ApiConstants {
    const val COMMON_MSG = "Something Went Wrong"
    val BASE_URL_DEV =
        "http://hoabl-backend-dev-306342355.ap-south-1.elb.amazonaws.com/"

    const val GENERATE_OTP = "hoabl-customer/generate_otp"
    const val VALIDATE_OTP = "hoabl-customer/verify_otp"
    const val SET_NAME = "hoabl-customer/set_name"
    const val TROUBLE_SIGNING = "hoabl-customer/case/sign-in"

    const val HOME = "hoabl-admin/get-page"
    const val PROMISES = "hoabl-admin/get-page"
    const val INVESTMENT = "hoabl-admin/get-page"
    const val INVESTMENT_PROJECT_DETAIL = "hoabl-admin/project-contents/{id}"


}
