package com.androhub.networkmodule.mvvm.model.response

import com.google.gson.annotations.SerializedName

  class GetSegmentResponseBean  {
    @SerializedName("data")
    var dataSegment: DataSegment? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("status")
    var status: Int? = null // 200

    data class DataSegment(
        @SerializedName("createdAt")
        var createdAt: Long, // 1640676094874
        @SerializedName("default")
        var default: Boolean, // true
        @SerializedName("id")
        var id: String, // 90af8915-6240-4410-b74d-c04f50b955ba
        @SerializedName("isDeleted")
        var isDeleted: Boolean, // false
        @SerializedName("isError")
        var isError: String, // Oh no! Your should contain max 2 characters!
        @SerializedName("isInValidName")
        var isInValidName: Boolean, // false
        @SerializedName("isInValidPrefix")
        var isInValidPrefix: Boolean, // false
        @SerializedName("isInValidTicketPerRound")
        var isInValidTicketPerRound: Boolean, // false
        @SerializedName("isValidName")
        var isValidName: Boolean, // true
        @SerializedName("isValidPrefix")
        var isValidPrefix: Boolean, // true
        @SerializedName("isValidTicketPerRound")
        var isValidTicketPerRound: Boolean, // true
        @SerializedName("merchantID")
        var merchantID: String, // 5aa6fb90-75e1-4ac4-b0ef-9c98a71b9a18
        @SerializedName("name")
        var segmentName: String, // Elite
        @SerializedName("prefix")
        var prefix: String, // PE
        @SerializedName("sortOrder")
        var sortOrder: Int, // 0
        @SerializedName("ticketPerRound")
        var ticketPerRound: Int, // 5
        @SerializedName("updatedAt")
        var updatedAt: Long // 1640676094874
    )
}