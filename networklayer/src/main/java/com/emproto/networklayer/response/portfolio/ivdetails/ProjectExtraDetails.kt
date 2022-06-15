package com.emproto.networklayer.response.portfolio.ivdetails

import com.emproto.networklayer.response.portfolio.dashboard.Address
import com.emproto.networklayer.response.portfolio.dashboard.GeneralInfoEscalationGraph
import com.emproto.networklayer.response.portfolio.dashboard.ProjectIcon

data class ProjectExtraDetails(
    val address: Address,
    val projectIco: ProjectIcon,
//    val latitude: String,
//    val longitude: String,
//    val altitude: String?,
    val graphData:GeneralInfoEscalationGraph,
    val launchName:String
)
