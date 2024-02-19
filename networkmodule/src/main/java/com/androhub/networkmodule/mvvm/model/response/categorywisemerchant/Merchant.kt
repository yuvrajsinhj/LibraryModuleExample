package com.androhub.networkmodule.mvvm.model.response.categorywisemerchant

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Merchant : Serializable {
    @SerializedName("categoryName")
    @Expose
    var categoryName: String? = null


    @SerializedName("merchantName")
    @Expose
    var merchantName: String? = null

    @SerializedName("catID")
    @Expose
    var catID: String? = null

    @SerializedName("logoURL")
    @Expose
    var logoURL: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null


    @SerializedName("cname")
    @Expose
    var cname: String? = null

    @SerializedName("userID")
    @Expose
    var userID: String? = null

    @SerializedName("displayImage")
    @Expose
    var displayImage: String? = null

    @SerializedName("branchCount")
    @Expose
    var branchCount: Int? = null

}