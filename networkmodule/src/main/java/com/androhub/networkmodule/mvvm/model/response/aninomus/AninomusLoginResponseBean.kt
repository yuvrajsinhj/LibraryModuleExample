package com.androhub.networkmodule.mvvm.model.response.aninomus


import com.google.gson.annotations.SerializedName

class AninomusLoginResponseBean {
    @SerializedName("data")
    var data: LoginBean?=null
    @SerializedName("message")
    var message: String?=""
    @SerializedName("status")
    var status: Int=0
}

