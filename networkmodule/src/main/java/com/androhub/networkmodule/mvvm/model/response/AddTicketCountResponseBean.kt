package com.androhub.networkmodule.mvvm.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddTicketCountResponseBean {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("status")
    @Expose
    val status: Int? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data(
    @SerializedName("count")
    var count: Int? = null,
    @SerializedName("waitTime")
    var waitTime: Int = 0,
        @SerializedName("breackTime")
    var breakTime: Int = 0,
        @SerializedName("agentCount")
    val agentCount: Int=0
)
}