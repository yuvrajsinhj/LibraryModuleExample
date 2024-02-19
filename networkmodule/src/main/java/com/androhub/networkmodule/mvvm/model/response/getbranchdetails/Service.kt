package com.androhub.networkmodule.mvvm.model.response.getbranchdetails

import com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant.Branch
import com.androhub.networkmodule.utils.Constant
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Service : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("appointmentTime")
    @Expose
    var appointmentTime: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("englishName")
    @Expose
    var englishServiceName: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("servingTime")
    @Expose
    var servingTime: Double? = null

    @SerializedName("branchID")
    @Expose
    var branchID: String? = null

    @SerializedName("maxRebookTime")
    @Expose
    var maxRebookTime: Double? = Constant.REBOOK_DEFAULT_TIME_DOUBLE

    @SerializedName("lang")
    @Expose
    var lang: String? = null

    @SerializedName("child")
    @Expose
    val child: List<Any>? = null

    @SerializedName("prefix")
    @Expose
    var prefix: String? = null

    @SerializedName("appointmentPrefix")
    @Expose
    var appointmentPrefix: String? = null

    @SerializedName("form")
    @Expose
    var form: Form? = null

    var logoURL: String = ""

    var nearByFastBranch: Branch? = null
    var currentBranchPeopleCount: Int? = -1
    var currentBranchWaitTime: Double? = -1.0
    var currentBranchDistance: Double? = -1.0


    var oldBranchID: String = ""
    var oldBranchName: String = ""

    var oldBranchIDTemp: String = ""
    var oldBranchNameTemp: String = ""

    var branchName: String = ""
    var merchantName: String = ""
    var merchantID: String = ""
    var isOpenBranch: Boolean = false
    var isFasterSelected: Boolean = false

    class Form : Serializable {
        @SerializedName("createdAt")
        var createdAt: Long = 0

        @SerializedName("deletedAt")
        var deletedAt: Int = 0

        @SerializedName("formId")
        var formId: FormId? = null

        @SerializedName("id")
        var id: String? = ""

        @SerializedName("isDeleted")
        var isDeleted: Boolean = false

        @SerializedName("serviceID")
        var serviceID: String? = ""

        @SerializedName("updatedAt")
        var updatedAt: Long = 0
    }

    class FormId : Serializable {
        @SerializedName("arabicDescription")
        var arabicDescription: String? = ""

        @SerializedName("arabicTitle")
        var arabicTitle: String? = ""

        @SerializedName("createdAt")
        var createdAt: Long = 0

        @SerializedName("createdBy")
        var createdBy: String? = ""

        @SerializedName("description")
        var description: String? = ""

        @SerializedName("formDetails")
        var formDetails: List<String>? = null

        @SerializedName("id")
        var id: String? = ""

        @SerializedName("isDeleted")
        var isDeleted: Boolean = false

        @SerializedName("merchantID")
        var merchantID: String? = ""

        @SerializedName("title")
        var title: String? = ""

        @SerializedName("updatedAt")
        var updatedAt: Long = 0

        @SerializedName("updatedBy")
        var updatedBy: String? = ""
    }

    companion object {
        private const val serialVersionUID = -5810981465204570628L
    }


}