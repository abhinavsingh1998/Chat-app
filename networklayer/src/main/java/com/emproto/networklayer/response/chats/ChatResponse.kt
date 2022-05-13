package com.emproto.networklayer.response.chats

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ChatResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val chatList: List<Chat>

)

data class Chat(
    @SerializedName("isInvested")
    val isInvested: Boolean,
    @SerializedName("lastMessage")
    val lastMessage: Any?,
    @SerializedName("project")
    val project: List<Project>,
    @SerializedName("unreadCount")
    val unreadCount: Any?
) : Serializable

data class Project(
    @SerializedName("id")
    val id: Int,
    @SerializedName("launchName")
    val launchName: String,
    @SerializedName("project")
    val project: ProjectX,
    @SerializedName("projectCoverImages")
    val projectCoverImages: List<ProjectCoverImages>,
    @SerializedName("projectId")
    val projectId: Int
)

data class ProjectX(
    @SerializedName("crmProjectId")
    val crmProjectId: String
)

data class ProjectCoverImages(
    @SerializedName("chatListViewPageMedia")
    val chatListViewPageMedia: List<ChatListViewPageMedia>,
    @SerializedName("chatPageMedia")
    val chatPageMedia: ChatPageMedia,
    @SerializedName("collectionListViewPageMedia")
    val collectionListViewPageMedia: CollectionListViewPageMedia,
    @SerializedName("homePageMedia")
    val homePageMedia: HomePageMedia,
    @SerializedName("newInvestmentPageMedia")
    val newInvestmentPageMedia: NewInvestmentPageMedia,
    @SerializedName("portfolioPageMedia")
    val portfolioPageMedia: PortfolioPageMedia
)



data class ChatListViewPageMedia(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: List<Value>
)

data class ChatPageMedia(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: ValueX
)

data class CollectionListViewPageMedia(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: ValueXX
)

data class HomePageMedia(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: ValueXXX
)

data class NewInvestmentPageMedia(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: ValueXXXX
)

data class PortfolioPageMedia(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: ValueXXXXX
)
data class Value(
    @SerializedName("height")
    val height: Int,
    @SerializedName("mediaType")
    val mediaType: String,
    @SerializedName("size")
    val size: Double,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)
data class ValueX(
    @SerializedName("height")
    val height: Int,
    @SerializedName("mediaType")
    val mediaType: String,
    @SerializedName("size")
    val size: Double,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)
data class ValueXX(
    @SerializedName("height")
    val height: Int,
    @SerializedName("mediaType")
    val mediaType: String,
    @SerializedName("size")
    val size: Double,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)
data class ValueXXX(
    @SerializedName("height")
    val height: Int,
    @SerializedName("mediaType")
    val mediaType: String,
    @SerializedName("size")
    val size: Double,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)
data class ValueXXXX(
    @SerializedName("height")
    val height: Int,
    @SerializedName("mediaType")
    val mediaType: String,
    @SerializedName("size")
    val size: Double,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)

data class ValueXXXXX(
    @SerializedName("height")
    val height: Int,
    @SerializedName("mediaType")
    val mediaType: String,
    @SerializedName("size")
    val size: Double,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)