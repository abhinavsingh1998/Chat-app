package com.emproto.networklayer.response.profile


import com.google.gson.annotations.SerializedName

data class UploadDocumentResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("response")
        val response: Response
    ) {
        data class Response(
            @SerializedName("d")
            val d: D
        ) {
            data class D(
                @SerializedName("Author")
                val author: Author,
                @SerializedName("CheckInComment")
                val checkInComment: String,
                @SerializedName("CheckOutType")
                val checkOutType: Int,
                @SerializedName("CheckedOutByUser")
                val checkedOutByUser: CheckedOutByUser,
                @SerializedName("ContentTag")
                val contentTag: String,
                @SerializedName("CustomizedPageStatus")
                val customizedPageStatus: Int,
                @SerializedName("ETag")
                val eTag: String,
                @SerializedName("EffectiveInformationRightsManagementSettings")
                val effectiveInformationRightsManagementSettings: EffectiveInformationRightsManagementSettings,
                @SerializedName("Exists")
                val exists: Boolean,
                @SerializedName("InformationRightsManagementSettings")
                val informationRightsManagementSettings: InformationRightsManagementSettings,
                @SerializedName("IrmEnabled")
                val irmEnabled: Boolean,
                @SerializedName("Length")
                val length: String,
                @SerializedName("Level")
                val level: Int,
                @SerializedName("LinkingUri")
                val linkingUri: Any?,
                @SerializedName("LinkingUrl")
                val linkingUrl: String,
                @SerializedName("ListItemAllFields")
                val listItemAllFields: ListItemAllFields,
                @SerializedName("LockedByUser")
                val lockedByUser: LockedByUser,
                @SerializedName("MajorVersion")
                val majorVersion: Int,
                @SerializedName("__metadata")
                val metadata: Metadata,
                @SerializedName("MinorVersion")
                val minorVersion: Int,
                @SerializedName("ModifiedBy")
                val modifiedBy: ModifiedBy,
                @SerializedName("Name")
                val name: String,
                @SerializedName("Properties")
                val properties: Properties,
                @SerializedName("ServerRelativeUrl")
                val serverRelativeUrl: String,
                @SerializedName("TimeCreated")
                val timeCreated: String,
                @SerializedName("TimeLastModified")
                val timeLastModified: String,
                @SerializedName("Title")
                val title: Any?,
                @SerializedName("UIVersion")
                val uIVersion: Int,
                @SerializedName("UIVersionLabel")
                val uIVersionLabel: String,
                @SerializedName("UniqueId")
                val uniqueId: String,
                @SerializedName("VersionEvents")
                val versionEvents: VersionEvents,
                @SerializedName("Versions")
                val versions: Versions
            ) {
                data class Author(
                    @SerializedName("__deferred")
                    val deferred: Deferred
                ) {
                    data class Deferred(
                        @SerializedName("uri")
                        val uri: String
                    )
                }

                data class CheckedOutByUser(
                    @SerializedName("__deferred")
                    val deferred: Deferred
                ) {
                    data class Deferred(
                        @SerializedName("uri")
                        val uri: String
                    )
                }

                data class EffectiveInformationRightsManagementSettings(
                    @SerializedName("__deferred")
                    val deferred: Deferred
                ) {
                    data class Deferred(
                        @SerializedName("uri")
                        val uri: String
                    )
                }

                data class InformationRightsManagementSettings(
                    @SerializedName("__deferred")
                    val deferred: Deferred
                ) {
                    data class Deferred(
                        @SerializedName("uri")
                        val uri: String
                    )
                }

                data class ListItemAllFields(
                    @SerializedName("__deferred")
                    val deferred: Deferred
                ) {
                    data class Deferred(
                        @SerializedName("uri")
                        val uri: String
                    )
                }

                data class LockedByUser(
                    @SerializedName("__deferred")
                    val deferred: Deferred
                ) {
                    data class Deferred(
                        @SerializedName("uri")
                        val uri: String
                    )
                }

                data class Metadata(
                    @SerializedName("id")
                    val id: String,
                    @SerializedName("type")
                    val type: String,
                    @SerializedName("uri")
                    val uri: String
                )

                data class ModifiedBy(
                    @SerializedName("__deferred")
                    val deferred: Deferred
                ) {
                    data class Deferred(
                        @SerializedName("uri")
                        val uri: String
                    )
                }

                data class Properties(
                    @SerializedName("__deferred")
                    val deferred: Deferred
                ) {
                    data class Deferred(
                        @SerializedName("uri")
                        val uri: String
                    )
                }

                data class VersionEvents(
                    @SerializedName("__deferred")
                    val deferred: Deferred
                ) {
                    data class Deferred(
                        @SerializedName("uri")
                        val uri: String
                    )
                }

                data class Versions(
                    @SerializedName("__deferred")
                    val deferred: Deferred
                ) {
                    data class Deferred(
                        @SerializedName("uri")
                        val uri: String
                    )
                }
            }
        }
    }
}