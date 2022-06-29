package com.emproto.networklayer.response.profile

 data class KycUpload(
     var documentName: String,
     var documentCategory:Int,
     var documentType:Int,
     var status: String,
     var path: String = "",
     var name: String = ""
 )