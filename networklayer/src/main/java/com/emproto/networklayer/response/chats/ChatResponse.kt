package com.emproto.networklayer.response.chats

import com.emproto.networklayer.response.Address
import java.io.Serializable

data class ChatResponse(
    val code: Int,
    val `data`: List<CData>,
    val message: String
):Serializable

data class CData(
    val booking: Booking,
    val investment: Investment,
    val isInvested: Boolean,
    val lastMessage: LastMessage,
    val name: String,
    val portfolioData: Any,
    val primaryOwner: String,
    val topicId: String,
    val unreadCount: Int
):Serializable

data class Booking(
    val afsStatus: Any,
    val agreementValue: Int,
    val allocationDate: Any,
    val applicationId: String,
    val bookingStatus: Int,
    val camCharges: Int,
    val camGST: Double,
    val corpusCharges: Int,
    val corpusGST: Double,
    val createdAt: String,
    val crmBookingId: String,
    val crmCoapplicantId: Any,
    val crmInventory: CrmInventory,
    val crmInventoryBucketId: String,
    val crmInventoryId: String,
    val crmLaunchPhase: CrmLaunchPhase,
    val crmLaunchPhaseId: String,
    val crmProjectId: String,
    val doesAgreementValueIncludeInfraValue: Boolean,
    val id: Int,
    val infraGST: Double,
    val infraValue: Int,
    val isPOAAlloted: Boolean,
    val isPOARequired: Boolean,
    val owners: Any,
    val ownershipDate: Any,
    val possesionDate: Any,
    val primaryApplicant: PrimaryApplicant,
    val registrationCharges: Int,
    val registrationDate: Any,
    val registrationGST: Any,
    val registrationNumber: Any,
    val stampdutyCharges: Int,
    val stampdutyGST: Any,
    val updatedAt: String,
    val userId: String
):Serializable

data class Investment(
    val crmLaunchPhase: CrmLaunchPhase?
):Serializable

data class CrmLaunchPhaseX(
    val crmId: String,
    val crmProjectId: String,
    val projectContent: ProjectContentX
):Serializable

data class ProjectContent(
    val crmLaunchPhaseId: Int,
    val id: Int,
    val launchName: String,
    val projectCoverImages: ProjectCoverImages?,
    val address:Address
):Serializable

data class ProjectContentX(
    val crmLaunchPhaseId: String,
    val launchName: String,
    val projectCoverImages: ProjectCoverImagesX
)

data class ChatListViewPageMedia(
    val key: String,
    val name: String,
    val value: Value?
):Serializable

data class ProjectCoverImages(
    val chatPageMedia: ChatPageMedia,
    val homePageMedia: HomePageMedia,
    val portfolioPageMedia: PortfolioPageMedia,
    val chatListViewPageMedia:ChatListViewPageMedia?,
    val newInvestmentPageMedia: NewInvestmentPageMedia,
    val collectionListViewPageMedia: CollectionListViewPageMedia,
    ):Serializable

data class ProjectCoverImagesX(
    val chatListViewPageMedia: ChatListViewPageMediaX,
    val chatPageMedia: ChatPageMediaX
):Serializable

data class ChatPageMediaX(
    val key: String,
    val name: String,
    val value: ValueXXXXXX
):Serializable

data class ValueXXXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
):Serializable

data class PrimaryApplicant(
    val firstName: String,
    val id: Int,
    val lastName: String
):Serializable

data class ChatListViewPageMediaX(
    val key: String,
    val name: String,
    val value: ValueXXXXXX
):Serializable

data class CollectionListViewPageMedia(
    val key: String,
    val name: String,
    val value: Value
):Serializable

data class ChatPageMedia(
    val key: String,
    val name: String,
    val value: ValueX
):Serializable

data class CrmInventory(
    val crmId: String,
    val id: Int,
    val name: String
):Serializable

data class HomePageMedia(
    val key: String,
    val name: String,
    val value: Value
):Serializable

data class CrmLaunchPhase(
    val crmId: String,
    val crmProjectId: String,
    val id: Int,
    val projectContent: ProjectContent?
):Serializable

data class NewInvestmentPageMedia(
    val key: String,
    val name: String,
    val value: Value
):Serializable

data class PortfolioPageMedia(
    val key: String,
    val name: String,
    val value: Value
):Serializable

data class Value(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
):Serializable

data class ValueX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
):Serializable

data class ValueXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
):Serializable

data class ValueXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
):Serializable

data class ValueXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
):Serializable

data class ValueXXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
):Serializable

data class LastMessage(
    val createdAt: String,
    val message: String,
    val topicId: String
):Serializable