package com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {

    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: Double? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: Double? = null


    @SerializedName("preventCustomerBookingOption")
    @Expose
    var preventCustomerBookingOption: String? = null


    @SerializedName("id")
    @Expose
    var id: String = ""

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("isActive")
    @Expose
    var isActive: Boolean = false

    @SerializedName("logoURL")
    @Expose
    var logoURL: String? = null

    @SerializedName("licenseCode")
    @Expose
    var licenseCode: String? = null

    @SerializedName("expiryDate")
    @Expose
    var expiryDate: Double? = null

//    @SerializedName("licenseCount")
//    @Expose
//    var licenseCount: Int? = null

    @SerializedName("licenseValidation")
    @Expose
    var licenseValidation: String? = null

    @SerializedName("createdBy")
    @Expose
    var createdBy: String? = null

    @SerializedName("updatedBy")
    @Expose
    var updatedBy: String? = null

    @SerializedName("deletedAt")
    @Expose
    var deletedAt: Double? = null

    @SerializedName("isDeleted")
    @Expose
    var isDeleted: Boolean? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("displayImage")
    @Expose
    var displayImage: String? = null

    @SerializedName("userID")
    @Expose
    var userID: String? = null

    @SerializedName("categoryID")
    @Expose
    var categoryID: String? = null

    @SerializedName("categoryName")
    @Expose
    var categoryName: String? = null

    @SerializedName("eserviceLogo")
    @Expose
    var eserviceLogo: String? = null

    @SerializedName("isEservice")
    @Expose
    var isEservice: Boolean? = false

    @SerializedName("branch")
    @Expose
    var branch: ArrayList<Branch>? = ArrayList()

    companion object {
        private const val serialVersionUID = -932813819941562303L
    }
}