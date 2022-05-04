package com.emproto.networklayer.response.investment

data class PdOpprotunityDoc(
    val aboutProjects: PdAboutProjects,
    val bannerImage: PdBannerImage,
    val createdAt: String,
    val currentInfraStory: PdCurrentInfraStory,
    val escalationGraph: PdEscalationGraph,
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
    val projectAminities: List<PdProjectAminity>,
    val projectContentId: Int,
    val tourismAround: PdTourismAround,
    val upcomingInfraStory: PdUpcomingInfraStory,
    val updatedAt: String
)