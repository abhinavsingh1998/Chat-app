package com.emproto.networklayer.response.investment

import com.emproto.networklayer.response.home.PageManagementAndNewInvestments
import java.io.Serializable

data class PageManagementsOrNewInvestment(
    val address: Address,
    val areaStartingFrom: String,
    val fomoContent: FomoContent,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val id: Int,
    val isEscalationGraphActive: Boolean,
    val launchName: String,
    val pageManagementAndNewInvestments: PageManagementAndNewInvestments,
    val priceStartingFrom: String,
    val projectCoverImages: ProjectCoverImages,
    val shortDescription: String,
    val mediaGalleryOrProjectContent: MediaGalleryOrProjectContent
):Serializable
