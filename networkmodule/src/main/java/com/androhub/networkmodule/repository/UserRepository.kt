package com.mylib.Repository

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.androhub.networkmodule.R
import com.androhub.networkmodule.libs.RetrofitApiService
import com.androhub.networkmodule.libs.RetrofitApiServiceImage
import com.androhub.networkmodule.mvvm.model.request.DeleteImageRequestBean
import com.androhub.networkmodule.mvvm.model.response.CheckBranchBean
import com.androhub.networkmodule.mvvm.model.response.GetSegmentResponseBean
import com.androhub.networkmodule.mvvm.model.response.ImageResponseBean
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.BranchDetailsResponse
import com.androhub.networkmodule.utils.ApiConstant
import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.utils.Utils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class UserRepository {
    val gson = GsonBuilder().create()

    companion object {
        fun getInstance(): UserRepository {
            return UserRepository()
        }
    }


    var mutableLiveDataGetSegmentBean = MutableLiveData<GetSegmentResponseBean>()

    //get segment
    fun requestGetSegmentApiCall(context: Context, request: HashMap<String, Any>) {
        var methodName= "requestGetSegmentApiCall"
        var apiEndPoint= ApiConstant.GET_SEGMENT

        var call = RetrofitApiService.apiInterface.requestGetSegment(request)
        call.enqueue(object : retrofit2.Callback<GetSegmentResponseBean> {
            override fun onFailure(call: Call<GetSegmentResponseBean>, t: Throwable) {




                var msg=if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) context.resources.getString(
                    R.string.timeout_error_msg_two) else if (!TextUtils.isEmpty(t.localizedMessage))t.localizedMessage else context.resources.getString(
                    R.string.internal_server)
                sendBroadcastForLogs(context,
                    apiEndPoint,"$methodName", msg)
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model=GetSegmentResponseBean()
                    model.message=msg
                    mutableLiveDataGetSegmentBean.value = model
                }
                catch (e: Exception) {
                    var model=GetSegmentResponseBean()
                    model.message=e.message
                    mutableLiveDataGetSegmentBean.value = model
                }
            }

            override fun onResponse(
                call: Call<GetSegmentResponseBean>,
                response: Response<GetSegmentResponseBean>
            ) {
                if ((null == response.body() && null == response.errorBody()) || TextUtils.isEmpty(
                        response.body().toString()
                    ) || (response.body()!=null && null==response.body()!!.status)
                ) {
                    mutableLiveDataGetSegmentBean.value = gson.fromJson(
                        "{\"message\":\"Unable to fetch details, please try again later\"}",
                        GetSegmentResponseBean::class.java
                    )
                    sendBroadcastForLogs(context,
                        apiEndPoint,"FAILURE $methodName","Response not proper=${response.body().toString()}")
                    return
                }

                val strResponse = if (response.body()!=null) Gson().toJson(response.body())  else "Response not proper"
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataGetSegmentBean.value = response.body()
                        if (response.body()!=null && (response.body()!!.dataSegment==null || TextUtils.isEmpty(response.body()!!.dataSegment!!.segmentName)))
                        {
                            sendBroadcastForLogs(context,
                                apiEndPoint,"NULL $methodName","dataSegment getting null")
                        }
                    } catch (e: Exception) {
                        sendBroadcastForLogs(context,
                            apiEndPoint,"SUCCESS $methodName",e.toString())
                    }
                    return
                }
                else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataGetSegmentBean.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            GetSegmentResponseBean::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error=msg+"--"+msg2
                        mutableLiveDataGetSegmentBean.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            GetSegmentResponseBean::class.java
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
    fun responseGetSegmentApiCall(): MutableLiveData<GetSegmentResponseBean> {
        mutableLiveDataGetSegmentBean = MutableLiveData()
        return mutableLiveDataGetSegmentBean
    }
    var mutableLiveDataIsBranchOpen  = MutableLiveData<CheckBranchBean>()

    fun  requestCheckBranchIsOpenApiCall(context: Context,branchId: HashMap<String,String>){
        var apiEndPoint = ApiConstant.CHECK_BRANCH_ISOPEN
        var methodName = "responseGetOtherBranchByMerchantApiCall"
        var call = RetrofitApiService.apiInterface.requestCheckBranchApiCall(branchId)
        call.enqueue(object : retrofit2.Callback<CheckBranchBean>{
            override fun onResponse(
                call: Call<CheckBranchBean>,
                response: Response<CheckBranchBean>
            ) {
                if (response.code() == Constant.STATUS.SUCCESS) {

                    try {
                        mutableLiveDataIsBranchOpen.value = response.body()
                    } catch (e: Exception) {
                        sendBroadcastForLogs(
                            context,
                            apiEndPoint, "SUCCESS ", e.toString()
                        )
                    }
                    return
                }
                else {
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataIsBranchOpen.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            CheckBranchBean::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error=msg+"--"+msg2
                        mutableLiveDataIsBranchOpen.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            CheckBranchBean::class.java
                        )
                    }

                    sendBroadcastForLogs(
                        context,
                        apiEndPoint, if (response.code() == Constant.STATUS.FAIL) "FAIL " else "methodName", error+"="+"strResponse"
                    )

                }
            }

            override fun onFailure(call: Call<CheckBranchBean>, t: Throwable) {
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

            }

        })

    }

    fun responseCeckIsBranchOpen():MutableLiveData<CheckBranchBean>{
        mutableLiveDataIsBranchOpen = MutableLiveData()
        return mutableLiveDataIsBranchOpen
    }

    var mutableLiveDataGetBranchByBranchIdList = MutableLiveData<BranchDetailsResponse>()
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
    fun responseGetBranchByBranchIdApiCall(): MutableLiveData<BranchDetailsResponse> {
        mutableLiveDataGetBranchByBranchIdList = MutableLiveData()
        return mutableLiveDataGetBranchByBranchIdList
    }

    var mutableLiveDataFormImage = MutableLiveData<ImageResponseBean>()
    fun requestUploeadImage(requireContext: Context, type: RequestBody?, formId: RequestBody?, part: MultipartBody.Part){
        var methodName = ApiConstant.UPLOAD_IMAGE
        var apiEndPint = ApiConstant.UPLOAD_IMAGE
        var call = RetrofitApiServiceImage.apiInterface.requestUploeadImage(type,formId,part)
        call.enqueue(object :retrofit2.Callback<ImageResponseBean>{
            override fun onResponse(
                call: Call<ImageResponseBean>,
                response: Response<ImageResponseBean>,
            ) {
                val strResponses =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper"

                if (response.isSuccessful){
                    try {
                        Log.e("eeee",response.body().toString())
                        mutableLiveDataFormImage.value = response.body()

                    }catch (e:Exception){
                        sendBroadcastForLogs(
                            requireContext,
                            apiEndPint, "SUCCESS $methodName", e.toString()
                        )
                    }
                }else{
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataFormImage.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            ImageResponseBean::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error= msg + "--" +msg2
                        mutableLiveDataFormImage.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            ImageResponseBean::class.java
                        )
                    }
                    sendBroadcastForLogs(
                        requireContext,
                        apiEndPint, "Failuer $methodName",error +"=="+response.body().toString()
                    )
                }
            }

            override fun onFailure(call: Call<ImageResponseBean>, t: Throwable) {
                Log.e("Usher",call.toString())

                Utils.print("onFailure=====" + t.localizedMessage)
                var msg=if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) requireContext.resources.getString(
                    R.string.timeout_error_msg_two) else if (!TextUtils.isEmpty(t.localizedMessage))t.localizedMessage else requireContext.resources.getString(
                    R.string.internal_server)
                sendBroadcastForLogs(requireContext,
                    apiEndPint,"$methodName", msg)
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
//                    var model= ImageResponseBean()
//                    model.message=msg
//                    mutableLiveDataFormImage.value = model
                }
                catch (e: Exception) {
//                    var model= ImageResponseBean()
//                    model.message= e.message.toString()
//                    mutableLiveDataFormImage.value = model
                }
            }

        })


    }
    fun responseUploeadImage():MutableLiveData<ImageResponseBean>{
        mutableLiveDataFormImage = MutableLiveData()
        return mutableLiveDataFormImage
    }
    var mutableLiveDataDeleteImage = MutableLiveData<ImageResponseBean>()
    fun requestDeleteImage(requireContext: Context, part: DeleteImageRequestBean){
        var methodName = "requestUsherPin"
        var apiEndPint = ApiConstant.DELETE_IMAGE
        var call = RetrofitApiService.apiInterface.requestDeleteImage(part)
        call.enqueue(object :retrofit2.Callback<ImageResponseBean>{
            override fun onResponse(
                call: Call<ImageResponseBean>,
                response: Response<ImageResponseBean>,
            ) {
                val strResponses =
                    if (response.body() != null) Gson().toJson(response.body()) else "Response not proper"

                if (response.isSuccessful){
                    try {
                        mutableLiveDataDeleteImage.value = response.body()

                    }catch (e:Exception){
                        sendBroadcastForLogs(
                            requireContext,
                            apiEndPint, "SUCCESS $methodName", e.toString()
                        )
                    }
                }else{
                    var error = "error ${response.code()}"
                    try {
                        mutableLiveDataDeleteImage.value = gson.fromJson(
                            response.errorBody()!!.string(),
                            ImageResponseBean::class.java
                        )
                    } catch (e: Exception) {

                        var msg2 = if (response.raw() != null) response.raw().toString() else ""
                        var msg = if (response.errorBody() != null) response.errorBody()!!
                            .string() else msg2
                        error= msg + "--" +msg2
                        mutableLiveDataDeleteImage.value = gson.fromJson(
                            "{\"message\":\"${response.code()}  Server configuration problems, please try again later ${msg}\"}",
                            ImageResponseBean::class.java
                        )
                    }
//                    Response{protocol=h2, code=500, message=, url=https://api.test.balador.io/user/usher/pin}
                    sendBroadcastForLogs(
                        requireContext,
                        apiEndPint, "Failuer $methodName",error +"=="+response.body().toString()
                    )
                }
            }

            override fun onFailure(call: Call<ImageResponseBean>, t: Throwable) {
                Log.e("Usher",call.toString())

                Utils.print("onFailure=====" + t.localizedMessage)
                var msg=if (!TextUtils.isEmpty(t.localizedMessage) && t.localizedMessage.contains("Failed to connect")) requireContext.resources.getString(
                    R.string.timeout_error_msg_two) else if (!TextUtils.isEmpty(t.localizedMessage))t.localizedMessage else requireContext.resources.getString(
                    R.string.internal_server)
                sendBroadcastForLogs(requireContext,
                    apiEndPint,"$methodName", msg)
                //Failed to connect to api.staging.dooree.io/15.184.166.137:443
                try {
                    var model= ImageResponseBean()
                    model.message=msg
                    mutableLiveDataDeleteImage.value = model
                }
                catch (e: Exception) {
                    var model= ImageResponseBean()
                    model.message= e.message.toString()
                    mutableLiveDataDeleteImage.value = model
                }
            }

        })


    }

    fun responseDeleteImage():MutableLiveData<ImageResponseBean>{
        mutableLiveDataDeleteImage = MutableLiveData()
        return mutableLiveDataDeleteImage
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