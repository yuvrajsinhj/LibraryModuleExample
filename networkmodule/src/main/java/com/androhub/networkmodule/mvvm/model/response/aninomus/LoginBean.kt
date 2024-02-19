package com.androhub.networkmodule.mvvm.model.response.aninomus


import com.google.gson.annotations.SerializedName

class LoginBean{
    @SerializedName("createdAt")
    var createdAt: Long=0
    @SerializedName("devices")
    var devices: List<Any>?=null
    @SerializedName("firstName")
    var firstName: String? = null
    @SerializedName("id")
    var id: String? = null
    @SerializedName("isDeleted")
    var isDeleted: Boolean=false
    @SerializedName("isoCode")
    var isoCode: String? = null
    @SerializedName("lastName")
    var lastName: String? = "B"
    @SerializedName("name")
    var name: String? = "B"
    @SerializedName("phone")
    var phone: String? = null
    @SerializedName("token")
    var token: String? = null
    @SerializedName("updatedAt")
    var updatedAt: Long=0
}
