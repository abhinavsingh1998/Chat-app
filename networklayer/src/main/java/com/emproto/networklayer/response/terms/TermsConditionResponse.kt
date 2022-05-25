package com.emproto.networklayer.response.terms

data class TermsConditionResponse(
    val code: Int,
    val `data`: Page
)

data class Page(val page: Data)