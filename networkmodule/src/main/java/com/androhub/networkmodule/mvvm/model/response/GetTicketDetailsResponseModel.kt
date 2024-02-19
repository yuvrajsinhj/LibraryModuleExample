package com.androhub.networkmodule.mvvm.model.response


import com.google.gson.annotations.SerializedName

class GetTicketDetailsResponseModel(
    @SerializedName("data")
    var data: Data? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("status")
    var status: Int? = null
) {
    class Data(

        @SerializedName("ticket")
        var ticket: TicketBean? = null
    )
}