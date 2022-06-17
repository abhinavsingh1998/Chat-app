package com.emproto.hoabl.model

import com.emproto.networklayer.response.investment.PageManagementsOrNewInvestment

data class NewLaunchModel(
    val list :List<PageManagementsOrNewInvestment>,
    val isClicked:Boolean
)
