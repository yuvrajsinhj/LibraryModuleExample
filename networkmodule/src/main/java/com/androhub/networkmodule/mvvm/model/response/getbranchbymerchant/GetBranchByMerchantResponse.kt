package com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable


 class GetBranchByMerchantResponse : Serializable {



    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    companion object {
        private const val serialVersionUID = -1865838600953901538L
    }
}