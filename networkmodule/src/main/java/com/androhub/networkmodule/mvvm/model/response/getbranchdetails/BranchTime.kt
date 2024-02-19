package com.androhub.networkmodule.mvvm.model.response.getbranchdetails

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class BranchTime : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("day")
    @Expose
    var day: String? = null

    @SerializedName("startTime")
    @Expose
    var startTime: String? = null

    @SerializedName("endTime")
    @Expose
    var endTime: String? = null

    @SerializedName("isWorking")
    @Expose
    var isWorking: Boolean = false

    var sortOrderTemp = 0

    companion object {
        private const val serialVersionUID = -6900422161393146129L
    }
}