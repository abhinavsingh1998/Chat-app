package com.emproto.networklayer.response.investment

data class mData(
    val projectContent: PdData,
    val projectContentsAndFaqs: List<ProjectContentsAndFaq>,
    val pageManagementContent:PageManagementContent
)