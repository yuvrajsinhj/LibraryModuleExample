package com.androhub.networkmodule.mvvm.model.response

import com.androhub.networkmodule.utils.Constant
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TicketBean(
    @SerializedName("isRebook")
    var isRebook: Boolean? = false,
    @SerializedName("isCheckIn")
    var isCheckIn: Boolean? = false,
    @SerializedName("approxTime")
    var approxTime: Double? = null,
    @SerializedName("branchId")
    var branchId: String? = "",
    @SerializedName("branchName")
    var branchName: String? = "",
    @SerializedName("completedAt")
    var completedAt: Long? = null,
    @SerializedName("createdAt")
    var createdAt: Long? = null,

    @SerializedName("customerId")
    var customerId: String? = "",
    @SerializedName("customerName")
    var customerName: String? = "null",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("isDeleted")
    var isDeleted: Boolean? = null,
    @SerializedName("merchantId")
    var merchantId: String? = "",
    @SerializedName("merchantLogo")
    var merchantLogo: String? = "",
    @SerializedName("waitTime")
    var waitTime: Double? = null,
    @SerializedName("merchantName")
    var merchantName: String? = "",
    @SerializedName("number")
    var number: String? = "",
    @SerializedName("counter")
    var counter: String? = "",
    @SerializedName("prefix")
    var prefix: String? = "",
    @SerializedName("processedByUserId")
    var processedByUserId: String? = "",
    @SerializedName("processedByUserName")
    var processedByUserName: String? = "",
    @SerializedName("processingTime")
    var processingTime: Double? = null,
    @SerializedName("queueId")
    var queueId: String? = "",
    @SerializedName("serviceId")
    var serviceId: String? = "",
    @SerializedName("serviceName")
    var serviceName: String? = "",

    @SerializedName("appointmentEndTime")
var appointmentEndTime: String? = "",
    @SerializedName("appointmentPrefix")
    var appointmentPrefix: String? = "",
    @SerializedName("startedAt")
    var startedAt: Long? = null,
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("updatedAt")
    var updatedAt: Long? = null,
    @SerializedName("userId")
    var userId: String? = "",
    @SerializedName("userName")
    var userName: String? = "",
    @SerializedName("ticketType")
    var ticketType: String? = "",
    @SerializedName("appointmentTime")
    var appointmentTime: String? = "",
    @SerializedName("date")
    var appointmentDate: String? = "",
    @SerializedName("waitingTime")
    var waitingTime: Int? = null,
    @SerializedName("customersCount")
    var customersCount: Int = 0,
    @SerializedName("branchOpenTime")
    var branchOpenTime: String = "",

    @SerializedName("noShow")
    var noShow: Boolean = false,
    @SerializedName("noShowTime")
    var noShowTime: String = "",
    @SerializedName("noShowExpireTime")
    var noShowExpireTime: String = "",




    @SerializedName("categoryID")
    var categoryID: String? = null,
    @SerializedName("categoryName")
    var categoryName: String? = null,
    @SerializedName("branchStatus")
    var branchStatus: String? = null,
    @SerializedName("maxRebookTime")
    var maxRebookTime: Int? = Constant.REBOOK_DEFAULT_TIME,
    var type: Int = 0,


    @SerializedName("starttime")
    var starttime: String?=null,
    @SerializedName("endtime")
    val endTime: String?=null,
    @SerializedName("allowAction")
    val allowAction: String? = "",
    @SerializedName("eventLogo")
    val eventLogo: String? = "",


    @SerializedName("checkIn")
    val checkIn: Boolean,
    @SerializedName("checkInTime")
    val checkInTime: Int,
    @SerializedName("conciergeId")
    val conciergeId: String,
    @SerializedName("conciergeName")
    val conciergeName: String,

    @SerializedName("deviceID")
    val deviceID: Any,
    @SerializedName("distanceFromBranch")
    val distanceFromBranch: Any,
    @SerializedName("eventId")
    val eventId: String,
    @SerializedName("eventName")
    var eventName: String,

    @SerializedName("isConcierge")
    val isConcierge: Boolean,


    @SerializedName("isRecall")
    val isRecall: Boolean,
    @SerializedName("isServiceAvailable")
    val isServiceAvailable: Boolean,
    @SerializedName("isTransfer")
    val isTransfer: Boolean,
    @SerializedName("isWeb")
    val isWeb: Boolean,
    @SerializedName("isoCode")
    val isoCode: String,
    @SerializedName("lang")
    val lang: String,
    @SerializedName("lat")
    val lat: Any,
    @SerializedName("location")
    var location: String,
    @SerializedName("locationArabic")
    var locationArabic: String,
    @SerializedName("long")
    val long: Any,


    @SerializedName("noShowAt")
    val noShowAt:Long? = null,

    @SerializedName("num")
    val num: Int,

    @SerializedName("phone")
    val phone: String,
    @SerializedName("queueTime")
    val queueTime: Int,
    @SerializedName("rebookOf")
    val rebookOf: String,
    @SerializedName("rebookedAt")
    val rebookedAt: Double,
    @SerializedName("routeName")
    val routeName: String,
    @SerializedName("segment")
    val segment: String,
    @SerializedName("servedAt")
    val servedAt: Double,

    @SerializedName("serviceScheduleId")
    val serviceScheduleId: String,
    @SerializedName("shift")
    val shift: Any,


    @SerializedName("timeZone")
    val timeZone: String,

    @SerializedName("textUnderTimeFinish")
    val textUnderTimeFinish: String? = null,
    @SerializedName("arabicTextUnderTimeFinish")
    val arabicTextUnderTimeFinish: String? = null,
    @SerializedName("textUnderTime")
    val textUnderTime: String?=null,
    @SerializedName("arabicTextUnderTime")
    val arabicTextUnderTime: String?=null,
    @SerializedName("agentCount")
    val agentCount: Int=0,



) : Serializable