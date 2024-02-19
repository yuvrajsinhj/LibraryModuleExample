package com.androhub.networkmodule.libs


import com.androhub.networkmodule.mvvm.model.request.AddTicketRequestBean
import com.androhub.networkmodule.mvvm.model.request.AnonomusRequestBean
import com.androhub.networkmodule.mvvm.model.request.CancelTicketRequestBean
import com.androhub.networkmodule.mvvm.model.request.DeleteImageRequestBean
import com.androhub.networkmodule.mvvm.model.request.FasterNearByDataBean
import com.androhub.networkmodule.mvvm.model.request.TicketCountRequestBean
import com.androhub.networkmodule.mvvm.model.request.TokenRequestBean
import com.androhub.networkmodule.mvvm.model.response.AddTicketCountResponseBean
import com.androhub.networkmodule.mvvm.model.response.AddTicketResponseBean
import com.androhub.networkmodule.mvvm.model.response.CheckBranchBean
import com.androhub.networkmodule.mvvm.model.response.DistanceResponseBean
import com.androhub.networkmodule.mvvm.model.response.GeneralResponseBean
import com.androhub.networkmodule.mvvm.model.response.GetFasterBranchListResponseBean
import com.androhub.networkmodule.mvvm.model.response.GetSegmentResponseBean
import com.androhub.networkmodule.mvvm.model.response.GetTicketDetailsResponseModel
import com.androhub.networkmodule.mvvm.model.response.GetTicketHistoryResponseModel
import com.androhub.networkmodule.mvvm.model.response.ImageResponseBean
import com.androhub.networkmodule.mvvm.model.response.aninomus.AninomusLoginResponseBean
import com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant.GetBranchByMerchantResponse
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.BranchDetailsResponse
import com.androhub.networkmodule.mvvm.model.response.tokenGenerate.TokenGenerateResponseBean
import com.androhub.networkmodule.utils.ApiConstant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface ApiInterface {
    @POST(ApiConstant.GET_ANINYMOUS_LOGIN)
    fun requestGetAninymousLoginApiCall(@Body request: AnonomusRequestBean): Call<AninomusLoginResponseBean>

    @POST(ApiConstant.GET_GENERATE_TOKEN_SDK)
    fun requestGetGenerateTokenApiCall(@Body request: TokenRequestBean): Call<TokenGenerateResponseBean>



    @GET(ApiConstant.GET_BRANCH_BY_MERCHANT)
    fun requestGetBranchByMerchantListApiCall(@QueryMap hashMap: HashMap<String, Any>): Call<GetBranchByMerchantResponse>

    @GET(ApiConstant.GET_BRANCH_DISTANCE)
    fun requestGetBranchDistanceApiCall(@QueryMap hashMap: HashMap<String, Any>): Call<DistanceResponseBean>

    @GET(ApiConstant.GET_SEGMENT)
    fun requestGetSegment(@QueryMap hashMap: HashMap<String, Any>): Call<GetSegmentResponseBean>

    //get faster branches
    @POST(ApiConstant.GET_FASTER_BRANCH)
    fun requestGetFasterBranchListApiCall(@Body request: FasterNearByDataBean): Call<GetFasterBranchListResponseBean>


    @GET(ApiConstant.GET_BRANCH_DETAIL_BY_BRANCHID)
    fun requestGetBranchDetailsByBranchIdApiCall(@QueryMap hashMap: HashMap<String, Any>): Call<BranchDetailsResponse>


    @POST(ApiConstant.GET_TICKET_COUNT)
    fun requestGetTicketCountByBranchIdApiCall(@Body request: TicketCountRequestBean): Call<AddTicketCountResponseBean>

    @POST(ApiConstant.ADD_TICKET)
    fun requestAddTicketApiCall(@Body request: AddTicketRequestBean): Call<AddTicketResponseBean>

    @GET(ApiConstant.CHECK_BRANCH_ISOPEN)
    fun requestCheckBranchApiCall(@QueryMap hashMap: HashMap<String, String>): Call<CheckBranchBean>

    @POST(ApiConstant.NO_SHOW_CANCEL_TICKET_PROCESS)
    fun requestCancelTicketApiCall(@Body request: CancelTicketRequestBean): Call<GeneralResponseBean>

    @GET(ApiConstant.GET_TICKET_BY_CUST)   //<<---------gettivcket
    fun requestGetTicketByIdListApiCall(@QueryMap hashMap: HashMap<String, String>): Call<GetTicketDetailsResponseModel>

    @GET(ApiConstant.GET_TICKET_BY_CUST)   //<<---------gettivcket
    fun requestGetTicketByCustomerListApiCall(@QueryMap hashMap: HashMap<String, String>): Call<GetTicketHistoryResponseModel>
    @Multipart
    @POST(ApiConstant.UPLOAD_IMAGE)
    fun requestUploeadImage(@Part("type") type: RequestBody?, @Part("formId") formId: RequestBody?, @Part file: MultipartBody.Part): Call<ImageResponseBean>
    @POST(ApiConstant.DELETE_IMAGE)
    fun requestDeleteImage(@Body request: DeleteImageRequestBean): Call<ImageResponseBean>

}