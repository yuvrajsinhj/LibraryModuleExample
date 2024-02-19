package com.androhub.networkmodule.mvvm.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GeneralResponseBean {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null
}