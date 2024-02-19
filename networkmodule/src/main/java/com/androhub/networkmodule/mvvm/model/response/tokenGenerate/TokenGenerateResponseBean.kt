package com.androhub.networkmodule.mvvm.model.response.tokenGenerate


import com.google.gson.annotations.SerializedName

class TokenGenerateResponseBean{
    @SerializedName("data")
    var data: TokenBean?=null
    @SerializedName("message")
    var message: String?=null
    @SerializedName("status")
    var status: Int=0
}

