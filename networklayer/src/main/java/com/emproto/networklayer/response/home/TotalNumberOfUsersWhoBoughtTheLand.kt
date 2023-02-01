package com.emproto.networklayer.response.home

import java.io.Serializable

data class TotalNumberOfUsersWhoBoughtTheLand(
    val displayName: String,
    val value: String,
    val shouldDisplay:Boolean
):Serializable