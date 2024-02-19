package com.androhub.networkmodule.mvvm.model.response.categorywisemerchant

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class CategoryWiseMerchant : Serializable {
    @SerializedName("status")
    var status: Int? = null

    @SerializedName("data")
    var data: List<Datum>? = null

    @SerializedName("message")
    var message: String? = null

}