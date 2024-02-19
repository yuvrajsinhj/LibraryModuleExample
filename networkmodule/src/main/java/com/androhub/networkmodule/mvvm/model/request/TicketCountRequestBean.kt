package com.androhub.networkmodule.mvvm.model.request

import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data


class TicketCountRequestBean {

    var ticketId : String? = ""
    var timeZone : String? = ""
    var branchId : String? = ""
    var serviceId : String? = ""
    var shift : List<Data.Shift>? = null
}