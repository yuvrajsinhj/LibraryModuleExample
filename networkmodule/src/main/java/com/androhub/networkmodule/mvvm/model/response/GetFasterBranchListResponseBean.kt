package com.androhub.networkmodule.mvvm.model.response


import com.google.gson.annotations.SerializedName

class GetFasterBranchListResponseBean(
    @SerializedName("data")
    var data: ArrayList<Data?> = ArrayList(),
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Int? = null
) {
    class Data(
        @SerializedName("branchId")
        var branchId: String? = null,
        @SerializedName("branchName")
        var branchName: String? = null,
        @SerializedName("count")
        var count: Int? = null,
        @SerializedName("lat")
        var lat: Double? = null,
        @SerializedName("long")
        var long: Double? = 0.0,
        @SerializedName("merchantID")
        var merchantID: String? = "",
        @SerializedName("waitTime")
        var waitTime: Double? = 0.0
    )
}