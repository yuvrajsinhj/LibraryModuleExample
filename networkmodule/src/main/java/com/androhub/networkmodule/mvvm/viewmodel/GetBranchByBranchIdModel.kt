package com.androhub.networkmodule.mvvm.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mylib.Repository.AuthRepository
import com.mylib.Repository.QueueRepository
import com.mylib.Repository.UserRepository
import com.androhub.networkmodule.mvvm.model.request.FasterNearByDataBean
import com.androhub.networkmodule.mvvm.model.response.GetFasterBranchListResponseBean
import com.androhub.networkmodule.mvvm.model.response.GetSegmentResponseBean
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.BranchDetailsResponse


class GetBranchByBranchIdModel(application: Application) : AndroidViewModel(application) {
    private var authRepository = AuthRepository.getInstance()
    var queuerepository = QueueRepository.getInstance()
    var userRepository = UserRepository.getInstance()


    fun requestGetSegmentApiCall(context: Context, request: HashMap<String, Any>) {
        userRepository.requestGetSegmentApiCall(context,request)
    }

    fun responseGetSegmentApiCall(): MutableLiveData<GetSegmentResponseBean> {
        return userRepository.responseGetSegmentApiCall()
    }

    fun requestGetFasterBranchListApiCall(context: Context, request: FasterNearByDataBean) {
        queuerepository.requestGetFasterBranchListApiCall(context,request)
    }

    fun responseGetFasterBranchListApiCall(): MutableLiveData<GetFasterBranchListResponseBean> {
        return queuerepository.responseGetFasterBranchListApiCall()
    }

    fun requestGetBranchDetailsByBranchIdApiCall(context: Context,request: HashMap<String,Any>) {
        authRepository.requestGetBranchDetailsByBranchIdApiCall(context,request)
    }

    fun responseGetBranchByBranchIdApiCall(): MutableLiveData<BranchDetailsResponse> {
        return authRepository.responseGetBranchByBranchIdApiCall()
    }
}