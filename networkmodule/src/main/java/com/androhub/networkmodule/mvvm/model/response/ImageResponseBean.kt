package com.androhub.networkmodule.mvvm.model.response


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ImageResponseBean : Serializable {
    @SerializedName("data")
    var data: String=""

    @SerializedName("message")
    var message: String=""

    @SerializedName("status")
    var status: Int=0
}
