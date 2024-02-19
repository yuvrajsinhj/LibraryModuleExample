package com.androhub.networkmodule.mvvm.model.response


import com.google.gson.annotations.SerializedName

class GetTicketHistoryResponseModel(
    @SerializedName("data")
    var data: Data? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Int? = null
) {
    class Data(
        @SerializedName("count")
        var count: Int? = null,
        @SerializedName("currentTime")
        var serverCurrentTime: Long? = 0,
        @SerializedName("ticket")
        var ticket: ArrayList<TicketBean> = ArrayList()
    )
}