package com.androhub.networkmodule.mvvm.model.response


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AddTicketResponseBean(
    @SerializedName("data")
    var data: Data? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Int? = null
):Serializable {
    class Data(
        @SerializedName("location")
        var location: String?="",
        @SerializedName("locationArabic")
        var locationArabic: String?="",
        @SerializedName("isCheckIn")
        var isCheckIn: Boolean? = false,
        @SerializedName("approxTime")
        var approxTime: Double? = null,
        @SerializedName("branchId")
        var branchId: String? = null,
        @SerializedName("branchName")
        var branchName: String? = null,
        @SerializedName("createdAt")
        var createdAt: Long? = null,
        @SerializedName("counter")
        var counter: String? = "",
        @SerializedName("customerId")
        var customerId: String? = null,
        @SerializedName("customerName")
        var customerName: String? = null,
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("number")
        var number: String? = null,
        @SerializedName("queueId")
        var queueId: String? = null,
        @SerializedName("serviceId")
        var serviceId: String? = null,
        @SerializedName("serviceName")
        var serviceName: String? = null,
         @SerializedName("updatedAt")
        var updatedAt: Long? = null,
        @SerializedName("appointmentEndTime")
        var appointmentEndTime: String? = null,
        @SerializedName("appointmentTime")
        var appointmentTime: String? = null,
        @SerializedName("ticketType")
        var ticketType: String? = null,
        @SerializedName("userId")
        var userId: String? = null,
        @SerializedName("userName")
        var userName: String? = null,
        @SerializedName("merchantName")
        var merchantName: String? = null,
        @SerializedName("merchantLogo")
        var merchantLogo: String? = null,
        @SerializedName("processedByUserId ")
        var processedByUserId : String? = "",
        @SerializedName("categoryID")
        var categoryID: String? = null,
        @SerializedName("categoryName")
        var categoryName: String? = null,


        @SerializedName("starttime")
        var starttime: String? = null,
        @SerializedName("endtime")
        var endTime: String?=null,
        @SerializedName("eventName")
        var eventName: String? = null,
        @SerializedName("eventLogo")
        var eventLogo: String? = null,
        @SerializedName("textUnderTimeFinish")
        var textUnderTimeFinish: String? = null,
        @SerializedName("arabicTextUnderTimeFinish")
        var arabicTextUnderTimeFinish: String? = null,
        @SerializedName("textUnderTime")
        var textUnderTime: String?=null,
        @SerializedName("arabicTextUnderTime")
        var arabicTextUnderTime: String?=null,

        var date: String? = null,
        var status: String? = null,
        var logoURL: String? = null,
        var servingTime: Double = 0.0,
        var customersCount: Int = 0,
        var  notCallDetailsAPI: Boolean  = false,
        var  isFromPast: Boolean  = false

    ):Serializable
}