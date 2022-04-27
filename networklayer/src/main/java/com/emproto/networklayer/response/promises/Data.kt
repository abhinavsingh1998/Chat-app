package com.emproto.networklayer.response.promises

data class Data(
    val hoablPagesOrPromises: List<HoablPagesOrPromise>,
    val id: Int,
    val isPublished: Boolean,
    val pageName: String,
    val pageType: String,
    val projectPagesOrPromises: List<ProjectPagesOrPromise>,
    val promiseSection: PromiseSection
)