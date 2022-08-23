package com.example.portfolioui.models

import com.emproto.networklayer.response.portfolio.prtimeline.TimeLine

data class StepsModel(val viewType: Int, val timeline:TimeLine, val type:String="")
