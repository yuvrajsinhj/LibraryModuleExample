package com.androhub.networkmodule.mvvm.model.response.getbranchdetails

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Data : Serializable {

    @SerializedName("isOpenBranch")
    @Expose
    var isOpenBranch: Boolean? = false

    @SerializedName("showTextStatus")
    @Expose
    val showTextStatus: String? = null

    @SerializedName("colorCode")
    @Expose
    val colorCode: String? = null

    @SerializedName("inBreak")
    @Expose
    val inBreak: Boolean? = false

    @SerializedName("inGraceTime")
    @Expose
    val inGraceTime: Boolean? = false

    @SerializedName("workTime")
    @Expose
    val workTime: List<WorkTime>? = null

    @SerializedName("lat")
    @Expose
    var lat: Double? = null

    @SerializedName("long")
    @Expose
    var long: Double? = null

    @SerializedName("todayTime")
    @Expose
    val todayTime: TodayTime? = null

    @SerializedName("merchantID")
    @Expose
    var merchantID: String? = null

    @SerializedName("branchName")
    @Expose
    var branchName: String? = null

    @SerializedName("merchantName")
    @Expose
    var merchantName: String? = null

    @SerializedName("logoURL")
    @Expose
    var logoURL: String? = null

    @SerializedName("displayImage")
    @Expose
    var displayImage: String? = null

    @SerializedName("segment")
    @Expose
    var segment: String? = null

    @SerializedName("segmentation")
    @Expose
    var segmentationAllow: Boolean? = false

    @SerializedName("merchantActive")
    @Expose
    var merchantActive: Boolean? = false

    @SerializedName("branchActive")
    @Expose
    var branchActive: Boolean? = false

    @SerializedName("categoryID")
    @Expose
    var categoryID: String? = null

    @SerializedName("categoryName")
    @Expose
    var categoryName: String? = null

    @SerializedName("timeZone")
    @Expose
    var timeZone: String? = null

    @SerializedName("branchTime")
    @Expose
    var branchTime: ArrayList<BranchTime>? = ArrayList()

    @SerializedName("distance")
    @Expose
    var distance: Double? = 0.0

    @SerializedName("parentService")
    @Expose
    val parentService: List<ParentService>? = null

    class WorkTime : Serializable {

        @SerializedName("day")
        val day: String = ""

        @SerializedName("isWorking")
        val isWorking: Boolean = false

        @SerializedName("shifts")
        val shifts: List<Shift>? = null

    }

    class Shift : Serializable {
        @SerializedName("end")
        val end: String = ""

        @SerializedName("start")
        val start: String = ""
    }

    class TodayTime : Serializable {
        @SerializedName("day")
        val day: String = ""

        @SerializedName("shifts")
        val shifts: List<Shift>? = null
    }

    class ParentService : Serializable {
        @SerializedName("appointmentPrefix")
        val appointmentPrefix: String = ""

        @SerializedName("appointmentTime")
        val appointmentTime: Int = 0

        @SerializedName("arName")
        val arName: String = ""

        @SerializedName("form")
        var form: Service.Form? = null

        @SerializedName("branchID")
        val branchID: String = ""

        @SerializedName("child")
        val child: List<Child>? = null

        @SerializedName("createdAt")
        val createdAt: Long = 0

        @SerializedName("createdBy")
        val createdBy: String = ""

        @SerializedName("deletedAt")
        val deletedAt: Int = 0

        @SerializedName("description")
        val description: String = ""

        @SerializedName("englishName")
        val englishName: String = ""

        @SerializedName("id")
        val id: String = ""

        @SerializedName("isActive")
        val isActive: Boolean = false

        @SerializedName("isDeleted")
        val isDeleted: Boolean = false

        @SerializedName("lang")
        val lang: String = ""

        @SerializedName("maxRebookTime")
        val maxRebookTime: Double = 0.0

        @SerializedName("merchantId")
        val merchantId: String = ""

        @SerializedName("name")
        val name: String = ""



        @SerializedName("parentCategoryId")
        val parentCategoryId: String = ""

        @SerializedName("prefix")
        val prefix: String = ""


        @SerializedName("servingTime")
        val servingTime: Double = 0.0

        @SerializedName("updatedAt")
        val updatedAt: Long = 0

        @SerializedName("updatedBy")
        val updatedBy: String = ""
        val type: String = "Parent"
    }

    class Child : Serializable {
        @SerializedName("appointmentPrefix")
        val appointmentPrefix: String = ""

        @SerializedName("appointmentTime")
        val appointmentTime: Int = 0

        @SerializedName("branchID")
        val branchID: String = ""

        @SerializedName("child")
        val child2: List<Child1>? = null

        @SerializedName("description")
        val description: String = ""

        @SerializedName("englishName")
        val englishName: String = ""

        @SerializedName("id")
        val id: String = ""

        @SerializedName("lang")
        val lang: String = ""

        @SerializedName("maxRebookTime")
        val maxRebookTime: Double = 0.0

        @SerializedName("name")
        val name: String = ""

        @SerializedName("parentCategoryId")
        val parentCategoryId: String = ""

        @SerializedName("prefix")
        val prefix: String = ""

        @SerializedName("form")
        var form: Service.Form? = null


        @SerializedName("servingTime")
        val servingTime: Double = 0.0
        val type: String = "Child"
    }

    class Child1 : Serializable {
        @SerializedName("appointmentPrefix")
        val appointmentPrefix: String = ""

        @SerializedName("appointmentTime")
        val appointmentTime: Int = 0

        @SerializedName("branchID")
        val branchID: String = ""


        @SerializedName("description")
        val description: String = ""

        @SerializedName("englishName")
        val englishName: String = ""

        @SerializedName("id")
        val id: String = ""

        @SerializedName("lang")
        val lang: String = ""

        @SerializedName("maxRebookTime")
        val maxRebookTime: Double = 0.0

        @SerializedName("name")
        val name: String = ""

        @SerializedName("parentCategoryId")
        val parentCategoryId: String = ""

        @SerializedName("form")
        var form: Service.Form? = null

        @SerializedName("prefix")
        val prefix: String = ""



        @SerializedName("servingTime")
        val servingTime: Double = 0.0
        val type: String = "Child1"
    }

    // var distanceStr: String = "10.00 KM"
    var distanceStr: String = ""
}