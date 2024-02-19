package com.mylib.Repository

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.androhub.networkmodule.R
import com.androhub.networkmodule.libs.RetrofitApiService
import com.androhub.networkmodule.mvvm.model.request.AddTicketRequestBean
import com.androhub.networkmodule.mvvm.model.request.CancelTicketRequestBean
import com.androhub.networkmodule.mvvm.model.request.FasterNearByDataBean
import com.androhub.networkmodule.mvvm.model.request.TicketCountRequestBean
import com.androhub.networkmodule.mvvm.model.response.AddTicketCountResponseBean
import com.androhub.networkmodule.mvvm.model.response.AddTicketResponseBean
import com.androhub.networkmodule.mvvm.model.response.GeneralResponseBean
import com.androhub.networkmodule.mvvm.model.response.GetFasterBranchListResponseBean
import com.androhub.networkmodule.mvvm.model.response.GetTicketDetailsResponseModel
import com.androhub.networkmodule.mvvm.model.response.GetTicketHistoryResponseModel
import com.androhub.networkmodule.utils.ApiConstant
import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.utils.Utils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response

class QueueRepository {
    val gson = GsonBuilder().create()

    companion object {
        fun getInstance(): QueueRepository {
            return QueueRepository()
        }
    }

    var mutableLiveDataGeneralResponseBean = MutableLiveData<GeneralResponseBean>()

    var mutableLiveDataGetFasterBranchListResponseBean =
        MutableLiveData<GetFasterBranchListResponseBean>()
    var mutableLiveDataAddTicketCountResponseBean = MutableLiveData<AddTicketCountResponseBean>()


    //get faster branches
    fun requestGetFasterBranchListApiCall(context: Context, request: FasterNearByDataBean) {
        var methodName = "requestGetFasterBranchListApiCall"
        var apiEndPoint = ApiConstant.GET_FASTER_BRANCH
        var call = RetrofitApiService.apiInterface.requestGetFasterBranchListApiCall(request)
        call.enqueue(object : retrofit2.Callback<GetFasterBranchListResponseBean> {
            override fun onFailure(call: Call<GetFasterBranchListResponseBean>, t: Throwable) {

                //
                Utils.print("onFailure=====" + t.localizedMessage)
                var msg =
                    if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                        R.string.timeout_error_msg_two) else if (!TextUtils.isEmpty(t.localizedMessage)) t.localizedMessage else context.resources.getString(
                        R.string.internal_server)
                sendBroadcastForLogs(context,
                    apiEndPoint, "$methodName", msg)
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model = GetFasterBranchListResponseBean()
                    model.message = msg
                    mutableLiveDataGetFasterBranchListResponseBean.value = model
                } catch (e: Exception) {
                    var model = GetFasterBranchListResponseBean()
                    model.message = e.message
                    mutableLiveDataGetFasterBranchListResponseBean.value = model
                }
            }

            override fun onResponse(
                call: Call<GetFasterBranchListResponseBean>,
                response: Response<GetFasterBranchListResponseBean>,
            ) {
                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body() != null && null == response.body()!!.status)
                ) {
                    mutableLiveDataGetFasterBranchListResponseBean.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        GetFasterBranchListResponseBean::class.java
                    )
                    sendBroadcastForLogs(context,
                        apiEndPoint,
                        "FAILURE $methodName",
                        "Response not proper=${response.body().toString()}")
                    return
                }
                val strResponse =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper"
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataGetFasterBranchListResponseBean.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(context,
                            apiEndPoint, "SUCCESS $methodName", e.toString())
                    }
                    return
                } else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataGetFasterBranchListResponseBean.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            GetFasterBranchListResponseBean::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error = msg + "--" + msg2
                        mutableLiveDataGetFasterBranchListResponseBean.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            GetFasterBranchListResponseBean::class.java
                        )
                    }

                    sendBroadcastForLogs(
                        context,
                        apiEndPoint,
                        if (response.code() == Constant.STATUS.FAIL) "FAIL $methodName" else methodName,
                        error + "=" + strResponse
                    )

                }


            }
        })
    }

    fun responseGetFasterBranchListApiCall(): MutableLiveData<GetFasterBranchListResponseBean> {
        mutableLiveDataGetFasterBranchListResponseBean = MutableLiveData()
        return mutableLiveDataGetFasterBranchListResponseBean
    }


    fun requestGetTicketCountByBranchIdApiCall(
        context: Context,
        merchantListRequestBean: TicketCountRequestBean,
    ) {
        var methodName = "requestGetTicketCountByBranchIdApiCall"
        var apiEndPoint = ApiConstant.GET_TICKET_COUNT
        var call = RetrofitApiService.apiInterface.requestGetTicketCountByBranchIdApiCall(
            merchantListRequestBean
        )
        call.enqueue(object : retrofit2.Callback<AddTicketCountResponseBean> {
            override fun onFailure(call: Call<AddTicketCountResponseBean>, t: Throwable) {

                //
                Utils.print("onFailure=====" + t.localizedMessage)
                var msg =
                    if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                        R.string.timeout_error_msg_two) else if (!TextUtils.isEmpty(t.localizedMessage)) t.localizedMessage else context.resources.getString(
                        R.string.internal_server)
                sendBroadcastForLogs(context,
                    apiEndPoint, "$methodName", msg)
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model = AddTicketCountResponseBean()
                    model.message = msg
                    mutableLiveDataAddTicketCountResponseBean.value = model
                } catch (e: Exception) {
                    var model = AddTicketCountResponseBean()
                    model.message = e.message
                    mutableLiveDataAddTicketCountResponseBean.value = model
                }
            }

            override fun onResponse(
                call: Call<AddTicketCountResponseBean>,
                response: Response<AddTicketCountResponseBean>,
            ) {

                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body() != null && null == response.body()!!.status)
                ) {
                    mutableLiveDataAddTicketCountResponseBean.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        AddTicketCountResponseBean::class.java
                    )
                    sendBroadcastForLogs(context,
                        apiEndPoint,
                        "FAILURE $methodName",
                        "Response not proper=${response.body().toString()}")
                    return
                }

                val strResponse =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper"
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataAddTicketCountResponseBean.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(context,
                            apiEndPoint, "SUCCESS $methodName", e.toString())
                    }
                    return
                } else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataAddTicketCountResponseBean.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            AddTicketCountResponseBean::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error = msg + "--" + msg2
                        mutableLiveDataAddTicketCountResponseBean.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            AddTicketCountResponseBean::class.java
                        )
                    }

                    sendBroadcastForLogs(
                        context,
                        apiEndPoint,
                        if (response.code() == Constant.STATUS.FAIL) "FAIL $methodName" else methodName,
                        error + "=" + strResponse
                    )

                }

            }
        })
    }

    //
    fun responseGetTicketCountByBranchIdApiCall(): MutableLiveData<AddTicketCountResponseBean> {
        mutableLiveDataAddTicketCountResponseBean = MutableLiveData()
        return mutableLiveDataAddTicketCountResponseBean
    }
    //    // add ticket

    var mutableLiveDataAddTicketResponseBean = MutableLiveData<AddTicketResponseBean>()

    fun requestAddTicketApiCall(context: Context, request: AddTicketRequestBean) {
        var methodName= "requestAddTicketApiCall"
        var apiEndPoint= ApiConstant.ADD_TICKET
        var call = RetrofitApiService.apiInterface.requestAddTicketApiCall(request)
        call.enqueue(object : retrofit2.Callback<AddTicketResponseBean> {
            override fun onFailure(call: Call<AddTicketResponseBean>, t: Throwable) {

                //
                Utils.print("onFailure=====" + t.localizedMessage)
                var msg=if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                    R.string.timeout_error_msg_two) else if (!TextUtils.isEmpty(t.localizedMessage))t.localizedMessage else context.resources.getString(
                    R.string.internal_server)
                sendBroadcastForLogs(context,
                    apiEndPoint,"$methodName", msg)
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model= AddTicketResponseBean()
                    model.message=msg
                    mutableLiveDataAddTicketResponseBean.value = model
                }
                catch (e: Exception) {
                    var model= AddTicketResponseBean()
                    model.message=e.message
                    mutableLiveDataAddTicketResponseBean.value = model
                }
            }

            override fun onResponse(
                call: Call<AddTicketResponseBean>,
                response: Response<AddTicketResponseBean>
            ) {

                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body()!=null && null==response.body()!!.status)
                ) {
                    mutableLiveDataAddTicketResponseBean.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        AddTicketResponseBean::class.java
                    )
                    sendBroadcastForLogs(context,
                        apiEndPoint,"FAILURE $methodName","Response not proper=${response.body().toString()}")
                    return
                }

                val strResponse = if (response.body()!=null)   Gson().toJson(response.body())  else "Response not proper"
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataAddTicketResponseBean.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(context,
                            apiEndPoint, "SUCCESS $methodName", e.toString())
                    }
                    return
                } else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataAddTicketResponseBean.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            AddTicketResponseBean::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error = msg + "--" + msg2
                        mutableLiveDataAddTicketResponseBean.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            AddTicketResponseBean::class.java
                        )
                    }

                    sendBroadcastForLogs(
                        context,
                        apiEndPoint, if (response.code() == Constant.STATUS.FAIL) "FAIL $methodName" else methodName, error+"="+strResponse
                    )

                }

            }
        })
    }

    fun responseAddTicketApiCall(): MutableLiveData<AddTicketResponseBean> {
        mutableLiveDataAddTicketResponseBean = MutableLiveData()
        return mutableLiveDataAddTicketResponseBean
    }
    // cancel ticket
    fun requestCancelTicketApiCall(context: Context, request: CancelTicketRequestBean) {
        var methodName = "requestCancelTicketApiCall"
        var apiEndPoint = ApiConstant.NO_SHOW_CANCEL_TICKET_PROCESS
        var call = RetrofitApiService.apiInterface.requestCancelTicketApiCall(request)
        call.enqueue(object : retrofit2.Callback<GeneralResponseBean> {
            override fun onFailure(call: Call<GeneralResponseBean>, t: Throwable) {

                //
                Utils.print("onFailure=====" + t.localizedMessage)
                var msg =
                    if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                        R.string.timeout_error_msg_two) else if (!TextUtils.isEmpty(t.localizedMessage)) t.localizedMessage else context.resources.getString(
                        R.string.internal_server)
                sendBroadcastForLogs(context,
                    apiEndPoint, "$methodName", msg)
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model = GeneralResponseBean()
                    model.message = msg
                    mutableLiveDataGeneralResponseBean.value = model
                } catch (e: Exception) {
                    var model = GeneralResponseBean()
                    model.message = e.message
                    mutableLiveDataGeneralResponseBean.value = model
                }
            }

            override fun onResponse(
                call: Call<GeneralResponseBean>,
                response: Response<GeneralResponseBean>,
            ) {
                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body() != null && null == response.body()!!.status)
                ) {
                    mutableLiveDataGeneralResponseBean.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        GeneralResponseBean::class.java
                    )
                    sendBroadcastForLogs(context,
                        apiEndPoint,
                        "FAILURE $methodName",
                        "Response not proper=${response.body().toString()}")
                    return
                }

                val strResponse =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper"
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataGeneralResponseBean.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(context,
                            apiEndPoint, "SUCCESS $methodName", e.toString())
                    }
                    return
                } else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataGeneralResponseBean.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            GeneralResponseBean::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error = msg + "--" + msg2
                        mutableLiveDataGeneralResponseBean.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            GeneralResponseBean::class.java
                        )
                    }

                    sendBroadcastForLogs(
                        context,
                        apiEndPoint,
                        if (response.code() == Constant.STATUS.FAIL) "FAIL $methodName" else methodName,
                        error + "=" + strResponse
                    )

                }


            }
        })
    }

    fun responseCancelTicketApiCall(): MutableLiveData<GeneralResponseBean> {
        mutableLiveDataGeneralResponseBean = MutableLiveData()
        return mutableLiveDataGeneralResponseBean
    }
    var mutableLiveDataGetTicketDetailsResponseModel =
        MutableLiveData<GetTicketDetailsResponseModel>()
    fun requestGetTicketByIdListApiCall(
        context: Context,
        merchantListRequestBean: HashMap<String, String>,
    ) {
        Utils.print("custAPI 3")
        var methodName = "requestGetTicketByIdListApiCall"
        var apiEndPoint = ApiConstant.GET_TICKET_BY_CUST
        var call = RetrofitApiService.apiInterface.requestGetTicketByIdListApiCall(
            merchantListRequestBean
        )
        call.enqueue(object : retrofit2.Callback<GetTicketDetailsResponseModel> {

            override fun onFailure(call: Call<GetTicketDetailsResponseModel>, t: Throwable) {

                //
                Utils.print("onFailure=====" + t.localizedMessage)
                var msg =
                    if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                        R.string.timeout_error_msg_two) else if (!TextUtils.isEmpty(t.localizedMessage)) t.localizedMessage else context.resources.getString(
                        R.string.internal_server)
                sendBroadcastForLogs(context,
                    apiEndPoint, "$methodName", msg)
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model = GetTicketDetailsResponseModel()
                    model.message = msg
                    mutableLiveDataGetTicketDetailsResponseModel.value = model
                } catch (e: Exception) {
                    var model = GetTicketDetailsResponseModel()
                    model.message = e.message
                    mutableLiveDataGetTicketDetailsResponseModel.value = model
                }
            }

            override fun onResponse(
                call: Call<GetTicketDetailsResponseModel>,
                response: Response<GetTicketDetailsResponseModel>,
            ) {

                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body() != null && null == response.body()!!.status)
                ) {
                    mutableLiveDataGetTicketDetailsResponseModel.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        GetTicketDetailsResponseModel::class.java
                    )
                    sendBroadcastForLogs(context,
                        apiEndPoint,
                        "FAILURE $methodName",
                        "Response not proper=${response.body().toString()}")
                    return
                }

                val strResponse =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper"
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataGetTicketDetailsResponseModel.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(context,
                            apiEndPoint, "SUCCESS $methodName", e.toString())
                    }
                    return
                } else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataGetTicketDetailsResponseModel.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            GetTicketDetailsResponseModel::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error = msg + "--" + msg2
                        mutableLiveDataGetTicketDetailsResponseModel.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            GetTicketDetailsResponseModel::class.java
                        )
                    }

                    sendBroadcastForLogs(
                        context,
                        apiEndPoint,
                        if (response.code() == Constant.STATUS.FAIL) "FAIL $methodName" else methodName,
                        error + "=" + strResponse
                    )

                }

            }
        })
    }

    //
    fun responseGetTicketByIdListApiCall(): MutableLiveData<GetTicketDetailsResponseModel> {
        mutableLiveDataGetTicketDetailsResponseModel = MutableLiveData()
        return mutableLiveDataGetTicketDetailsResponseModel
    }
    var mutableLiveDataGetTicketByCustomerResponse =
        MutableLiveData<GetTicketHistoryResponseModel>()
    fun requestGetTicketByCustomerListApiCall(
        context: Context,
        merchantListRequestBean: HashMap<String, String>,
    ) {
        Utils.print("custAPI 2")
        var methodName = "requestGetTicketByCustomerListApiCall"
        var apiEndPoint = ApiConstant.GET_TICKET_BY_CUST

        var call = RetrofitApiService.apiInterface.requestGetTicketByCustomerListApiCall(
            merchantListRequestBean
        )
        call.enqueue(object : retrofit2.Callback<GetTicketHistoryResponseModel> {
            override fun onFailure(call: Call<GetTicketHistoryResponseModel>, t: Throwable) {

                //
                Utils.print("onFailure=====" + t.localizedMessage)
                var msg =
                    if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                        R.string.timeout_error_msg_two) else if (!TextUtils.isEmpty(t.localizedMessage)) t.localizedMessage else context.resources.getString(
                        R.string.internal_server)
                sendBroadcastForLogs(context,
                    apiEndPoint, "$methodName", msg)
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model = GetTicketHistoryResponseModel()
                    model.message = msg
                    mutableLiveDataGetTicketByCustomerResponse.value = model
                } catch (e: Exception) {

                    mutableLiveDataGetTicketByCustomerResponse.value = null
                }
            }

            override fun onResponse(
                call: Call<GetTicketHistoryResponseModel>,
                response: Response<GetTicketHistoryResponseModel>,
            ) {
                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body() != null && null == response.body()!!.status)
                ) {
                    mutableLiveDataGetTicketByCustomerResponse.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        GetTicketHistoryResponseModel::class.java
                    )
                    sendBroadcastForLogs(context,
                        apiEndPoint,
                        "FAILURE $methodName",
                        "Response not proper=${response.body().toString()}")
                    return
                }
                val strResponse =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper"
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataGetTicketByCustomerResponse.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(context,
                            apiEndPoint, "SUCCESS $methodName", e.toString())
                    }
                    return
                } else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataGetTicketByCustomerResponse.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            GetTicketHistoryResponseModel::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error = msg + "--" + msg2
                        mutableLiveDataGetTicketByCustomerResponse.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            GetTicketHistoryResponseModel::class.java
                        )
                    }

                    sendBroadcastForLogs(
                        context,
                        apiEndPoint,
                        if (response.code() == Constant.STATUS.FAIL) "FAIL $methodName" else methodName,
                        error + "=" + strResponse
                    )

                }


            }
        })
    }

    //
    fun responseGetTicketByCustomerListApiCall(): MutableLiveData<GetTicketHistoryResponseModel> {
        mutableLiveDataGetTicketByCustomerResponse = MutableLiveData()
        return mutableLiveDataGetTicketByCustomerResponse
    }




    fun sendBroadcastForLogs(
        context: Context,
        apiName: String,
        serviceName: String,
        errorDetail: String
    ) {
        var time= Utils.getCurrentDate()+" = "
        try {
            val intent = Intent()
            intent.putExtra(Constant.API_NAME, apiName)
            intent.putExtra(Constant.SERVICE_NAME, serviceName)
            intent.putExtra(Constant.ERROR_DETAIL, time+errorDetail)
            if (!TextUtils.isEmpty(serviceName) && serviceName.contains("FAIL", false)) {
                intent.putExtra(Constant.IS_TOKEN_EXPIRED, true)
            }
            if (!TextUtils.isEmpty(serviceName) && serviceName.contains(Constant.LOGOUT_FORCEFULLY, false)) {
                intent.putExtra(Constant.LOGOUT_FORCEFULLY, true)
            }
            intent.action = Constant.BROADCAST_LOGS
            context.sendBroadcast(intent)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}