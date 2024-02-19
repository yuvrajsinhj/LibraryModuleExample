package com.androhub.networkmodule.mvvm.model.response.getbranchdetails

import com.androhub.networkmodule.utils.frombuilder.dynamicFormBuilder.FormComponentItemTemp
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ServiceForm : Serializable {
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
        var formDetails: List<FormComponentItemTemp>? = null

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

//    companion object {
//        private const val serialVersionUID = -5810981465204570628L
//    }


}