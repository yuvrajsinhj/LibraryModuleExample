package com.mylib.Repository

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.androhub.networkmodule.R
import com.androhub.networkmodule.libs.RetrofitApiService
import com.androhub.networkmodule.mvvm.model.request.AnonomusRequestBean
import com.androhub.networkmodule.mvvm.model.request.TokenRequestBean
import com.androhub.networkmodule.mvvm.model.response.DistanceResponseBean
import com.androhub.networkmodule.mvvm.model.response.GetTicketHistoryResponseModel
import com.androhub.networkmodule.mvvm.model.response.aninomus.AninomusLoginResponseBean
import com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant.GetBranchByMerchantResponse
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.BranchDetailsResponse
import com.androhub.networkmodule.mvvm.model.response.tokenGenerate.TokenGenerateResponseBean
import com.androhub.networkmodule.utils.ApiConstant
import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.utils.Utils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response

class AuthRepository {
    companion object {
        fun getInstance(): AuthRepository {
            return AuthRepository()
        }
    }

    val gson = GsonBuilder().create()


    var mutableLiveDataGetBranchByMerchantIdList = MutableLiveData<GetBranchByMerchantResponse>()
    var mutableLiveDataGetAninymousLoginData = MutableLiveData<AninomusLoginResponseBean>()
    var mutableLiveDataGetTokenGenerateData = MutableLiveData<TokenGenerateResponseBean>()
    var mutableLiveDataGetOtherBranchByMerchantIdList = MutableLiveData<GetBranchByMerchantResponse>()
    var mutableLiveDataGetBranchDistance= MutableLiveData<DistanceResponseBean>()





    fun requestGetGenerateTokenApiCall(
        context: Context,
        anonomusRequestBean: TokenRequestBean
    ) {
        var methodName = "requestGetBranchByMerchantListApiCall"
        var apiEndPoint = ApiConstant.GET_GENERATE_TOKEN_SDK

        var call = RetrofitApiService.apiInterface.requestGetGenerateTokenApiCall(
            anonomusRequestBean
        )
        call.enqueue(object : retrofit2.Callback<TokenGenerateResponseBean> {
            override fun onFailure(call: Call<TokenGenerateResponseBean>, t: Throwable) {
                Utils.print("onFailure=====" + t.localizedMessage)


                var msg =
                    if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                        R.string.timeout_error_msg_two
                    ) else if (!TextUtils.isEmpty(t.localizedMessage)) t.localizedMessage else context.resources.getString(
                        R.string.internal_server
                    )
                sendBroadcastForLogs(
                    context,
                    apiEndPoint, "$methodName", msg
                )
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model = TokenGenerateResponseBean()
                    model.message = msg
                    mutableLiveDataGetTokenGenerateData.value = model
                } catch (e: Exception) {
                    var model = TokenGenerateResponseBean()
                    model.message = e.message
                    mutableLiveDataGetTokenGenerateData.value = model
                }
            }

            override fun onResponse(
                call: Call<TokenGenerateResponseBean>,
                response: Response<TokenGenerateResponseBean>
            ) {

                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body() != null && null == response.body()!!.status)
                ) {
                    mutableLiveDataGetTokenGenerateData.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        TokenGenerateResponseBean::class.java
                    )
                    sendBroadcastForLogs(
                        context,
                        apiEndPoint,
                        "FAILURE $methodName",
                        "Response not proper=${response.body().toString()}"
                    )
                    return
                }
                val strResponse =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper - "
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataGetTokenGenerateData.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(
                            context,
                            apiEndPoint, "SUCCESS $methodName", e.toString()
                        )
                    }
                    return
                }
                else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataGetTokenGenerateData.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            TokenGenerateResponseBean::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error=msg+"--"+msg2
                        mutableLiveDataGetTokenGenerateData.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            TokenGenerateResponseBean::class.java
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

    //
    fun responseGetGetTokenGenerateApiCall(): MutableLiveData<TokenGenerateResponseBean> {
        mutableLiveDataGetTokenGenerateData = MutableLiveData()
        return mutableLiveDataGetTokenGenerateData
    }



    fun requestGetAninymousLoginApiCall(
        context: Context,
        anonomusRequestBean: AnonomusRequestBean
    ) {
        var methodName = "requestGetBranchByMerchantListApiCall"
        var apiEndPoint = ApiConstant.GET_ANINYMOUS_LOGIN

        var call = RetrofitApiService.apiInterface.requestGetAninymousLoginApiCall(
            anonomusRequestBean
        )
        call.enqueue(object : retrofit2.Callback<AninomusLoginResponseBean> {
            override fun onFailure(call: Call<AninomusLoginResponseBean>, t: Throwable) {
                Utils.print("onFailure=====" + t.localizedMessage)


                var msg =
                    if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                        R.string.timeout_error_msg_two
                    ) else if (!TextUtils.isEmpty(t.localizedMessage)) t.localizedMessage else context.resources.getString(
                        R.string.internal_server
                    )
                sendBroadcastForLogs(
                    context,
                    apiEndPoint, "$methodName", msg
                )
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model = AninomusLoginResponseBean()
                    model.message = msg
                    mutableLiveDataGetAninymousLoginData.value = model
                } catch (e: Exception) {
                    var model = AninomusLoginResponseBean()
                    model.message = e.message
                    mutableLiveDataGetAninymousLoginData.value = model
                }
            }

            override fun onResponse(
                call: Call<AninomusLoginResponseBean>,
                response: Response<AninomusLoginResponseBean>
            ) {

                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body() != null && null == response.body()!!.status)
                ) {
                    mutableLiveDataGetAninymousLoginData.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        AninomusLoginResponseBean::class.java
                    )
                    sendBroadcastForLogs(
                        context,
                        apiEndPoint,
                        "FAILURE $methodName",
                        "Response not proper=${response.body().toString()}"
                    )
                    return
                }
                val strResponse =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper - "
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataGetAninymousLoginData.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(
                            context,
                            apiEndPoint, "SUCCESS $methodName", e.toString()
                        )
                    }
                    return
                }
                else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataGetAninymousLoginData.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            AninomusLoginResponseBean::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error=msg+"--"+msg2
                        mutableLiveDataGetAninymousLoginData.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            AninomusLoginResponseBean::class.java
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

    //
    fun responseGetAninymousLoginApiCall(): MutableLiveData<AninomusLoginResponseBean> {
        mutableLiveDataGetAninymousLoginData = MutableLiveData()
        return mutableLiveDataGetAninymousLoginData
    }


    fun requestGetBranchByMerchantListApiCall(
        context: Context,
        merchantListRequestBean: HashMap<String, Any>
    ) {
        var methodName = "requestGetBranchByMerchantListApiCall"
        var apiEndPoint = ApiConstant.GET_BRANCH_BY_MERCHANT

        var call = RetrofitApiService.apiInterface.requestGetBranchByMerchantListApiCall(
            merchantListRequestBean
        )
        call.enqueue(object : retrofit2.Callback<GetBranchByMerchantResponse> {
            override fun onFailure(call: Call<GetBranchByMerchantResponse>, t: Throwable) {
                Utils.print("onFailure=====" + t.localizedMessage)


                var msg =
                    if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                        R.string.timeout_error_msg_two
                    ) else if (!TextUtils.isEmpty(t.localizedMessage)) t.localizedMessage else context.resources.getString(
                        R.string.internal_server
                    )
                sendBroadcastForLogs(
                    context,
                    apiEndPoint, "$methodName", msg
                )
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model = GetBranchByMerchantResponse()
                    model.message = msg
                    mutableLiveDataGetBranchByMerchantIdList.value = model
                } catch (e: Exception) {
                    var model = GetBranchByMerchantResponse()
                    model.message = e.message
                    mutableLiveDataGetBranchByMerchantIdList.value = model
                }
            }

            override fun onResponse(
                call: Call<GetBranchByMerchantResponse>,
                response: Response<GetBranchByMerchantResponse>
            ) {

                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body() != null && null == response.body()!!.status)
                ) {
                    mutableLiveDataGetBranchByMerchantIdList.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        GetBranchByMerchantResponse::class.java
                    )
                    sendBroadcastForLogs(
                        context,
                        apiEndPoint,
                        "FAILURE $methodName",
                        "Response not proper=${response.body().toString()}"
                    )
                    return
                }
                val strResponse =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper - "
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataGetBranchByMerchantIdList.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(
                            context,
                            apiEndPoint, "SUCCESS $methodName", e.toString()
                        )
                    }
                    return
                }
                else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataGetBranchByMerchantIdList.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            GetBranchByMerchantResponse::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error=msg+"--"+msg2
                        mutableLiveDataGetBranchByMerchantIdList.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            GetBranchByMerchantResponse::class.java
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

    //
    fun responseGetBranchByMerchantApiCall(): MutableLiveData<GetBranchByMerchantResponse> {
        mutableLiveDataGetBranchByMerchantIdList = MutableLiveData()
        return mutableLiveDataGetBranchByMerchantIdList
    }



    fun requestGetOtherBranchByMerchantListApiCall(
        context: Context,
        merchantListRequestBean: HashMap<String, Any>
    ) {
        var methodName = "requestGetOtherBranchByMerchantListApiCall"
        var apiEndPoint = ApiConstant.GET_BRANCH_BY_MERCHANT
        var call = RetrofitApiService.apiInterface.requestGetBranchByMerchantListApiCall(
            merchantListRequestBean
        )
        call.enqueue(object : retrofit2.Callback<GetBranchByMerchantResponse> {
            override fun onFailure(call: Call<GetBranchByMerchantResponse>, t: Throwable) {
                Utils.print("onFailure=====" + t.localizedMessage)

                var msg =
                    if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                        R.string.timeout_error_msg_two
                    ) else if (!TextUtils.isEmpty(t.localizedMessage)) t.localizedMessage else context.resources.getString(
                        R.string.internal_server
                    )
                sendBroadcastForLogs(
                    context,
                    apiEndPoint, "$methodName", msg
                )
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model = GetBranchByMerchantResponse()
                    model.message = msg
                    mutableLiveDataGetOtherBranchByMerchantIdList.value = model
                } catch (e: Exception) {
                    var model = GetBranchByMerchantResponse()
                    model.message = e.message
                    mutableLiveDataGetOtherBranchByMerchantIdList.value = model
                }
            }

            override fun onResponse(
                call: Call<GetBranchByMerchantResponse>,
                response: Response<GetBranchByMerchantResponse>
            ) {
                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body() != null && null == response.body()!!.status)
                ) {
                    mutableLiveDataGetOtherBranchByMerchantIdList.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        GetBranchByMerchantResponse::class.java
                    )
                    sendBroadcastForLogs(
                        context,
                        apiEndPoint,
                        "FAILURE $methodName",
                        "Response not proper=${response.body().toString()}"
                    )
                    return
                }
                val strResponse =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper - "
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataGetOtherBranchByMerchantIdList.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(
                            context,
                            apiEndPoint, "SUCCESS $methodName", e.toString()
                        )
                    }
                    return
                }

                else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataGetOtherBranchByMerchantIdList.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            GetBranchByMerchantResponse::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error=msg+"--"+msg2
                        mutableLiveDataGetOtherBranchByMerchantIdList.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            GetBranchByMerchantResponse::class.java
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

    //
    fun responseGetOtherBranchByMerchantApiCall(): MutableLiveData<GetBranchByMerchantResponse> {
        mutableLiveDataGetOtherBranchByMerchantIdList = MutableLiveData()
        return mutableLiveDataGetOtherBranchByMerchantIdList
    }


    fun requestGetBranchDistanceApiCall(
        context: Context,
        merchantListRequestBean: HashMap<String, Any>
    ) {
        var methodName = "requestGetOtherBranchByMerchantListApiCall"
        var apiEndPoint = ApiConstant.GET_BRANCH_DISTANCE
        var call = RetrofitApiService.apiInterface.requestGetBranchDistanceApiCall(
            merchantListRequestBean
        )
        call.enqueue(object : retrofit2.Callback<DistanceResponseBean> {
            override fun onFailure(call: Call<DistanceResponseBean>, t: Throwable) {
                Utils.print("onFailure=====" + t.localizedMessage)

                var msg =
                    if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                        R.string.timeout_error_msg_two
                    ) else if (!TextUtils.isEmpty(t.localizedMessage)) t.localizedMessage else context.resources.getString(
                        R.string.internal_server
                    )
                sendBroadcastForLogs(
                    context,
                    apiEndPoint, "$methodName", msg
                )
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model = DistanceResponseBean()
                    model.message = msg
                    mutableLiveDataGetBranchDistance.value = model
                } catch (e: Exception) {
                    var model = DistanceResponseBean()
                    model.message = e.message
                    mutableLiveDataGetBranchDistance.value = model
                }
            }

            override fun onResponse(
                call: Call<DistanceResponseBean>,
                response: Response<DistanceResponseBean>
            ) {
                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body() != null && null == response.body()!!.status)
                ) {
                    mutableLiveDataGetBranchDistance.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        DistanceResponseBean::class.java
                    )
                    sendBroadcastForLogs(
                        context,
                        apiEndPoint,
                        "FAILURE $methodName",
                        "Response not proper=${response.body().toString()}"
                    )
                    return
                }
                val strResponse =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper - "
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataGetBranchDistance.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(
                            context,
                            apiEndPoint, "SUCCESS $methodName", e.toString()
                        )
                    }
                    return
                }

                else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataGetBranchDistance.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            DistanceResponseBean::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error=msg+"--"+msg2
                        mutableLiveDataGetBranchDistance.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            DistanceResponseBean::class.java
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

    fun responseGetBranchDistanceApiCall(): MutableLiveData<DistanceResponseBean> {
        mutableLiveDataGetBranchDistance = MutableLiveData()
        return mutableLiveDataGetBranchDistance
    }



    fun requestGetBranchDetailsByBranchIdApiCall(context: Context, merchantListRequestBean: HashMap<String, Any>) {
        var methodName = "requestGetBranchDetailsByBranchIdApiCall"
        var apiEndPoint = ApiConstant.GET_BRANCH_DETAIL_BY_BRANCHID
        var call = RetrofitApiService.apiInterface.requestGetBranchDetailsByBranchIdApiCall(
            merchantListRequestBean
        )
        call.enqueue(object : retrofit2.Callback<BranchDetailsResponse> {
            override fun onFailure(call: Call<BranchDetailsResponse>, t: Throwable) {

                //
                Utils.print("onFailure=====" + t.localizedMessage)
                var msg =
                    if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                        R.string.timeout_error_msg_two
                    ) else if (!TextUtils.isEmpty(t.localizedMessage)) t.localizedMessage else context.resources.getString(
                        R.string.internal_server
                    )
                sendBroadcastForLogs(
                    context,
                    apiEndPoint, "$methodName", msg
                )
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model = BranchDetailsResponse()
                    model.message = msg
                    mutableLiveDataGetBranchByBranchIdList.value = model
                } catch (e: Exception) {
                    var model = BranchDetailsResponse()
                    model.message = e.message
                    mutableLiveDataGetBranchByBranchIdList.value = model
                }
            }

            override fun onResponse(
                call: Call<BranchDetailsResponse>,
                response: Response<BranchDetailsResponse>
            ) {
                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body() != null && null == response.body()!!.status)
                ) {
                    mutableLiveDataGetBranchByBranchIdList.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        BranchDetailsResponse::class.java
                    )
                    sendBroadcastForLogs(
                        context,
                        apiEndPoint,
                        "FAILURE $methodName",
                        "Response not proper=${response.body().toString()}"
                    )
                    return
                }

                val strResponse =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper - "
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataGetBranchByBranchIdList.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(
                            context,
                            apiEndPoint, "SUCCESS $methodName", e.toString()
                        )
                    }
                    return
                }
                else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataGetBranchByBranchIdList.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            BranchDetailsResponse::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error=msg+"--"+msg2
                        mutableLiveDataGetBranchByBranchIdList.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            BranchDetailsResponse::class.java
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

    //
    var mutableLiveDataGetBranchByBranchIdList = MutableLiveData<BranchDetailsResponse>()

    fun responseGetBranchByBranchIdApiCall(): MutableLiveData<BranchDetailsResponse> {
        mutableLiveDataGetBranchByBranchIdList = MutableLiveData()
        return mutableLiveDataGetBranchByBranchIdList
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
        var time=Utils.getCurrentDate()+" = "
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