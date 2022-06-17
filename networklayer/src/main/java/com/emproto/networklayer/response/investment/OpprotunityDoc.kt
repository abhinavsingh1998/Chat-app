package com.emproto.networklayer.response.investment

data class OpprotunityDoc(
    val aboutProjects: AboutProjects,
    val bannerImage: BannerImage,
    val createdAt: String,
    val currentInfraStory: CurrentInfraStory,
    val escalationGraph: EscalationGraph,
    val id: Int,
    val isAboutProjectsActive: Boolean,
    val isBannerImageActive: Boolean,
    val isCurrentInfraStoryActive: Boolean,
    val isEscalationGraphActive: Boolean,
    val isPageFooterActive: Boolean,
    val isProjectAminitiesActive: Boolean,
    val isTourismAroundActive: Boolean,
    val isUpcomingInfraStoryActive: Boolean,
    val pageFooter: String,
    val projectAminities: List<ProjectAminity>,
    val projectContentId: Int,
    val tourismAround: TourismAround,
    val upcomingInfraStory: UpcomingInfraStory,
    val updatedAt: String,
    val whyToInvestMedia: WhyToInvestMedia
)