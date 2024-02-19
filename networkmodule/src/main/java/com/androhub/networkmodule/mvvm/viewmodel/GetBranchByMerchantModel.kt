package com.androhub.networkmodule.mvvm.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mylib.Repository.AuthRepository
import com.androhub.networkmodule.mvvm.model.request.AnonomusRequestBean
import com.androhub.networkmodule.mvvm.model.request.TokenRequestBean
import com.androhub.networkmodule.mvvm.model.response.DistanceResponseBean
import com.androhub.networkmodule.mvvm.model.response.GetTicketHistoryResponseModel
import com.androhub.networkmodule.mvvm.model.response.aninomus.AninomusLoginResponseBean
import com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant.GetBranchByMerchantResponse
import com.androhub.networkmodule.mvvm.model.response.tokenGenerate.TokenGenerateResponseBean

class GetBranchByMerchantModel(application: Application) : AndroidViewModel(application) {

    private var authRepository = AuthRepository.getInstance()


    fun requestGetBranchByMerchantListApiCall(context: Context, request: HashMap<String, Any>) {
        authRepository.requestGetBranchByMerchantListApiCall(context, request)
    }

    fun responseGetBranchByMerchantApiCall(): MutableLiveData<GetBranchByMerchantResponse> {
        return authRepository.responseGetBranchByMerchantApiCall()
    }

    fun requestGetGenerateTokenApiCall(context: Context, request: TokenRequestBean) {
        authRepository.requestGetGenerateTokenApiCall(context,request)
    }

    fun responseGetGetTokenGenerateApiCall(): MutableLiveData<TokenGenerateResponseBean> {
        return authRepository.responseGetGetTokenGenerateApiCall()
    }

    fun requestGetAninymousLoginApiCall(context: Context, request: AnonomusRequestBean) {
        authRepository.requestGetAninymousLoginApiCall(context,request)
    }

    fun responseGetAninymousLoginApiCall(): MutableLiveData<AninomusLoginResponseBean> {
        return authRepository.responseGetAninymousLoginApiCall()
    }

    fun requestGetOtherBranchByMerchantListApiCall(
        context: Context,
        request: HashMap<String, Any>,
    ) {
        authRepository.requestGetOtherBranchByMerchantListApiCall(context, request)
    }
    fun responseGetOtherBranchByMerchantApiCall(): MutableLiveData<GetBranchByMerchantResponse> {
        return authRepository.responseGetOtherBranchByMerchantApiCall()
    }

    fun requestGetTicketByCustomerListApiCall(context: Context,requestMap: HashMap<String, String>) {
        authRepository.requestGetTicketByCustomerListApiCall(context,requestMap)
    }

    fun responseGetTicketByCustomerListApiCall(): MutableLiveData<GetTicketHistoryResponseModel> {
        Log.e("called","Aaaaaaaa")
        return authRepository.responseGetTicketByCustomerListApiCall()
    }



    fun requestGetBranchDistanceApiCall(context: Context, request: HashMap<String, Any>) {
        authRepository.requestGetBranchDistanceApiCall(context, request)
    }
    fun responseGetBranchDistanceApiCall(): MutableLiveData<DistanceResponseBean> {
        return authRepository.responseGetBranchDistanceApiCall()
    }
}