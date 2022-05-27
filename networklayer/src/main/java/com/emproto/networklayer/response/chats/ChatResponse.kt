package com.emproto.networklayer.response.chats


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChatResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val chatList: ArrayList<ChatList>,
    @SerializedName("message")
    val message: String
):Serializable {
    data class ChatList(
        @SerializedName("isInvested")
        val isInvested: Boolean,
        @SerializedName("lastMessage")
        val lastMessage: Any?,
        @SerializedName("project")
        val project: Project,
        @SerializedName("unreadCount")
        val unreadCount: Any?
    ) : Serializable {
        data class Project(
            @SerializedName("id")
            val id: Int,
            @SerializedName("launchName")
            val launchName: String,
            @SerializedName("project")
            val project: Project,
            @SerializedName("projectCoverImages")
            val projectCoverImages: ProjectCoverImages,
            @SerializedName("projectId")
            val projectId: Int
        ):Serializable {
            data class Project(
                @SerializedName("crmProjectId")
                val crmProjectId: String
            ):Serializable

            data class ProjectCoverImages(
                @SerializedName("chatListViewPageMedia")
                val chatListViewPageMedia: ChatListViewPageMedia,
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
            ) :Serializable{
                data class ChatListViewPageMedia(
                    @SerializedName("key")
                    val key: String,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("value")
                    val value: Value
                ) :Serializable{
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
                    ):Serializable
                }

                data class ChatPageMedia(
                    @SerializedName("key")
                    val key: String,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("value")
                    val value: Value
                ) :Serializable{
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
                    ):Serializable
                }

                data class CollectionListViewPageMedia(
                    @SerializedName("key")
                    val key: String,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("value")
                    val value: Value
                ) :Serializable{
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
                    ):Serializable
                }

                data class HomePageMedia(
                    @SerializedName("key")
                    val key: String,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("value")
                    val value: Value
                ) :Serializable{
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
                    ):Serializable
                }

                data class NewInvestmentPageMedia(
                    @SerializedName("key")
                    val key: String,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("value")
                    val value: Value
                ):Serializable {
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
                    ):Serializable
                }

                data class PortfolioPageMedia(
                    @SerializedName("key")
                    val key: String,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("value")
                    val value: Value
                ) :Serializable{
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
                    ):Serializable
                }
            }
        }
    }
}