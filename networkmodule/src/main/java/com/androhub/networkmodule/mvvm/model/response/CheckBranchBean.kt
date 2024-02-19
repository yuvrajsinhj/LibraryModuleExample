package com.androhub.networkmodule.mvvm.model.response


import com.google.gson.annotations.SerializedName

data class CheckBranchBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
) {
    data class Data(
        @SerializedName("inBreak")
        val inBreak: Boolean,
        @SerializedName("inGraceTime")
        val inGraceTime: Boolean,
        @SerializedName("isOpenBranch")
        val isOpenBranch: Boolean
    )
}