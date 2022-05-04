package com.emproto.networklayer.response.investment

data class Data(
    val collectionOne: CollectionOne,
    val collectionTwo: CollectionTwo,
    val collectionOneCollectionId: Int,
    val collectionTwoCollectionId: Int,
    val collectionTwoProjects: Any,
    val createdAt: String,
    val hoablPagesOrPromises: List<Any>,
    val hoablPromiseDisplayName: Any,
    val homePagesOrPromises: List<Any>,
    val id: Int,
    val insightsDisplaName: Any,
    val isCollectionOneActive: Boolean,
    val isHoablPromiseActive: Any,
    val isInsightsActive: Any,
    val isLatestUpdatesActive: Any,
    val isMastheadActive: Any,
    val isNewInvestmentsActive: Any,
    val isPromisesActive: Any,
    val isPublished: Boolean,
    val isTestimonialsActive: Any,
    val iscollectionTwoProjectsActive: Any,
    val latestUpdatesDisplayName: Any,
    val mastheadSection: Any,
    val mythBuster: Any,
    val newInvestments: NewInvestments,
    val newInvestmentsCollectionId: Any,
    val numberOfScreens: Any,
    val pageManagementOrInsights: List<Any>,
    val pageManagementOrLatestUpdates: List<Any>,
    val pageManagementsOrCollectionOneModels: List<PageManagementsOrCollectionOneModel>,
    val pageManagementsOrCollectionTwoModels: List<PageManagementsOrCollectionOneModel>,
    val pageManagementsOrNewInvestments: List<PageManagementsOrNewInvestment>,
    val pageManagementsOrTestimonials: List<Any>,
    val pageName: String,
    val pageType: String,
    val playTimeInSeconds: Any,
    val promiseSection: Any,
    val promisesDisplayName: Any,
    val propositionScreens: Any,
    val sectionName: Any,
    val splashScreenImage: Any,
    val testimonialsDisplayName: Any,
    val promotionAndOffersMedia: PromotionAndOffersMedia,
    val address: Address,
    val areaRange: AreaRange,
    val fomoContent: FomoContent,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val inventoryBucketContents: List<InventoryBucketContent>,
    val isEscalationGraphActive: Boolean,
    val isInventoryBucketActive: Boolean,
    val isKeyPillarsActive: Boolean,
    val isLocationInfrastructureActive: Boolean,
    val isOffersAndPromotionsActive: Boolean,
    val keyPillars: KeyPillars,
    val launchName: String,
    val locationInfrastructure: LocationInfrastructure,
    val mediaGalleries: List<MediaGallery>,
    val offersAndPromotions: OffersAndPromotions,
    val opprotunityDocs: List<OpprotunityDoc>,
    val priceRange: PriceRange,
    val projectId: Int,
    val projectTimelines: List<ProjectTimeline>,
    val shortDescription: String,
    val status: String,
    val updatedAt: String
)