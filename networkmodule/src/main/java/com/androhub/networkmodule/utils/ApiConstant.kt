package com.androhub.networkmodule.utils

object ApiConstant {

    const val BASE_URL = "https://api.balador.io/"
    const val actionUserFolder="user"
    const val actionQueueFolder="queue"


    const val GET_ANINYMOUS_LOGIN = "$actionUserFolder/aninymous/login"
    const val GET_GENERATE_TOKEN_SDK = "$actionUserFolder/generate-token-sdk"

    const val GET_BRANCH_BY_MERCHANT = "$actionUserFolder/getBranchByMerchant"
    const val GET_BRANCH_DISTANCE =  "$actionUserFolder/v3/branch/distance"

    const val GET_SEGMENT= "segment/segment"
    //ticket
    const val GET_FASTER_BRANCH = "$actionQueueFolder/nearBranch"

    const val GET_BRANCH_DETAIL_BY_BRANCHID =  "$actionUserFolder/v4/getByBranchID"
    const val GET_TICKET_COUNT = "$actionQueueFolder/ticket/count"

    const val CHECK_BRANCH_ISOPEN =  "$actionUserFolder/v2/checkBranch"
    //ticket
    const val ADD_TICKET = "$actionQueueFolder/ticket"
    const val GET_TICKET_BY_CUST = "$actionQueueFolder/ticket"    //<<---------gettivcket
    const val NO_SHOW_CANCEL_TICKET_PROCESS = "$actionQueueFolder/ticket/cancel"

    const val UPLOAD_IMAGE = "$actionUserFolder/customer/upload"
    const val DELETE_IMAGE = "$actionUserFolder/customer/delete"


}