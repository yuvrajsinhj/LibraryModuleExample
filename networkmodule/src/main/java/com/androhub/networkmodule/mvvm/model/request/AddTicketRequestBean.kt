package com.androhub.networkmodule.mvvm.model.request


import com.androhub.networkmodule.MyApplication
import com.androhub.networkmodule.PrefConst
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.ServiceForm
import com.google.gson.annotations.SerializedName

class AddTicketRequestBean {
    //var userId: String? = ""
    var customerEmail: String? = ""
    var appointmentTime: String? = ""
    var date: String? = ""
    var appointmentPrefix: String? = ""
    var prefix: String? = ""
    var branchId: String? = ""
    var branchName: String? = ""
    var serviceId: String? = ""
    var serviceName: String? = ""

    var userName: String? = ""
    var customerId: String? = ""
    var customerName: String? = ""
    var phone: String? = ""
    var isoCode: String? =
        MyApplication.getAppManager().prefManager.getString(PrefConst.PREF_ISO_CODE)

    var merchantId: String? = ""
    var branchOpenTime: String? = ""
    var merchantName: String? = ""

    var deviceID: String? = ""
    var distanceFromBranch: String? = ""
    var merchantLogo: String? = ""
    var segment: String? = ""
    var lat: String? = "0.0"
    var long: String? = "0.0"
    var maxRebookTime: Double? = null
    var approxTime: Double? = null
    var queueTime: Double? = null
    var branchStatus: String? = null

    var timeZone: String? = null
    var shift: List<Data.Shift>? = null
    var formId: String? = ""

    @SerializedName("formDetail")
    var formDetail: ServiceForm.FormId? = null
//    var formDetail: qqqqqqq.FormDetail?=null
//    var formDetail: String? = null


}