package com.androhub.networkmodule.mvvm.model.response.categorywisemerchant

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Datum : Serializable {
    @SerializedName("categoryID")
    @Expose
    var categoryID: String? = null

    @SerializedName("categoryName")
    @Expose
    var categoryName: String? = null

    @SerializedName("isDonate")
    @Expose
    var isDonate: Boolean? = false



    @SerializedName("merchant")
    @Expose
    var merchant: List<Merchant>? = ArrayList()

}