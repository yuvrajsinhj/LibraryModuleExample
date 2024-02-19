package com.androhub.networkmodule.utils

import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data


object Constant {

    lateinit var TEMP_SHIFT: List<Data.Shift>

    const val ACTION_APPOINTMENT = "appointment"

    const val APP_NAME = "Dooree"
    const val BROADCAST_LOGS = "BROADCAST_LOGS"
    const val API_NAME = "API_NAME"
    const val SERVICE_NAME = "SERVICE_NAME"
    const val ERROR_DETAIL = "ERROR_DETAIL"
    const val IS_TOKEN_EXPIRED = "IS_TOKEN_EXPIRED"
    const val LOGOUT_FORCEFULLY = "LOGOUT_FORCEFULLY"

    const val TEMP_ID = "d5fa8d5bb02e2e8315"
    const val RESULT_CODE_RESET_PWD = 600
    const val INTENT_100 = 100
    const val INTENT_200 = 200
    const val INTENT_300 = 300
    const val INTENT_400 = 400
    const val INTENT_500 = 500
    const val STATIC_LAT = "23.647946"
    const val STATIC_LNG = "72.8980809"

    const val PLATFORM = "Android"

    var GPS_REQUEST = 111


    const val DEFAULT_COUNTRY = "JO"


    var DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    var REFRESH_TIME: Long = 10000//10 sec
    var REFRESH_TIME_HOME: Long = 5000//5 sec
    var PER_PAGE_LIMIT = 25
    var SERVER_DATE_FORMAT = DATE_FORMAT
    const val DISPLAY_DATE_FORMAT_AR = "yyyy/MM/dd"
    const val DISPLAY_DATE_FORMAT = "dd/MM/yyyy"
    const val DISPLAY_DATE_FORMAT_EVENT = "MMMM dd, yyyy"
    const val DISPLAY_DATE_FORMAT_TICKET = "EEEE, MMMM dd"//Monday, October 26th
    const val DISPLAY_DATE_FORMAT_TWO = "dd MMM yyyy"
    const val APPOINTMENT_DATE_FORMAT = "dd-MM-yyyy"
    const val APPOINTMENT_DATE_FORMAT2 = "dd/MM/yyyy"
    var CHAT_TIME_FORMAT = "hh:mm a"
    var SERVER_TIME_FORMAT = "HH:mm"
    var DISPLAY_TIME_FORMAT = "hh:mm a"
    var DISPLAY_TIME_FORMAT_ESERVICE = "hh:mm a, MMMM dd"
    var DISPLAY_TIME_FORMAT_ESERVICE_TWO = "MMM dd"

    const val CANCEL_TICKET_STATUS = "cancelled"

    //distance Interval
    const val REBOOK_DEFAULT_TIME_DOUBLE: Double = 0.0//rebook default time
    const val REBOOK_DEFAULT_TIME: Int = 0//rebook default time
    const val DISTANCE_INTERVAL_DEFAULT: Int = 500//meter
    const val DISTANCE_INTERVAL_SHORT: Int = 200//meter



    interface SEGMENT {
        companion object {
            const val CUSTOMER = "customer"
            const val SERVICE = "service"

        }
    }

    interface PREVENT_CUSTOMER_BOOKING {
        companion object {
            const val PREVENT = "prevent"
            const val WARNING = "warning"

        }
    }


    interface TICKET_STATUS {
        companion object {
            const val STATUS_REBOOK = "re-book"
            const val STATUS_PROCESSED = "processed"
            const val STATUS_NO_SHOW = "no-show"
            const val STATUS_CANCELLED = "cancelled"
            const val STATUS_COMPLETED = "completed"
            const val STATUS_NOT_PROCESSED = "Not processed"
            const val STATUS_ON_HOLD = "onhold"
            const val REVIEWED = "reviewed"
            const val STATUS_NO_SHOW_APPOINTMENT_TEMP = "No show"
        }
    }





    interface STATUS {
        companion object {
            const val SUCCESS = 200
            const val FAIL = 403
            const val EXPIRED = 402
            const val TOKEN_EXPIRE = 403
            const val GATEWAY_TIME_OUT = 504
            const val USER_NOT_FOUND = 404
            const val SERVER_ERROR = 500
        }
    }

    interface ERROR_CODE {
        companion object {
            const val MAP_ERROR = "MAP_ERROR"
            const val USER_NOT_CONFIRM = "UserNotConfirmedException"
            const val TOKEN_EXPIRE = "NotAuthorizedException"
            const val INVALID_PHONE = "auth/invalid-phone-number"
            const val INVALID_PHONE_TOO_LONG = "TOO_LONG"
            const val INVALID_PHONE_TOO_SHORT = "TOO_SHORT"


        }
    }


    interface INTENT_EXTRA {
        companion object {

            const val USER_MERCHANT_ID: String = "USER_MERCHANT_ID"
            const val USER_SECRET_KEY: String = "USER_SECRET_KEY"
            const val USER_PHONE_KEY: String = "USER_PHONE_KEY"
            const val USER_ISO_KEY: String = "USER_ISO_KEY"

            const val MAIN_RESPONSE: String="MAIN_RESPONSE"
            const val MESSAGE_RESPONSE: String="MESSAGE_RESPONSE"
            const val DURATION: String="DURATION"

            const val IN_BREAK = "IN_BREAK"
            const val IN_GRACETIME = "IN_GRACETIME"

            const val IMAGE_PATH = "IMAGE_PATH"
            const val IMAGE_RES = "IMAGE_RES"

            const val MERCHANT_ID = "merchantId"

            const val SELECTED_BRANCH_DISTANCE = "SELECTED_BRANCH_DISTANCE"
            const val SELECTED_BRANCH_MINUTES = "SELECTED_BRANCH_MINUTES"

            const val BRANCH_ID = "branchId"
            const val PREVENT_CUSTOMER_BOOKING = "preventCustomerBookingOption"
            const val FROM_WHERE = "FROM_WHERE"
            const val FROM_NEAR_BY = "FROM_NEAR_BY"
            const val WAITING_TIME = "WAITING_TIME"
            const val BREAK_TIME = "BREAK_TIME"
            const val BRANCH_BEAN = "BRANCH_BEAN"
            const val BANK_TIME_ARRAY = "BANK_TIME_ARRAY"
            const val FORM_FILLED_DETAILS_FORM_ID = "FORM_FILLED_DETAILS_FORM_ID"
            const val SERVICE_BEAN = "SERVICE_BEAN"
            const val APPOINTMENT_BEAN = "APPOINTMENT_BEAN"
            const val TIME_BEAN = "TIME_BEAN"
            const val TICKET_BEAN = "TICKET_BEAN"
            const val TICKET_ADDED = "TICKET_ADDED"
            const val DO_NOT_REFRESH = "DO_NOT_REFRESH"
            const val NEAR_BY_BRANCH_LIST = "NEAR_BY_BRANCH_LIST"


        }
    }

    interface LANGUAGE {
        companion object {
            const val ENGLISH = "en"
            const val AREBIC = "ar"

        }
    }

    interface TICKET_TYPE {
        companion object {
            const val TI_APPOINTMENT = "appointment"
            const val ESERVICE = "eservice"
            const val DONATION = "donation"
        }
    }




}