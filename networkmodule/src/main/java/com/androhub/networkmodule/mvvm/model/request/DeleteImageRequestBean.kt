package com.androhub.networkmodule.mvvm.model.request


import com.google.gson.annotations.SerializedName

 class DeleteImageRequestBean {
    @SerializedName("link")
    var link: List<String>?=null
}