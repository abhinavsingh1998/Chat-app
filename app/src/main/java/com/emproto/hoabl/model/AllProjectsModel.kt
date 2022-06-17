package com.emproto.hoabl.model

import com.emproto.networklayer.response.investment.ApData

data class AllProjectsModel(
    val list :List<ApData>,
    val isClicked:Boolean
)
