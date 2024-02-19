package com.androhub.networkmodule.mvvm.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mylib.Repository.QueueRepository
import com.androhub.networkmodule.mvvm.model.request.CancelTicketRequestBean
import com.androhub.networkmodule.mvvm.model.response.GeneralResponseBean

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {
    var queueRepository = QueueRepository.getInstance()

    fun requestCancelTicketApiCall(context: Context, request: CancelTicketRequestBean) {
        queueRepository.requestCancelTicketApiCall(context,request)
    }

    fun responseCancelTicketApiCall(): MutableLiveData<GeneralResponseBean> {
        return queueRepository.responseCancelTicketApiCall()
    }


}