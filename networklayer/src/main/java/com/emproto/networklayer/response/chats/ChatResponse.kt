package com.emproto.networklayer.response.chats

import java.io.Serializable

data class ChatResponse(
    val code: Int,
    val `data`: List<CData>,
    val message: String
):Serializable

data class CData(
    val isInvested: Boolean,
    val lastMessage: LastMsg,
    val project: Project,
    val unreadCount: Int
):Serializable

data class Project(
    val areaStartingFrom: String,
    val crmId: String,
    val crmProjectId: String,
    val id: Int,
    val launchName: String,
    val priceStartingFrom: String,
    val projectContent: ProjectContent
):Serializable

data class ProjectContent(
    val crmLaunchPhaseId: Int,
    val id: Int,
    val launchName: String,
    val projectCoverImages: ProjectCoverImages
):Serializable

data class ProjectCoverImages(
    val chatListViewPageMedia: ChatListViewPageMedia,
    val chatPageMedia: ChatPageMedia,
    val collectionListViewPageMedia: CollectionListViewPageMedia,
    val homePageMedia: HomePageMedia,
    val newInvestmentPageMedia: NewInvestmentPageMedia,
    val portfolioPageMedia: PortfolioPageMedia
):Serializable

data class ChatListViewPageMedia(
    val key: String,
    val name: String,
    val value: Value
):Serializable

data class ChatPageMedia(
    val key: String,
    val name: String,
    val value: ValueX
):Serializable

data class CollectionListViewPageMedia(
    val key: String,
    val name: String,
    val value: ValueXX
):Serializable

data class HomePageMedia(
    val key: String,
    val name: String,
    val value: ValueXXX
):Serializable

data class NewInvestmentPageMedia(
    val key: String,
    val name: String,
    val value: ValueXXXX
):Serializable

data class PortfolioPageMedia(
    val key: String,
    val name: String,
    val value: ValueXXXXX
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

data class LastMsg(
    val conversationId: Any,
    val createdAt: String,
    val documents: Any,
    val id: Int,
    val message: String,
    val options: Any,
    val origin: String,
    val projectId: String,
    val replyTo: Any,
    val updatedAt: String,
    val userId: Int
):Serializable