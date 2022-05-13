package com.emproto.networklayer.response.chats

import java.io.Serializable

data class ChatResponse(
    val code: Int,
    val message: String,
    val chatList: ChatList

):Serializable


data class ChatList(
    val `data`: List<Data>
):Serializable

data class Data(
    val isInvested: Boolean,
    val lastMessage: Any?,
    val project: List<Project>,
    val unreadCount: Any?
):Serializable

data class Project(
    val id: Int,
    val launchName: String,
    val project: ProjectX,
    val projectCoverImages: List<ProjectCoverImages>,
    val projectId: Int
):Serializable

data class ProjectX(
    val crmProjectId: String
):Serializable

data class ProjectCoverImages(
    val chatListViewPageMedia: List<ChatListViewPageMedia>,
    val chatPageMedia: ChatPageMedia,
    val collectionListViewPageMedia: CollectionListViewPageMedia,
    val homePageMedia: HomePageMedia,
    val newInvestmentPageMedia: NewInvestmentPageMedia,
    val portfolioPageMedia: PortfolioPageMedia
):Serializable



data class ChatListViewPageMedia(
    val key: String,
    val name: String,
    val value: List<Value>
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