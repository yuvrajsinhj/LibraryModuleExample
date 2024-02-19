package com.androhub.networkmodule.mvvm.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mylib.Repository.QueueRepository
import com.mylib.Repository.UserRepository
import com.androhub.networkmodule.mvvm.model.request.AddTicketRequestBean
import com.androhub.networkmodule.mvvm.model.request.CancelTicketRequestBean
import com.androhub.networkmodule.mvvm.model.request.DeleteImageRequestBean
import com.androhub.networkmodule.mvvm.model.request.TicketCountRequestBean
import com.androhub.networkmodule.mvvm.model.response.AddTicketCountResponseBean
import com.androhub.networkmodule.mvvm.model.response.AddTicketResponseBean
import com.androhub.networkmodule.mvvm.model.response.CheckBranchBean
import com.androhub.networkmodule.mvvm.model.response.GeneralResponseBean
import com.androhub.networkmodule.mvvm.model.response.GetTicketDetailsResponseModel
import com.androhub.networkmodule.mvvm.model.response.GetTicketHistoryResponseModel
import com.androhub.networkmodule.mvvm.model.response.ImageResponseBean
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.BranchDetailsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CustomerBookingTicketViewModel(application: Application) : AndroidViewModel(application) {
    var repository = QueueRepository.getInstance()
    var userRepository = UserRepository.getInstance()

    fun requestGetTicketByCustomerListApiCall(context: Context,requestMap: HashMap<String, String>) {
        repository.requestGetTicketByCustomerListApiCall(context,requestMap)
    }

    fun responseGetTicketByCustomerListApiCall(): MutableLiveData<GetTicketHistoryResponseModel> {
        return repository.responseGetTicketByCustomerListApiCall()
    }
    fun requestGetTicketCountByBranchIdApiCall(context: Context, requestMap: TicketCountRequestBean) {
        repository.requestGetTicketCountByBranchIdApiCall(context,requestMap)
    }

    fun responseGetTicketCountByBranchIdApiCall(): MutableLiveData<AddTicketCountResponseBean> {
        return repository.responseGetTicketCountByBranchIdApiCall()
    }
    fun requestCheckBranchApiCall(context: Context,request: HashMap<String, String>){
        userRepository.requestCheckBranchIsOpenApiCall(context,request)
    }
    fun responseCheckBranchApiCall():MutableLiveData<CheckBranchBean>{
        return userRepository.responseCeckIsBranchOpen()
    }
    fun requestGetBranchDetailsByBranchIdApiCall(context: Context,request: HashMap<String,Any>) {
        userRepository.requestGetBranchDetailsByBranchIdApiCall(context,request)
    }

    fun responseGetBranchByBranchIdApiCall(): MutableLiveData<BranchDetailsResponse> {
        return userRepository.responseGetBranchByBranchIdApiCall()
    }
    fun requestAddTicketApiCall(context: Context,request: AddTicketRequestBean) {
        repository.requestAddTicketApiCall(context,request)
    }

    fun responseAddTicketApiCall(): MutableLiveData<AddTicketResponseBean> {
        return repository.responseAddTicketApiCall()
    }

    fun requestCancelTicketApiCall(context: Context,request: CancelTicketRequestBean) {
        repository.requestCancelTicketApiCall(context,request)
    }

    fun responseCancelTicketApiCall(): MutableLiveData<GeneralResponseBean> {
        return repository.responseCancelTicketApiCall()
    }
    fun requestGetTicketByIdListApiCall(context: Context,requestMap: HashMap<String,String>) {
        repository.requestGetTicketByIdListApiCall(context,requestMap)
    }

    fun responseGetTicketByIdListApiCall(): MutableLiveData<GetTicketDetailsResponseModel> {
        return repository.responseGetTicketByIdListApiCall()
    }

    fun requestUploeadImage(context: Context, type: RequestBody?, formId: RequestBody?, part: MultipartBody.Part){
        userRepository.requestUploeadImage(context,type,formId,part)
    }
    fun responseUploeadImageApiCall():MutableLiveData<ImageResponseBean>{
        return userRepository.responseUploeadImage()
    }
    fun requestDeleteImage(context: Context,link: DeleteImageRequestBean){
        userRepository.requestDeleteImage(context,link)
    }
    fun responseDeleteImage():MutableLiveData<ImageResponseBean>{
        return userRepository.responseDeleteImage()
    }
}