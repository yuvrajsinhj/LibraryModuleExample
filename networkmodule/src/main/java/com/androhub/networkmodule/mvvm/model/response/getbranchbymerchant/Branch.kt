package com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable
@Entity(tableName = "branch_data_table")
 class Branch : Serializable {
/*

    @PrimaryKey(autoGenerate = true)
    var primaryId: Int=0
*/

    @SerializedName("id")
    @Expose
    @PrimaryKey
    var id: String = ""

    @SerializedName("merchantID")
    @Expose
    var merchantId: String? = ""

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("merchantName")
    @Expose
    var merchantName: String? = null

    @SerializedName("logoURL")
    @Expose
    var logoURL: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("lat")
    @Expose
    var lat: Double? = 0.0

    @SerializedName("long")
    @Expose
    var long: Double? = 0.0

    @SerializedName("timeZone")
    @Expose
    var timeZone: String? = null

    @SerializedName("deskCount")
    @Expose
    var deskCount: Int? = null

    @SerializedName("licenseCount")
    @Expose
    var licenseCount: Double? = null

    @SerializedName("isActive")
    @Expose
    var isActive: Boolean=false

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("distance")
    @Expose
    var distance: Double? = 0.00

   var distanceValue: Long =  0
    var tempPosition: Int = 0

    var durationText: String? = ""
    var durationValue: Double = 0.0
    var durationValueInMinutes: Double = 0.0
    var fasterBranchWaitTime: Double =-1.0
    var totalTimeInMinutes: Double = -1.0


   // var distanceStr: String = "10.00 KM"
    var distanceStr: String = ""
    var errorAsynk: String = ""
   var isNearBy: Boolean=false

    @SerializedName("preventCustomerBookingOption")
    @Expose
    var preventCustomerBookingOption: String? = null


}