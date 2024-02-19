package com.androhub.networkmodule.mvvm.model.response


import com.google.gson.annotations.SerializedName
import java.io.Serializable

 class DistanceResponseBean : Serializable {
     @SerializedName("data")
     var data: DistanceData?=null
     @SerializedName("message")
     var message: String?=""
     @SerializedName("status")
     var status: Int?=0
}
    class DistanceData: Serializable {
        @SerializedName("branchId")
        var branchId: String?=""
        @SerializedName("distance")
        var distance: String?=""

}