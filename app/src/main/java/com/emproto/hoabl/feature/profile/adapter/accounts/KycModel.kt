package com.emproto.hoabl.feature.profile.adapter.accounts

import org.w3c.dom.DocumentType

data class KycModel (
    var documentName:String,
    var documentType:Int,
    var status:String
        )